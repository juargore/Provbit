package ai.bright.provbit.demo.domain.uses.user

import ai.bright.provbit.architecture.Result
import ai.bright.provbit.architecture.ResultInteractor
import ai.bright.provbit.architecture.catchResult
import ai.bright.provbit.demo.domain.ports.AuthenticationRepository
import me.tatarka.inject.annotations.Inject

interface Auth0SignOutUseCase: ResultInteractor<Unit, Unit, Unit> {


    @Inject
    class Default(
        private val authenticationRepository: AuthenticationRepository
    ): Auth0SignOutUseCase {
        override suspend fun invoke(input: Unit): Result<Unit, Unit> = catchResult {
            authenticationRepository.logout()
        }
    }

}