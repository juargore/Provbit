package ai.bright.provbit.android.screens.components

import ai.bright.provbit.demo.presentation.components.TextMultiSelectorViewData
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextMultiSelectorComposable(
    state: TextMultiSelectorViewData
) {
    val selectedItems by state.selectedItems.collectAsState()
    val selectableItems by state.selectableItems.collectAsState()
    LazyColumn {
        items(selectedItems.items) { selectedItem ->
            Row(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .background(Color(0xFF93DAE4))
                    .fillMaxWidth()
                    .clickable(onClick = selectedItem.onSelected)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = selectedItem.content, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
        }
    }
    Text(text = "Filter List")
    EditTextComposable(state.filter)
    Text(text = "Selectable Items")
    LazyColumn {
        items(selectableItems.items) { selectedItem ->
            Row(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .background(Color(0xFF93DAE4))
                    .fillMaxWidth()
                    .clickable(onClick = selectedItem.onSelected)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = selectedItem.content, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
        }
    }
}

