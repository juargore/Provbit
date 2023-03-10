package ai.bright.provbit.android.demo.adapters.auth0.ports

import ai.bright.provbit.demo.domain.ports.AuthenticationRepository
import ai.bright.provbit.demo.domain.ports.AuthenticationRepository.AuthenticationConnection
import ai.bright.provbit.demo.domain.ports.AuthenticationRepository.Credentials
import ai.bright.provbit.demo.entities.Timestamp
import javax.inject.Inject

class BypassAuthenticationRepository @Inject constructor() : AuthenticationRepository {

    private val creds = Credentials(
        idToken = "id-token",
        accessToken = "access-token",
        expiration = Timestamp(33192096907)
    )

    override suspend fun isAuthenticated(): Boolean = true

    override suspend fun login(
        connection: AuthenticationConnection,
        isSignUp: Boolean,
    ): Credentials = creds

    override suspend fun logout() { }

    override suspend fun getCredentials(): Credentials = creds
}
