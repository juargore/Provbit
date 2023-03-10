package ai.bright.provbit.android.screens

import ai.bright.provbit.android.ProcessorViewModel
import ai.bright.provbit.demo.SharedComponent
import ai.bright.provbit.demo.presentation.LoginEvent
import ai.bright.provbit.demo.presentation.SignInEvent
import ai.bright.provbit.demo.presentation.SignInViewData
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
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
class SignInViewModel @Inject constructor(
    sc: SharedComponent
): ProcessorViewModel<SignInViewData, SignInEvent> (
    sc.signInProcessor()
)

@Composable
fun SignInComposable(
    vm: SignInViewModel = hiltViewModel(),
    handle: (SignInEvent) -> Unit
) {
    val state by vm.viewData.collectAsState()
    vm.eventHandler = handle
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ){
        when (state.loginState) {
            is SignInViewData.LoginState.Auth -> Text("Authenticated")
            SignInViewData.LoginState.Unauth -> Text("Not authenticated")
        }
        Text(state.hardwareSerial)
        Button(onClick = state.login) {
            Text("Login")
        }
        Button(onClick = state.logout) {
            Text("Logout")
        }
        Button(onClick = state.queryForHardware) {
            Text("Query For Hardware")
        }
        Button(onClick = state.dismissAndContinue) {
            Text("Dismiss Screen")
        }
    }
}