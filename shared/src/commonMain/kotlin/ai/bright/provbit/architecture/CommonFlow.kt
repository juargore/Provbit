package ai.bright.provbit.architecture

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * [CommonFlow] and [CommonStateFlow] wrap [Flow] and [StateFlow] in such a way that
 * they can be [watch]ed from outside Kotlin (iOS in this case) in a straightforward manner.
 */
class CommonFlow<T>(private val origin: Flow<T>) : Flow<T> by origin {

    fun watch(block: (T) -> Unit): Closeable {
        val job = Job()

        onEach {
            block(it)
        }.launchIn(CoroutineScope(Dispatchers.Main + job))

        // TODO if writing to a state flow in `Dispatchers.Default` re-assign to main thread for
        // iOS execution

        return object : Closeable {
            override fun close() {
                job.cancel()
            }
        }
    }
}


fun <T> Flow<T>.asCommonFlow(): CommonFlow<T> = CommonFlow(this)
