package ai.bright.provbit.demo.domain.ports

import ai.bright.provbit.demo.entities.Timestamp

interface AuthenticationRepository {
    suspend fun isAuthenticated(): Boolean
    suspend fun login(connection: AuthenticationConnection, isSignUp: Boolean): Credentials
    suspend fun logout()
    suspend fun getCredentials(): Credentials

    data class Credentials(
        val idToken: String,
        val accessToken: String,
        val expiration: Timestamp
    )

    enum class AuthenticationConnection(val desc: String) {
        GOOGLE("google-oauth2"),
        EMAIL("Username-Password-Authentication"),
        APPLE("apple"),
        NONE("(none)"),
    }
}
