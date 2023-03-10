package ai.bright.provbit.demo.adapters.local.ports

import ai.bright.provbit.demo.AppScope
import ai.bright.provbit.demo.domain.ports.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class LocalUserRepository: UserRepository {

    override suspend fun login(name: String) {
        _currentUser.value = name
    }

    override suspend fun logout() {
        _currentUser.value = null
    }

    private val _currentUser = MutableStateFlow<String?>(null)
    override val currentUser: StateFlow<String?> = _currentUser
}