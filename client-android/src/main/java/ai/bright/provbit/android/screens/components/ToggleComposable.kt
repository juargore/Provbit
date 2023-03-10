package ai.bright.provbit.android.screens.components

import ai.bright.provbit.architecture.CommonStateFlow
import ai.bright.provbit.demo.presentation.components.ToggleViewData
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ToggleComposable(
    component: CommonStateFlow<ToggleViewData>
) {
    val state by component.collectAsState()
    Switch(
        checked = state.state,
        onCheckedChange = { state.set(it) }
    )
}
