package ai.bright.provbit.android.dagger

import ai.bright.provbit.demo.adapters.sqlite.DriverFactory
import ai.bright.provbit.demo.SharedComponent
import ai.bright.provbit.demo.create
import android.content.Context
import ai.bright.provbit.demo.domain.ports.AuthenticationRepository
import ai.bright.provbit.demo.domain.ports.ImageRecognitionRepository
import android.content.ContextWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ProcessorModule {

    @Provides
    @Singleton
    fun providesSharedComponent(
        @ApplicationContext appContext: Context,
        authenticationRepository: AuthenticationRepository,
        imageRecognitionRepository: ImageRecognitionRepository
    ): SharedComponent = SharedComponent.create(
        DriverFactory(appContext),
        authenticationRepository,
        imageRecognitionRepository,
        ContextWrapper(appContext)
    )
}
