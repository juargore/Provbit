package ai.bright.provbit.android.screens

import ai.bright.provbit.android.ProcessorViewModel
import ai.bright.provbit.android.screens.components.ButtonComposable
import ai.bright.provbit.demo.SharedComponent
import ai.bright.provbit.demo.presentation.StyleGuideEvent
import ai.bright.provbit.demo.presentation.StyleGuideViewData
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StyleGuideViewModel @Inject constructor(
    sc: SharedComponent
): ProcessorViewModel<StyleGuideViewData, StyleGuideEvent>(
    sc.styleGuideProcessor()
)

@Composable
fun StyleGuideComposable(
    vm: StyleGuideViewModel = hiltViewModel()
) {
    val state by vm.viewData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ButtonComposable(state = state.primaryButton)
        ButtonComposable(state = state.secondaryButton)
        ButtonComposable(state = state.tertiaryButton)
    }
}