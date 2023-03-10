package ai.bright.provbit.demo.injection

import ai.bright.provbit.client.conch.apis.HardwareApi
import ai.bright.provbit.client.conch.auth.HttpBearerAuth
import ai.bright.provbit.client.conch.network.NetworkLogger
import ai.bright.provbit.demo.AppScope
import ai.bright.provbit.demo.domain.ports.AuthenticationRepository
import ai.bright.provbit.logging.ConsoleNetworkLogger
import kotlinx.coroutines.runBlocking
import me.tatarka.inject.annotations.Provides

interface NetworkingComponent {

    @Provides
    @AppScope
    fun networkLogger(): NetworkLogger = ConsoleNetworkLogger {
        println("LOG: $it") // Move this to Kermit in the future
    }

    @Provides
    @AppScope
    fun bearerTokenProvider(
        authenticationRepository: AuthenticationRepository
    ): HttpBearerAuth.HttpBearerAuthTokenProvider = object: HttpBearerAuth.HttpBearerAuthTokenProvider {
        override val token: String?
            get() = try {
                runBlocking {
                    // Note: This could potentially be done through some use case,
                    // but I'm not sure if that matches correctly. This is a bit of
                    // a strange cross-cutting concern.
                    authenticationRepository.getCredentials().accessToken
                }
            } catch (e: Throwable) {
                null
            }

        // This value isn't currently used, but the generated network code includes in
        // for future support of auto-retry 403s.
        override var refreshToken: Boolean = false
    }

    @Provides
    @AppScope
    fun hardwareApi(
        networkLogger: NetworkLogger,
        bearerAuthTokenProvider: HttpBearerAuth.HttpBearerAuthTokenProvider
    ): HardwareApi = HardwareApi(
        // Eventually things like this value will be also injected in.
        baseUrl = "https://conch.uw2.dev.core.bai-infra.net",
        log = networkLogger
    ).apply { setBearerTokenProvider(bearerAuthTokenProvider) }
}