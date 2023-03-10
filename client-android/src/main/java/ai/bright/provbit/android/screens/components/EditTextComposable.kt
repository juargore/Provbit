package ai.bright.provbit.android.screens.components

import ai.bright.provbit.android.extensions.localized
import ai.bright.provbit.architecture.CommonStateFlow
import ai.bright.provbit.demo.presentation.components.EditTextViewData
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun EditTextComposable(
    flow: CommonStateFlow<EditTextViewData>
) {
    val state by flow.collectAsState()
    OutlinedTextField(
        value = state.text,
        onValueChange = state.onTextChanged,
        label = state.label?.let { { Text(text = it.localized()) } },
        isError = !state.valid,
        modifier = Modifier.padding(16.dp),
        visualTransformation = when (state.fieldType) {
            EditTextViewData.FieldType.Password -> PasswordVisualTransformation()
            else -> VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = when (state.fieldType) {
                EditTextViewData.FieldType.Text -> KeyboardType.Text
                EditTextViewData.FieldType.Phone -> KeyboardType.Phone
                EditTextViewData.FieldType.Number -> KeyboardType.Number
                EditTextViewData.FieldType.Password -> KeyboardType.Password
            }
        )
    )
}
