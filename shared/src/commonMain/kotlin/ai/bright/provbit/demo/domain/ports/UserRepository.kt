package ai.bright.provbit.demo.domain.ports

import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    suspend fun login(name: String)
    suspend fun logout()
    val currentUser: StateFlow<String?>
}