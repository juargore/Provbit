package ai.bright.provbit.android.dagger

import ai.bright.provbit.android.demo.adapters.auth0.Auth0Component
import ai.bright.provbit.demo.domain.ports.AuthenticationRepository
import androidx.compose.animation.ExperimentalAnimationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@OptIn(ExperimentalAnimationApi::class)
@InstallIn(SingletonComponent::class)
@Module(subcomponents = [Auth0Component::class])
object AuthModule {

    @Provides
    @Singleton
    fun providesAuthenticationRepository(
        builder: Auth0Component.Builder
    ): AuthenticationRepository =
        builder.build().repository()
}
