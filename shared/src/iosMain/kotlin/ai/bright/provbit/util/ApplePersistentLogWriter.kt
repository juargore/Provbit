package ai.bright.provbit.util

import ai.bright.core.fs.FileSystem.appendFile
import ai.bright.core.fs.FileSystem.mkdir
import ai.bright.core.fs.FileSystem.readDir
import ai.bright.core.fs.FileSystem.unlink
import ai.bright.core.fs.FileType
import ai.bright.core.fs.Path
import ai.bright.core.fs.PathComponent
import ai.bright.core.fs.StatResult
import ai.bright.provbit.AppConstants
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSError
import platform.Foundation.NSFileCreationDate
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileModificationDate
import platform.Foundation.NSFileSize
import platform.Foundation.NSFileType
import platform.Foundation.NSFileTypeDirectory
import platform.Foundation.NSFileTypeRegular
import platform.Foundation.NSThread
import platform.Foundation.NSTimeZone
import platform.Foundation.NSURL
import platform.Foundation.URLByAppendingPathComponent
import platform.Foundation.date
import platform.Foundation.lastPathComponent
import platform.Foundation.localTimeZone
import platform.Foundation.timeIntervalSince1970

class ApplePersistentLogWriter(private val logDirectoryPath: String) : LogWriter() {
    private val logFilePrefix = "log"
    private val maxFileSize = 10 * 1024 * 1024 // 10 MB
    private val maxFileCount = 5

    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        appendFile(
            path = retrieveTargetFilePath(logDirectoryPath = logDirectoryPath),
            contents = formatMessage(severity, message, tag, throwable),
            create = true,
        )
    }

    private fun retrieveTargetFilePath(logDirectoryPath: String): String {
        val formatter = NSDateFormatter()
        formatter.timeZone = NSTimeZone.localTimeZone
        formatter.dateFormat = "yyyy-MM-dd_HH.mm.ss"
        val timestamp = formatter.stringFromDate(NSDate.date())

        val newLogFilePath = "$logDirectoryPath/${logFilePrefix}_$timestamp.log"

        // retrieve stats for "log_<Date>_<Time>" files in our log folder sorted by newest to oldest
        val logFolderStat = readDir(logDirectoryPath)
            ?.filter { it.name.startsWith(logFilePrefix) }
            ?.sortedByDescending { it.name }

        // return path to correct log file to write to
        return when (logFolderStat) {
            null -> {
                mkdir(logDirectoryPath, true)
                newLogFilePath
            }
            else -> {
                when {
                    logFolderStat.isEmpty() -> {
                        mkdir(logDirectoryPath, true)
                        newLogFilePath
                    }
                    logFolderStat[0].size!! < maxFileSize -> logFolderStat[0].absolutePath.component!!
                    else -> {
                        if (logFolderStat.size >= maxFileCount)
                            unlink(logFolderStat[logFolderStat.size - 1].absolutePath.component!!)
                        newLogFilePath
                    }
                }
            }
        }
    }

    private fun formatMessage(severity: Severity, message: String, tag: String, throwable: Throwable?): String {
        var filename: String? = ""
        var lineNumber: String? = ""
        var functionName: String? = ""
        var stackTrace: String? = ""

         throwable?.let {
             if(calledFromUiLayer(throwable)){
                 /*
                 Example Throwable.stackTraceToString() from iOS UI (e.g. AppInfoCoordinator.swift):
                     kotlin.Throwable
                     ...
                     at 4   Provbit 0x00000001026554a4 $s7Provbit25DefaultAppInfoCoordinatorC04showcD4ViewyyF + 200 (/Users/ncusimano/Documents/DEV/Provbit/bright-provbit/client-ios/Provbit/AppInfo/AppInfoCoordinator.swift:69:56)
                     ...
                  */
                 val throwableStringList = throwable
                     .stackTraceToString()
                     .split(".swift:")

                 filename = throwableStringList[0]
                     .substringAfterLast("/") + ".swift"

                 lineNumber = throwableStringList[1]
                     .substringBefore(":")
             } else {
                 /*
                 Example Throwable.stackTraceToString() from shared layer (e.g. AppInfoProcessor.kt):
                     kotlin.Throwable
                     at 0   ProvbitShared 0x0000000104b36ec0 kfun:kotlin.Throwable#<init>(){} + 76 (/opt/buildAgent/work/6326934d18cfe24e/kotlin/kotlin-native/runtime/src/main/kotlin/kotlin/Throwable.kt:28:21)
                     at 1   ProvbitShared 0x0000000104a14bb4 kfun:ai.bright.provbit.demo.presentation.AppInfoProcessor.handleTestButtonClick#internal + 836 (/Users/ncusimano/Documents/DEV/Provbit/bright-provbit/shared/src/commonMain/kotlin/ai/bright/provbit/demo/presentation/AppInfoProcessor.kt:84:72)
                     ...
                  */
                 val throwableStringList = throwable
                     .stackTraceToString()
                     .substringAfter("Throwable.kt")
                     .split(".kt:")

                 functionName = throwableStringList[0]
                     .substringBeforeLast("#")
                     .substringAfterLast(".") + "() "

                 filename = throwableStringList[0]
                     .substringAfterLast("/") + ".kt"

                 lineNumber = throwableStringList[1]
                     .substringBefore(":")

                 stackTrace = throwable.stackTraceToString()
             }
         }

        val noThrowableMessage = "No Throwable passed to logger - "

        val source = AppConstants.iosSourceName

        val threadId = NSThread.currentThread
            .toString()
            .substringAfter("= ")
            .substringBefore(",")

        val formatter = NSDateFormatter()
        formatter.timeZone = NSTimeZone.localTimeZone
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss.SSSSSSZ"
        val timestamp = formatter.stringFromDate(NSDate.date())

        /*
        Example formatted output string:
            2022-03-01 16:58:15.311146-0600 Latham[654:145324] [Latham] [ℹ️info] 145010 AppSettingsStore.swift#L116 apiEnvironment Using default environment: dev
         */
        return "$timestamp [$tag] [$source] [${emojiPrefix(severity)}] $threadId " +
            "${(if (throwable != null) "$filename#L$lineNumber $functionName" else noThrowableMessage)}$message\n$stackTrace"
    }

    // Determines whether logger was invoked from the shared layer or the UI layer.
    // Unlike Android, the Throwable will have a different format depending on the source
    private fun calledFromUiLayer(throwable: Throwable): Boolean{
        val throwableString = throwable
            .stackTraceToString()
            .substringAfter("Throwable.kt") // throwables from both sources begin with a "Throwable.kt" reference

        // A throwable from the SwiftUI layer will refer to the calling .swift file before any kotlin files
        return throwableString.indexOf(".swift") < throwableString.indexOf(".kt")
    }

    private fun emojiPrefix(severity: Severity): String = when (severity) {
        Severity.Verbose -> "🔍 Verbose"
        Severity.Debug -> "🐛 Debug"
        Severity.Info -> "💡 Info"
        Severity.Warn -> "⚠ Warn"
        Severity.Error -> "❌ Error"
        Severity.Assert -> "🔨 Assert"
    }
}
