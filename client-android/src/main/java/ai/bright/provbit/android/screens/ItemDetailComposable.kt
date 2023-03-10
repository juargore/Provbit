package ai.bright.provbit.android.screens

import ai.bright.provbit.android.ProcessorViewModel
import ai.bright.provbit.android.screens.components.ButtonComposable
import ai.bright.provbit.android.screens.components.EditTextComposable
import ai.bright.provbit.demo.SharedComponent
import ai.bright.provbit.demo.presentation.ItemDetailEvent
import ai.bright.provbit.demo.presentation.ItemDetailViewData
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    sc: SharedComponent
): ProcessorViewModel<ItemDetailViewData, ItemDetailEvent>(
    sc.itemDetailProcessor(savedStateHandle["itemGuid"]!!)
)

@Composable
fun ItemDetailComposable(
    vm: ItemDetailViewModel = hiltViewModel(),
    handle: (ItemDetailEvent) -> Unit
) {
    val state by vm.viewData.collectAsState()
    vm.eventHandler = handle
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp),
    ) {
        when (val s = state) {
            ItemDetailViewData.Error -> Text(text = "Loading Failure")
            ItemDetailViewData.Loading -> Text(text = "Unloaded")
            is ItemDetailViewData.ShowItem -> {
                EditTextComposable(s.name)
                EditTextComposable(s.desc)
                ButtonComposable(state = s.save)
            }
        }
    }
}