package ai.bright.provbit.android.demo.adapters.auth0

import ai.bright.provbit.android.demo.adapters.auth0.ports.ImgRecognitionRepository
import androidx.compose.animation.ExperimentalAnimationApi
import dagger.Module
import dagger.Subcomponent
import dagger.hilt.migration.DisableInstallInCheck

@ExperimentalAnimationApi
@Subcomponent(modules = [ImageImplModule::class])
interface ImgComponent {

    fun repository(): ImgRecognitionRepository

    @Subcomponent.Builder interface Builder {
        fun build(): ImgComponent
    }
}

@Module
@DisableInstallInCheck
class ImageImplModule