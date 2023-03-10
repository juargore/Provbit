package ai.bright.provbit.util

import ai.bright.core.fs.FileSystem
import co.touchlab.kermit.LogWriter
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSError
import platform.Foundation.NSFileCoordinator
import platform.Foundation.NSFileCoordinatorReadingForUploading
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.URLByAppendingPathComponent

actual fun persistentPlatformLogWriter(
    logDirectoryPath: String
): LogWriter = ApplePersistentLogWriter(logDirectoryPath)

actual fun zipLogs(inputDirectoryPath: String, inputDirectoryLocation: String, outputZipName: String) {
    val fileManager = NSFileManager.defaultManager

    memScoped {
        val error: ObjCObjectVar<NSError?> = alloc()
        NSFileCoordinator().coordinateReadingItemAtURL(
            url = NSURL(fileURLWithPath = inputDirectoryPath),
            options = NSFileCoordinatorReadingForUploading,
            error.ptr
        ) { zipUrl ->
            val cachesDirectoryUrl = fileManager.URLForDirectory(
                NSCachesDirectory,
                NSUserDomainMask,
                appropriateForURL = zipUrl, // zipUrl points to the zip file created by the coordinator
                create = true,
                error = error.ptr
            )?.URLByAppendingPathComponent("$outputZipName.zip")
                ?: throw RuntimeException("could not retrieve cache directory url")

            zipUrl?.let {
                FileSystem.unlink("$inputDirectoryLocation/$outputZipName.zip") // remove old archive if exists
                fileManager.moveItemAtURL(it, cachesDirectoryUrl, error.ptr)
            }
        }
    }
}
