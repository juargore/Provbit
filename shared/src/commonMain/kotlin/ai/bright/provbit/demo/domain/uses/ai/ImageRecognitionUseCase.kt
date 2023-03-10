package ai.bright.provbit.demo.domain.uses.ai

import ai.bright.provbit.architecture.Result
import ai.bright.provbit.architecture.ResultInteractor
import ai.bright.provbit.architecture.catchResult
import ai.bright.provbit.demo.domain.ports.ImageRecognitionRepository
import ai.bright.provbit.demo.entities.ImageAnalysis
import me.tatarka.inject.annotations.Inject

interface ImageRecognitionUseCase: ResultInteractor<ByteArray, Unit, List<ImageAnalysis>> {

    @Inject
    class Default(
        private val ImageRecognitionRepository: ImageRecognitionRepository
    ): ImageRecognitionUseCase {
        override suspend fun invoke(input: ByteArray): Result<Unit, List<ImageAnalysis>> = catchResult {
            ImageRecognitionRepository.analyzeImage(input)
        }
    }

}