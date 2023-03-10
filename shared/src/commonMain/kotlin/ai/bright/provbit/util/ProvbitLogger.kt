package ai.bright.provbit.util

import ai.bright.core.fs.FileSystem.cachesDirectory
import ai.bright.provbit.AppConstants
import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter

private val logDirectoryLocation = "${cachesDirectory.absolutePath!!.component}"
private const val logDirectoryName = "Provbit_Logs"
private val logDirectoryPath = "${logDirectoryLocation}/$logDirectoryName"

/**
 * ProvbitLogger inherits from the Kermit Logger and allows for logging to multiple destinations with persistence.
 * Read more: https://github.com/touchlab/Kermit
 *
 * Log messages using the following format: <ProvbitLogger>.<severity-level>(message: String, throwable: Throwable?)
 * The Throwable (optional for Android and shared, required for iOS) can be accepted from either UI layer as well as the
 * shared layer. It's sole purpose is to provide persistent logs being written
 * (using the ApplePersistentLogger.kt/AndroidPersistentLogger.kt) with the filename, function name, and linenumber of
 * the calling code.
 *
 * Logging severity-levels to call (Throwables expected for error and assert calls):
 * v = verbose
 * d = debug
 * i = info
 * w = warn
 * e = error
 * a = assert
 *
 * The Kermit platformLogWriter() uses LogCat and NSLog to present logs in the Android and iOS terminals respectively.
 * The custom persistentPlatformLogWriter() writes logs to persisted files stored on the appropriate filesystems.
 * Read more: https://github.com/touchlab/Kermit/blob/main/docs/LOG_WRITER.md
 *
 * @param tag string to label generated log statements (default: project display name)
 * @param persistLogs boolean to write log statements to a persistent file in addition to the terminal (default: false)
 */
open class ProvbitLogger(tag: String? = AppConstants.projectDisplayName, persistLogs: Boolean? = false) : Logger(
    config = StaticConfig(
        logWriterList = if (persistLogs!!)
            listOf(
                platformLogWriter(),
                persistentPlatformLogWriter(logDirectoryPath)
            ) else
            listOf(platformLogWriter())
    ),
    tag = tag!!
) {
    /**
     * Export all persisted logs, compressed and zipped (to be attached to bug reports or sent to backend).
     */
    fun exportPersistedLogs() {
        zipLogs(
            inputDirectoryPath = logDirectoryPath,
            inputDirectoryLocation = logDirectoryLocation,
            outputZipName = logDirectoryName
        )
        sendLogs()
    }

    /**
     * Logs app and device details (to be called on app startup).
     */
    fun logAppAndDeviceDetails() {
        //TODO: Use "app-info.sh" script to retrieve environment information on startup (https://github.com/BrightDotAi/latham-client-ios/blob/main/client-ios/Scripts/app-info.sh)

        /*
        Example from Latham iOS
            AppInfo:
                Version: 1.0.0
                Build: 1
                buildDate: Tue Mar  1 14:16:03 CST 2022
                infoPlistCreationDate: March 1, 2022 at 12:18:24 PM CST
                installDate: February 25, 2022 at 10:36:13 AM CST
                appVersion: 1.0.0+1
                currentUsername: not available
                physicalMemory: 3874.21875
                megabytesUsed: 119.21875
                systemUptime: 1 Days, 20 Hours, 13 minutes, 39 seconds
                gitSha: c840a70fc7ee194f95c481b929a039ce0fbd1d24
                branchName: bug/LP-1324
                lastTag: build/2022022502-10-gc840a70f
                bundleDisplayName: -
                bundleExecutable: Latham
                modelName: iPad13,2
                hardwareString: iPad13,2
                projectName: Latham
                builderName: Mark Norgren
                builderEmail: mark@marknorgren.com
                deviceModel: iPad
                systemName: iPadOS
                bundleIdentifier: ai.bright.latham.debug
                deviceName: Bright’s iPad
         */
    }

    private fun sendLogs() {
        //TODO: Send zipped logs to backend
    }
}
