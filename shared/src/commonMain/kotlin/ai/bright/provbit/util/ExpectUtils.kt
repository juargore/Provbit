package ai.bright.provbit.util

import co.touchlab.kermit.LogWriter

/**
 * Will save log statements to a persistent file in the appropriate filesystem.
 * Persistent logs will consist of 5 rolling log files with a max size of 10 MB each.
 */
expect fun persistentPlatformLogWriter(logDirectoryPath: String): LogWriter

/**
 * Compress and zip all persisted logs (to be attached to bug reports or sent to backend).
 */
expect fun zipLogs(inputDirectoryPath: String, inputDirectoryLocation: String, outputZipName: String)
