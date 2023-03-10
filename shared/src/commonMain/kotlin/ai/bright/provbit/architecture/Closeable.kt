package ai.bright.provbit.architecture

/**
 * Certain observed things in iOS need to be explicitly closed.
 * This is a common interface for that.
 */
interface Closeable {
    fun close()
}