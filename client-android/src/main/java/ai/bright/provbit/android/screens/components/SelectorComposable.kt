package ai.bright.provbit.android.screens.components

import ai.bright.provbit.architecture.CommonStateFlow
import ai.bright.provbit.demo.presentation.components.SelectorViewData
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SelectorComposable(
    component: CommonStateFlow<SelectorViewData>
) {
    val state by component.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = state.selectedElement,
        readOnly = true,
        onValueChange = {},
        modifier = Modifier.padding(16.dp),
        trailingIcon = {
            Icon(
                Icons.Filled.ArrowDropDown, "Arrow Pointing Down",
                // This isn't an ideal way to make this clickable
                Modifier.clickable { expanded = !expanded }) },
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        state.allElements.forEachIndexed { idx, element ->
            DropdownMenuItem(
                onClick = {
                    state.select(idx)
                    expanded = false
                }
            ) {
                Text(text = element)
            }
        }
    }
}
