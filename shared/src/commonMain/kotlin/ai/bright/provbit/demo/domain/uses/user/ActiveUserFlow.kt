package ai.bright.provbit.demo.domain.uses.user

import ai.bright.provbit.architecture.FlowInteractor
import ai.bright.provbit.architecture.Interactor
import ai.bright.provbit.demo.domain.ports.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

/**
 * Returns a [Flow] of the current [UserState].
 */
interface ActiveUserFlow: FlowInteractor<Unit, UserState> {
    @Inject
    class Default(private val userRepository: UserRepository): ActiveUserFlow {
        override fun invoke(input: Unit): Flow<UserState> = userRepository.currentUser
            .map {
                when (it) {
                    null -> UserState.None
                    else -> UserState.Active(it)
                }
            }
    }
}

/**
 * Wrapper for possible user states.
 * [None] if no user session is active.
 * [Active] along with username for all active sessions.
 *
 * note: this type cannot be nested under [ActiveUserFlow] since it
 * breaks realm compilation... hmm.
 */
sealed class UserState {
    object None: UserState()
    data class Active(val name: String): UserState()
}

