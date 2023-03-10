package ai.bright.provbit.demo.presentation.components

import ai.bright.provbit.architecture.*
import kotlinx.coroutines.flow.MutableStateFlow

data class SelectorViewData(
    val allElements: List<String>,
    val selectionIdx: Int,
    val select: (Int) -> Unit
) {
    val selectedElement: String = allElements[selectionIdx]
}

fun ViewScope.selectorComponent(
    items: List<String>
): CommonStateFlow<SelectorViewData> {

    val selected = MutableStateFlow(0)

    return map(
        selected
    ) { idx ->
        SelectorViewData(
            items,
            idx
        ) {
            if (it in items.indices) {
                selected.value = it
            }
        }
    }.asCommonStateFlow()
}