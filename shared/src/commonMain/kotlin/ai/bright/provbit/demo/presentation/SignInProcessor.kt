package ai.bright.provbit.demo.presentation

import ai.bright.provbit.architecture.*
import ai.bright.provbit.demo.domain.uses.hardware.GetHardwareUseCase
import ai.bright.provbit.demo.domain.uses.user.Auth0SignInUseCase
import ai.bright.provbit.demo.domain.uses.user.Auth0SignOutUseCase
import ai.bright.provbit.demo.domain.uses.user.CurrentAuthStateUseCase
import ai.bright.provbit.demo.domain.uses.user.Auth0SignInMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

data class SignInViewData(
    val loginState: LoginState,
    val hardwareSerial: String,
    val login: () -> Unit,
    val logout: () -> Unit,
    val queryForHardware: () -> Unit,
    val dismissAndContinue: () -> Unit
) {
    sealed class LoginState {
        object Unauth: LoginState()
        object Auth: LoginState()
    }
}

sealed class SignInEvent {
    object Dismiss : SignInEvent()
}

@Inject
class SignInProcessor(
    private val currentAuthState: CurrentAuthStateUseCase,
    private val signIn: Auth0SignInUseCase,
    private val signOut: Auth0SignOutUseCase,
    private val getHardware: GetHardwareUseCase,
) : BaseProcessor<SignInViewData, SignInEvent>() {

    private val loginState: MutableStateFlow<SignInViewData.LoginState> =
        MutableStateFlow(SignInViewData.LoginState.Unauth)

    private val hardwareSerial: MutableStateFlow<String> =
        MutableStateFlow("")

    // Check state on init
    init { launch {
        if (currentAuthState()) {
            loginState.value = SignInViewData.LoginState.Auth
        }
    } }


    private fun startLogin() {
        launch {
            signIn.invoke(Auth0SignInMethod.Email)
                .onOk { loginState.value = SignInViewData.LoginState.Auth }
        }
    }

    private fun queryForHardware() {
        launch {
            getHardware.invoke("9bb95eed-8017-4ef3-b96f-7a21b1c1ee2c")
                .onOk {
                    hardwareSerial.value = it.serialNumber
                }
                .onErr {
                    hardwareSerial.value = "Failed to make call."
                }
        }
    }

    override val viewData: CommonStateFlow<SignInViewData> = combine(
        loginState,
        hardwareSerial
    ) { ls, hw ->
        SignInViewData(
            loginState = ls,
            hardwareSerial = hw,
            login = this::startLogin,
            logout = { launch { signOut() } },
            queryForHardware = this::queryForHardware,
            dismissAndContinue = { sendEvent(SignInEvent.Dismiss) },
        )
    }.asCommonStateFlow()
}