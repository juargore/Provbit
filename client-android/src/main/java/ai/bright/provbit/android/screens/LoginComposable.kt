package ai.bright.provbit.android.screens

import ai.bright.provbit.android.ProcessorViewModel
import ai.bright.provbit.android.screens.components.ButtonComposable
import ai.bright.provbit.android.screens.components.EditTextComposable
import ai.bright.provbit.demo.SharedComponent
import ai.bright.provbit.demo.presentation.LoginEvent
import ai.bright.provbit.demo.presentation.LoginViewData
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    sc: SharedComponent
): ProcessorViewModel<LoginViewData, LoginEvent>(
    sc.loginProcessor()
)

@Preview("Login")
@Composable
fun LoginComposable(
    vm: LoginViewModel = hiltViewModel(),
    handle: (LoginEvent) -> Unit
) {
    val state by vm.viewData.collectAsState()
    vm.eventHandler = handle

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        EditTextComposable(state.email)
        EditTextComposable(state.password)
        ButtonComposable(state.registerButton)
        ButtonComposable(state.loginButton)
    }
}