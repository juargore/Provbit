package ai.bright.provbit.demo.domain.uses.user

import ai.bright.provbit.architecture.Result
import ai.bright.provbit.architecture.ResultInteractor
import ai.bright.provbit.architecture.catchResult
import ai.bright.provbit.demo.domain.ports.AuthenticationRepository
import me.tatarka.inject.annotations.Inject

// Rename these once we get rid of the original ones.
interface Auth0SignInUseCase: ResultInteractor<Auth0SignInMethod, Unit, AuthenticationRepository.Credentials> {

    @Inject
    class Default(
        private val authenticationRepository: AuthenticationRepository
    ): Auth0SignInUseCase {
        override suspend fun invoke(input: Auth0SignInMethod): Result<Unit, AuthenticationRepository.Credentials> = catchResult {
            authenticationRepository.login(
                connection = when (input) {
                    Auth0SignInMethod.Google -> AuthenticationRepository.AuthenticationConnection.GOOGLE
                    Auth0SignInMethod.Apple -> AuthenticationRepository.AuthenticationConnection.APPLE
                    Auth0SignInMethod.Email -> AuthenticationRepository.AuthenticationConnection.EMAIL
                },
                isSignUp = false
            )
        }

    }
}
enum class Auth0SignInMethod {
    Google, Apple, Email
}

