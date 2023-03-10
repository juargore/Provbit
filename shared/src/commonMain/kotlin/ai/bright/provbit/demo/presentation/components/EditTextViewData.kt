package ai.bright.provbit.demo.presentation.components

import ai.bright.provbit.architecture.CommonStateFlow
import ai.bright.provbit.architecture.ViewScope
import ai.bright.provbit.architecture.asCommonStateFlow
import ai.bright.provbit.architecture.map
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.flow.*

data class EditTextViewData(
    val text: String,
    val label: StringDesc? = null,
    val hint: StringDesc? = null,
    val valid: Boolean = true,
    val fieldType: FieldType = FieldType.Text,
    val onTextChanged: (String) -> Unit,
) {
    enum class FieldType {
        Text,
        Phone,
        Number,
        Password
    }

}

fun ViewScope.editTextComponent(
    initialText: String = "",
    label: StringDesc? = null,
    hint: StringDesc? = null,
    fieldType: EditTextViewData.FieldType = EditTextViewData.FieldType.Text,
    validationCheck: (String) -> Boolean = { true },
): CommonStateFlow<EditTextViewData> {
    val textContent: MutableStateFlow<String> = MutableStateFlow(initialText)

    return map(
        textContent
    ) {
        EditTextViewData(
            text = it,
            label = label,
            hint = hint,
            valid = validationCheck(it),
            fieldType = fieldType,
            onTextChanged = { textContent.value = it }
        )
    }.asCommonStateFlow()
}