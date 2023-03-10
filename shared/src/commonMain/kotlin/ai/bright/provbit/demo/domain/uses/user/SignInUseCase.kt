package ai.bright.provbit.demo.domain.uses.user

import ai.bright.provbit.architecture.Interactor
import ai.bright.provbit.architecture.Result
import ai.bright.provbit.architecture.ResultInteractor
import ai.bright.provbit.architecture.catchResult
import ai.bright.provbit.demo.domain.ports.UserRepository
import me.tatarka.inject.annotations.Inject

/**
 * A [SignInUseCase] attempts to complete authentication using
 * the provided input string. If the user was logged in, returns
 * true.
 */
interface SignInUseCase: ResultInteractor<String, Unit, Unit> {

    @Inject
    class Default(
        private val userRepository: UserRepository
    ): SignInUseCase {
        override suspend fun invoke(input: String): Result<Unit, Unit> = catchResult {
            userRepository.login(input)
        }
    }
}