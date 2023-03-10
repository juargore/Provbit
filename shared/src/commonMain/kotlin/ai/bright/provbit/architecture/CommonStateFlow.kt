package ai.bright.provbit.architecture

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

/**
 * [CommonFlow] and [CommonStateFlow] wrap [Flow] and [StateFlow] in such a way that
 * they can be [watch]ed from outside Kotlin (iOS in this case) in a straightforward manner.
 */
class CommonStateFlow<T>(private val origin: StateFlow<T>): StateFlow<T> by origin {


    companion object {
        fun <T> singleton(single:T): CommonStateFlow<T> = CommonStateFlow(MutableStateFlow(single))
    }

    val currentValue: T
        get() = origin.value

    // This is technically duplicated, but it really messes up the class hierarchy/delegation
    // to make this extend CommonFlow
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
fun <T> StateFlow<T>.asCommonStateFlow(): CommonStateFlow<T> = CommonStateFlow(this)

