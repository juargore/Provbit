package ai.bright.provbit.android.dagger

import ai.bright.provbit.android.demo.adapters.auth0.ImgComponent
import ai.bright.provbit.demo.domain.ports.ImageRecognitionRepository
import androidx.compose.animation.ExperimentalAnimationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@OptIn(ExperimentalAnimationApi::class)
@InstallIn(SingletonComponent::class)
@Module(subcomponents = [ImgComponent::class])
object ImgModule {

    @Provides
    @Singleton
    fun providesImageRecognitionRepository(
        builder: ImgComponent.Builder
    ): ImageRecognitionRepository =
        builder.build().repository()
}
