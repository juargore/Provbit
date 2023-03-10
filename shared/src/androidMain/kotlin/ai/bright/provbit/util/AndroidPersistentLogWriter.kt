package ai.bright.provbit.util

import ai.bright.core.fs.FileSystem.appendFile
import ai.bright.core.fs.FileSystem.mkdir
import ai.bright.core.fs.FileSystem.readDir
import ai.bright.core.fs.FileSystem.unlink
import ai.bright.provbit.AppConstants.androidSourceName
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

class AndroidPersistentLogWriter(private val logDirectoryPath: String) : LogWriter() {
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
        val timestamp = SimpleDateFormat("yyyy-MM-dd_HH.mm.ss", Locale.US).format(Timestamp(System.currentTimeMillis()))

        val newLogFilePath = "$logDirectoryPath/${logFilePrefix}_$timestamp"

        // retrieve stats for any "log_<Date>_<Time>" files in our log folder sorted by newest to oldest
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
        /*
        Example Throwable.stackTraceToString() from either shared layer (e.g. AppInfoProcessor.kt) or Android UI (AppInfoComposable.kt):
            java.lang.Throwable
            at ai.bright.provbit.demo.presentation.AppInfoProcessor.handleTestButtonClick(AppInfoProcessor.kt:76)
            ...
       */
        val throwableString = throwable?.stackTraceToString()

        val functionName = throwableString
            ?.substringBefore("(")
            ?.substringAfterLast(".") + "()"

        val filename = throwableString
            ?.substringAfter("(")
            ?.substringBefore(":")

        val lineNumber = throwableString
            ?.substringAfter(":")
            ?.substringBefore(")")

        val noThrowableMessage = "No Throwable passed to logger - "

        val source = androidSourceName

        val threadId = Thread.currentThread().id

        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSZ", Locale.US).format(Timestamp(System.currentTimeMillis()))

        /*
        Example formatted output string:
            2022-03-01 16:58:15.311146-0600 Latham[654:145324] [Latham] [ℹ️info] 145010 AppSettingsStore.swift#L116 apiEnvironment Using default environment: dev
         */
        return "$timestamp [$tag] [$source] [${emojiPrefix(severity)}] $threadId " +
            "${(if (throwable != null) "$filename#L$lineNumber $functionName" else noThrowableMessage)} $message\n$throwableString"
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
