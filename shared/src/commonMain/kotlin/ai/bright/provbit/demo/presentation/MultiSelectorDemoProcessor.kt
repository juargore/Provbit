package ai.bright.provbit.demo.presentation

import ai.bright.provbit.architecture.*
import ai.bright.provbit.demo.domain.uses.items.ItemQuery
import ai.bright.provbit.demo.domain.uses.items.QueryItemsUseCase
import ai.bright.provbit.demo.entities.Item
import ai.bright.provbit.demo.presentation.components.TextMultiSelectorViewData
import ai.bright.provbit.demo.presentation.components.textMultiSelectorComponent
import kotlinx.coroutines.flow.*
import me.tatarka.inject.annotations.Inject

data class MultiSelectorDemoViewData(
    val title: String,
    val dataState: String,
    val multiSelector: TextMultiSelectorViewData
)

sealed class MultiSelectorDemoEvent

@Inject
class MultiSelectorDemoProcessor(
    private val queryItemsUseCase: QueryItemsUseCase
): BaseProcessor<MultiSelectorDemoViewData, MultiSelectorDemoEvent>() {

    private val title = MutableStateFlow("Multiselect Demo!")
    private val dataState = MutableStateFlow("No Data")
    private val multiSelector = textMultiSelectorComponent(flow {
        dataState.value = "Loading"
        queryItemsUseCase(ItemQuery.All)
            .catch { dataState.value = "Failed to Load" }
            .collect {
                dataState.value = "Loaded"
                emit(it.map(Item::guid))    //this needs to be a unique value -Josh R
            }
    })

    override val viewData: CommonStateFlow<MultiSelectorDemoViewData> =
        combine(title, dataState, multiSelector, ::MultiSelectorDemoViewData)
            .asCommonStateFlow()
}



