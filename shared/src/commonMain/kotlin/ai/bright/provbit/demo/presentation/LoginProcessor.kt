package ai.bright.provbit.demo.presentation

import ai.bright.provbit.MR
import ai.bright.provbit.architecture.BaseProcessor
import ai.bright.provbit.architecture.CommonStateFlow
import ai.bright.provbit.demo.domain.uses.user.SignInUseCase
import ai.bright.provbit.demo.presentation.components.ButtonViewData
import ai.bright.provbit.demo.presentation.components.EditTextViewData
import ai.bright.provbit.demo.presentation.components.editTextComponent
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

sealed class LoginEvent {
    object ToForm : LoginEvent()
}

data class LoginViewData(
    val email: CommonStateFlow<EditTextViewData>,
    val password: CommonStateFlow<EditTextViewData>,
    val registerButton: ButtonViewData,
    val loginButton: ButtonViewData,
)

@Inject
class LoginProcessor(
    private val signInUseCase: SignInUseCase,
) : BaseProcessor<LoginViewData, LoginEvent>() {

    private val emailField = editTextComponent(label = MR.strings.email_label.desc())
    private val passwordField = editTextComponent(
        label = MR.strings.password_label.desc(),
        fieldType = EditTextViewData.FieldType.Password
    )

    private val registerButton = ButtonViewData(
        MR.strings.register_user_button_text.desc()
    ) { sendEvent(LoginEvent.ToForm) }

    private val loginButton = ButtonViewData(
        MR.strings.login_button_text.desc()
    ) { launch { signInUseCase(emailField.value.text) } }

    override val viewData = CommonStateFlow.singleton(
        LoginViewData(
            emailField,
            passwordField,
            registerButton,
            loginButton
        )
    )
}
