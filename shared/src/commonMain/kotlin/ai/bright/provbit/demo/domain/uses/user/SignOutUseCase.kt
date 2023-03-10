package ai.bright.provbit.demo.domain.uses.user

import ai.bright.provbit.architecture.Interactor
import ai.bright.provbit.architecture.Result
import ai.bright.provbit.architecture.ResultInteractor
import ai.bright.provbit.architecture.catchResult
import ai.bright.provbit.demo.domain.ports.UserRepository
import me.tatarka.inject.annotations.Inject

/**
 * A [SignOutUseCase] attempts to sign out the current user.
 * If either no user is signed in, or sign out was successful,
 * this returns true. If an error occurred, and the sign out
 * was unsuccessful, returns false.
 */
interface SignOutUseCase: ResultInteractor<Unit, Unit, Unit> {

    @Inject
    class Default(private val userRepository: UserRepository): SignOutUseCase {
        override suspend fun invoke(input: Unit): Result<Unit, Unit> = catchResult {
            userRepository.logout()
        }
    }

}