package ai.bright.provbit.util

import ai.bright.core.fs.FileSystem
import co.touchlab.kermit.LogWriter
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

actual fun persistentPlatformLogWriter(
    logDirectoryPath: String
): LogWriter = AndroidPersistentLogWriter(logDirectoryPath)

actual fun zipLogs(inputDirectoryPath: String, inputDirectoryLocation: String, outputZipName: String) {
    FileSystem.unlink("$inputDirectoryLocation/$outputZipName.zip") // remove old archive if exists
    val inputDirectory = File(inputDirectoryPath)
    val outputZipFile = File.createTempFile(outputZipName, ".zip", File(inputDirectoryLocation))

    ZipOutputStream(BufferedOutputStream(FileOutputStream(outputZipFile))).use { zos ->
        inputDirectory
            .walkTopDown()
            .forEach { file ->
                val zipFileName = file.absolutePath.removePrefix(inputDirectory.absolutePath).removePrefix("/")
                val entry = ZipEntry("$zipFileName${(if (file.isDirectory) "/" else "")}")
                zos.putNextEntry(entry)
                if (file.isFile) {
                    file.inputStream().copyTo(zos)
                }
            }
    }
}
