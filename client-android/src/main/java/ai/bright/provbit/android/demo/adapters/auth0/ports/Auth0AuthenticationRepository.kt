package ai.bright.provbit.android.demo.adapters.auth0.ports

import ai.bright.provbit.android.HasActivity
import ai.bright.provbit.android.demo.adapters.auth0.converters.Auth0CredentialsConverter
import ai.bright.provbit.demo.domain.ports.AuthenticationRepository
import ai.bright.provbit.demo.domain.ports.AuthenticationRepository.AuthenticationConnection
import ai.bright.provbit.demo.domain.ports.AuthenticationRepository.Credentials
import android.app.Application
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.auth0.android.result.Credentials as Auth0Credentials

class Auth0AuthenticationRepository @Inject constructor(
    private val app: Application,
    private val auth0LoginBuilder: WebAuthProvider.Builder,
    private val auth0LogoutBuilder: WebAuthProvider.LogoutBuilder,
    private val secureCredentialsManager: SecureCredentialsManager,
) : AuthenticationRepository {

    private val auth0CredentialsConverter = Auth0CredentialsConverter()

    override suspend fun isAuthenticated() =
        secureCredentialsManager.hasValidCredentials()

    override suspend fun login(connection: AuthenticationConnection, isSignUp: Boolean): Credentials =
        suspendCoroutine { cont ->
            val context = (app as? HasActivity)?.currentActivity
//            val auth0LoginBuilder = auth0LoginBuilder.withConnection(connection.name)
            val parameters = if (isSignUp) {
                mapOf("action" to "signup")
            } else {
                mapOf("action" to "sign")
            }
            auth0LoginBuilder.withParameters(parameters)
            context?.also {
                auth0LoginBuilder.start(
                    context = it,
                    callback = object : Callback<Auth0Credentials, AuthenticationException> {
                        override fun onSuccess(result: Auth0Credentials) {
                            try {
                                secureCredentialsManager.saveCredentials(result)
                            } catch (exception: CredentialsManagerException) {
                                try {
                                    secureCredentialsManager.clearCredentials()
                                    secureCredentialsManager.saveCredentials(result)
                                } catch (exception: CredentialsManagerException) {
                                    // Add a log or metric here
                                    throw exception
                                }
                            }
                            cont.resume(
                                auth0CredentialsConverter.convertInbound(result)
                            )
                        }

                        override fun onFailure(error: AuthenticationException) {
                            // Add a log or metric here
                            throw error
                        }
                    }
                )
            }
        }

    override suspend fun logout() = suspendCoroutine<Unit> { cont ->
        println("Trying to sign out")
        val context = (app as? HasActivity)?.currentActivity
        context?.also {
            auth0LogoutBuilder.start(
                context = it,
                callback = object : Callback<Void?, AuthenticationException> {
                    override fun onSuccess(result: Void?) {
                        secureCredentialsManager.clearCredentials()
                        println("Signed OUT!")
                        cont.resume(Unit)
                    }

                    override fun onFailure(error: AuthenticationException) {
                        // Add a log or metric here
                        throw error
                    }
                }
            )
        }
    }

    override suspend fun getCredentials(): Credentials = suspendCoroutine { cont ->
        secureCredentialsManager.getCredentials(object :
                Callback<Auth0Credentials, CredentialsManagerException> {
                override fun onSuccess(result: Auth0Credentials) {
                    cont.resume(auth0CredentialsConverter.convertInbound(result))
                }

                override fun onFailure(error: CredentialsManagerException) {
                    // Add a log or metric here
                    throw error
                }
            })
    }
}
