package ai.bright.provbit.types

data class SearchBarViewData(
    val placeholder: String,
    val text: String
)

sealed class SearchBarAction {
    data class SearchTextChanged(val text: String) : SearchBarAction()
}