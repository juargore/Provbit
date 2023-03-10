package ai.bright.provbit.android.screens

import ai.bright.provbit.android.ProcessorViewModel
import ai.bright.provbit.android.screens.components.ButtonComposable
import ai.bright.provbit.demo.SharedComponent
import ai.bright.provbit.demo.presentation.ItemListEvent
import ai.bright.provbit.demo.presentation.ItemListViewData
import ai.bright.provbit.demo.presentation.ItemViewData
import ai.bright.provbit.demo.presentation.components.ButtonViewData
import ai.bright.provbit.theme.ProvbitButtonType
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.icerock.moko.resources.desc.desc
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel @Inject constructor(
    sc: SharedComponent
) : ProcessorViewModel<ItemListViewData, ItemListEvent>(
    sc.itemListProcessor()
)

@Composable
fun ItemListComposable(
    vm: ItemListViewModel = hiltViewModel(),
    handle: (ItemListEvent) -> Unit
) {
    val state by vm.viewData.collectAsState()
    val itemList by state.itemList.collectAsState()

    vm.eventHandler = handle
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ButtonComposable(
            state = ButtonViewData(
                "Add".desc(),
                ProvbitButtonType.Primary,
                state.onAdd
            )
        )
        TextField(
            value = state.searchText.currentValue.text,
            onValueChange = state.searchText.currentValue.onTextChanged
        )
        LazyColumn {
            items(
                items = itemList.items,
                key = ItemViewData::getId
            ) { data ->
                Row(
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .background(Color(0xFF93DAE4))
                        .fillMaxWidth()
                        .clickable(onClick = data.onClick)
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = data.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = data.desc)
                    }
                }
            }
        }
    }
}
