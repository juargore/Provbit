package ai.bright.provbit.demo.presentation.components

import ai.bright.provbit.architecture.*
import ai.bright.provbit.demo.presentation.components.TextMultiSelectorViewData.*
import kotlinx.coroutines.flow.*

data class TextMultiSelectorViewData(
    val selectedItems: CommonStateFlow<SelectorContentList>,
    val selectableItems: CommonStateFlow<SelectorContentList>,
    val filter: CommonStateFlow<EditTextViewData>,
) {

    data class SelectorContentList(
        val items: List<SelectableRow>
    )

    data class SelectableRow(
        val content: String,
        val onSelected: () -> Unit
    )
}

fun ViewScope.textMultiSelectorComponent(
    contentLoader: Flow<List<String>>
): CommonStateFlow<TextMultiSelectorViewData> {

    // The filter text component
    val filterText = editTextComponent()

    // A state flow of items that will load once [contentLoader] returns.
    val allItems = contentLoader.stateIn(
        this, SharingStarted.Eagerly, emptyList()
    )

    // A state flow of user selected items (as strings). Defaults to empty.
    val userSelectedItems = MutableStateFlow(emptyList<String>())

    // From these things, we can create two additional lists, which are needed
    // for output. First a list of `selected` items mapped to the right output.
    val selectedItems = map(userSelectedItems) { selectedStrings ->
        selectedStrings
            .map { currentRowContent ->
                SelectableRow(
                    currentRowContent
                ) {
                    // Remove item closure
                    if (userSelectedItems.value.contains(currentRowContent)) {
                        userSelectedItems.value -= currentRowContent
                    }
                }
            }.let(::SelectorContentList)
    }.asCommonStateFlow()

    // Second a list of `selectable` items which are filtered by those not
    // current selected and by the filter text content. Note, that this
    // should be updated every time the full list, user selection, or text filter
    // change.
    val selectableItems = combine(filterText, allItems, userSelectedItems) { filter, all, selected ->
        all
            .filter { !selected.contains(it) }
            .filter { it.lowercase().contains(filter.text.lowercase()) }
            .map { currentRowContent ->
                SelectableRow(
                    currentRowContent
                ) {
                    // Add item closure
                    if (allItems.value.contains(currentRowContent) &&
                        !userSelectedItems.value.contains(currentRowContent)) {
                        userSelectedItems.value += currentRowContent
                    }
                }
            }.let(::SelectorContentList)
    }.asCommonStateFlow()

    // Finally, we output the overall state. Since each component, is a flow, we can output
    // this as a singleton.
    return CommonStateFlow.singleton(
        TextMultiSelectorViewData(
            selectedItems,
            selectableItems,
            filterText
        )
    )

}
