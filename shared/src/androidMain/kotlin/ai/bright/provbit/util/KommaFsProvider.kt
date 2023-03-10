package ai.bright.provbit.util

import ai.bright.core.fs.FileSystem
import ai.bright.core.fs.utils.ModuleProvider
import android.content.Context

/**
 * Provider for Komma's File System. Automatically initializes the library with the context. (This is not needed
 * for iOS to also make use of the library)
 */
class KommaFsProvider: ModuleProvider("ai.bright.provbit", "kommaFsProvider") {
    override fun bootstrap(context: Context?) {
        FileSystem.initialize(context)
    }
}
