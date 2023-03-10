package ai.bright.provbit.demo.domain.uses.user

import ai.bright.provbit.architecture.Interactor
import ai.bright.provbit.demo.domain.ports.AuthenticationRepository
import me.tatarka.inject.annotations.Inject

interface CurrentAuthStateUseCase: Interactor<Unit, Boolean> {

    @Inject
    class Default(
        private val authenticationRepository: AuthenticationRepository
    ): CurrentAuthStateUseCase {
        override suspend fun invoke(input: Unit): Boolean = try {
            authenticationRepository.isAuthenticated()
        } catch (t: Throwable) {
            false
        }
    }
}