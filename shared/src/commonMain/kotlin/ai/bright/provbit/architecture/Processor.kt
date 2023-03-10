package ai.bright.provbit.architecture

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A [Processor] is a KMM class that handles logic for each screen in a application. Processors are
 * the "bridges" between "KMM-Land" and "Native Platform Land" that allow native platforms to only
 * need to implement display logic and handle navigation events as prescribed by the Processor.
 *
 * A Processor serves three functions:
 *
 * 1) Tell native platforms what [ViewData] should be on screen.
 * 2) Tell native platforms to handle on off [Event]s. Most often an [Event] is a navigation event.
 *
 * Processors differ from Components, in that they are attached to views, and can handle events.
 */
abstract class Processor<ViewData, Event> : ViewScope, Closeable {

    /**
     * Flow of [ViewData] to be displayed by native platforms.
     *
     * Each emission represents a change in the total state of a screen.
     */
    abstract val viewData: CommonStateFlow<ViewData>

    /**
     * Flow of [Event]s to be consumed by native platforms.
     */
    abstract val events: CommonFlow<Event>

}

class TestCommonState<ViewData> (
    viewData: ViewData
) {
    val viewData: CommonStateFlow<ViewData> =
        MutableStateFlow(viewData).asCommonStateFlow()
}