package ai.bright.provbit.android

import ai.bright.provbit.architecture.Processor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class ProcessorViewModel<ViewData, Event>(
    val processor: Processor<ViewData, Event>
) : ViewModel() {

    init {
        viewModelScope.launch { processor.events.collect { eventHandler(it) } }
    }

    /**
     * We call a cancellation on the processor here to tie it's lifespan to the view model.
     * It would be nice to modify this so the processor gets the viewModelScope passed in
     * as a parent, so this occurs automatically.
     */
    override fun onCleared() {
        super.onCleared()
        processor.cancel()
    }

    val viewData: StateFlow<ViewData> = processor.viewData

    var eventHandler: (Event) -> Unit = { }
}