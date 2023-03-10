package ai.bright.provbit.architecture

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Base processor from which other processors should inherit.
 *
 * Handles attach/detach functionality and leaves
 */
abstract class BaseProcessor<ViewData, Event> : Processor<ViewData, Event>() {

    private val handleChannel = Channel<Event>(capacity = UNLIMITED)
    override val events: CommonFlow<Event> =
        handleChannel.consumeAsFlow().asCommonFlow()

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main

    /**
     * Handle the given [event]. This will send it to the native platform that called
     * [attach].
     */
    fun sendEvent(event: Event) {
        handleChannel.trySend(event)
    }

    /**
     * For some reason, the `cancel` method is not available directly in iOS.
     * We name it [close] here to mimic the interface found in the [CommonFlow].
     */
    override fun close() { cancel() }
}