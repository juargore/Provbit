package ai.bright.provbit.android.screens

import ai.bright.provbit.android.ProcessorViewModel
import ai.bright.provbit.android.screens.components.EditTextComposable
import ai.bright.provbit.android.screens.components.SelectorComposable
import ai.bright.provbit.android.screens.components.ToggleComposable
import ai.bright.provbit.demo.SharedComponent
import ai.bright.provbit.demo.presentation.FormEvent
import ai.bright.provbit.demo.presentation.FormViewData
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    sc: SharedComponent
): ProcessorViewModel<FormViewData, FormEvent> (
    sc.formProcessor()
)

@Composable
fun FormComposable(
    vm: FormViewModel = hiltViewModel(),
    handle: (FormEvent) -> Unit
) {
    val state by vm.viewData.collectAsState()
    vm.eventHandler = handle
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ){
        EditTextComposable(state.emailAddress)
        EditTextComposable(state.firstName)
        EditTextComposable(state.lastName)
        ToggleComposable(state.isUsCitizen)
        EditTextComposable(state.phoneNumber)
        SelectorComposable(state.favoriteColor)
        Button(onClick = state.formSubmitButtonClick) {
            Text("Submit")
        }
    }
}