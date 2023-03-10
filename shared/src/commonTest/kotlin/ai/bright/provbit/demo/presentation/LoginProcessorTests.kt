@file:Suppress("IllegalIdentifier")

package ai.bright.provbit.demo.presentation

import ai.bright.provbit.BaseTest
import ai.bright.provbit.MR
import ai.bright.provbit.architecture.asOk
import ai.bright.provbit.demo.domain.uses.user.SignInUseCase
import ai.bright.provbit.testExhaustive
import app.cash.turbine.test
import dev.icerock.moko.resources.desc.desc
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class LoginProcessorTests : BaseTest() {

    @Test
    fun `on register tap should navigate to form`() = runTest {
        val signInUseCase: SignInUseCase = mockk()
        val processor = LoginProcessor(signInUseCase)
        processor.viewData.testExhaustive {
            val initialViewData = awaitItem()
            initialViewData.registerButton.onClick()
        }
        processor.events.test {
            assertEquals(LoginEvent.ToForm, awaitItem())
        }
    }

    @Test
    fun `on login tap should call sign in use case with correct input`() = runTest {
        val email = "email"
        val signInUseCase: SignInUseCase = mockk {
            coEvery { this@mockk.invoke(email) }.returns(Unit.asOk())
        }
        val processor = LoginProcessor(signInUseCase)
        processor.viewData.testExhaustive {
            val loginData = awaitItem()
            loginData.email.testExhaustive {
                awaitItem().onTextChanged(email)
                awaitItem() // We expect another email text field after we run text changed
            }
            loginData.loginButton.onClick()
        }
        processor.events.test {}
        coVerify { signInUseCase.invoke(email) }
    }

    @Test
    fun `initial view data should be correct`() = runTest {
        val signInUseCase: SignInUseCase = mockk()
        val processor = LoginProcessor(signInUseCase)
        processor.viewData.testExhaustive {
            with(awaitItem()) {
                assertEquals(MR.strings.register_user_button_text.desc(), registerButton.content)
                assertEquals(MR.strings.login_button_text.desc(), loginButton.content)
                assertEquals(MR.strings.email_label.desc(), email.currentValue.label)
                assertEquals(MR.strings.password_label.desc(), password.currentValue.label)
            }

        }
    }
}
