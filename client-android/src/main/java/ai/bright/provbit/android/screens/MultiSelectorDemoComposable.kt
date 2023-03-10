package ai.bright.provbit.android.screens

import ai.bright.provbit.android.ProcessorViewModel
import ai.bright.provbit.android.screens.components.TextMultiSelectorComposable
import ai.bright.provbit.demo.SharedComponent
import ai.bright.provbit.demo.presentation.MultiSelectorDemoEvent
import ai.bright.provbit.demo.presentation.MultiSelectorDemoViewData
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MultiSelectorDemoViewModel @Inject constructor(
    sc: SharedComponent
): ProcessorViewModel<MultiSelectorDemoViewData, MultiSelectorDemoEvent>(
    sc.multiSelectorDemoProcessor()
)

@Composable
fun MultiSelectorDemoComposable(
    vm: MultiSelectorDemoViewModel = hiltViewModel()
) {
    val state by vm.viewData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = state.title)
        Text(text = state.dataState)
        Text(text = "Selected Items")
        TextMultiSelectorComposable(state.multiSelector)
    }
}

