package ai.bright.provbit.android.demo.adapters.auth0

import ai.bright.provbit.android.BuildConfig
import ai.bright.provbit.android.demo.adapters.auth0.ports.Auth0AuthenticationRepository
import android.app.Application
import androidx.compose.animation.ExperimentalAnimationApi
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.provider.WebAuthProvider
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.hilt.migration.DisableInstallInCheck

@ExperimentalAnimationApi
@Subcomponent(modules = [Auth0ImplModule::class])
interface Auth0Component {

    fun repository(): Auth0AuthenticationRepository

    @Subcomponent.Builder interface Builder {
        fun build(): Auth0Component
    }
}

@Module
@DisableInstallInCheck
class Auth0ImplModule {

    @Provides
    fun providesAuth0(): Auth0 {
        return Auth0(
            BuildConfig.AUTH_CLIENT_ID,
            BuildConfig.AUTH_DOMAIN
        )
    }

    @Provides
    fun providesAuth0LoginProvider(
        auth: Auth0
    ): WebAuthProvider.Builder =
        WebAuthProvider.login(auth)
            .withScheme(BuildConfig.AUTH_CALLBACK_SCHEME)
            .withScope("read:hardware openid profile email") // Figure out scope managment

    @Provides
    fun providesAuth0LogoutProvider(
        auth: Auth0
    ): WebAuthProvider.LogoutBuilder =
        WebAuthProvider.logout(auth)
            .withScheme(BuildConfig.AUTH_CALLBACK_SCHEME)

    @Provides
    fun providesAuth0AuthAPIClient(
        auth: Auth0
    ): AuthenticationAPIClient =
        AuthenticationAPIClient(auth)

    @Provides
    fun providesCredentialManager(
        app: Application,
        authClient: AuthenticationAPIClient
    ): SecureCredentialsManager =
        SecureCredentialsManager(app, authClient, SharedPreferencesStorage(app))
}
