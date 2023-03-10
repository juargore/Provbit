package ai.bright.provbit.demo.domain.ports

import ai.bright.provbit.demo.entities.ImageAnalysis

interface ImageRecognitionRepository {

    suspend fun analyzeImage(image: ByteArray) : List<ImageAnalysis>
}