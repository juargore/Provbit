package ai.bright.provbit.demo.presentation.components

import ai.bright.provbit.demo.presentation.ImageAnalysisProcessor
import dev.icerock.moko.resources.desc.StringDesc

/**
 * View data for the [ImageAnalysisProcessor].
 */
sealed class ImageSelectorViewData {

    /**
     * Defined to initialize the state.
     */
    object None: ImageSelectorViewData()

    /**
     * Manage the success and error results when picking image from gallery.
     * Return [ByteArray] since it is a compatible format between platforms.
     */
    data class PickImage(
        val onPickImageSuccess: (ByteArray) -> Unit,
        val onPickImageError: (String) -> Unit,
    ) : ImageSelectorViewData()

    /**
     * Manage the success and error results when taking photo from camera.
     * Return [ByteArray] since it is a compatible format between platforms.
     * Can't use the same PickImage class, because it was 'overlapping' results.
     */
    data class TakePhoto(
        val onPhotoSuccess: (ByteArray) -> Unit,
        val onPhotoError: (String) -> Unit,
    ) : ImageSelectorViewData()

    /**
     * Get the result as [ByteArray] to set in the image component of each platform.
     */
    data class ResultImage(val resultBitmapAsArray: ByteArray) : ImageSelectorViewData()
    data class ResultPhoto(val resultBitmapAsArray: ByteArray) : ImageSelectorViewData()

    /**
     * Catch any error with generic message and append the real cause.
     */
    data class Error(val errorText: StringDesc) : ImageSelectorViewData()
}

/**
 * Manage the different results only in the Processor side.
 */
sealed class ImageSelectorResult {
    object Idle : ImageSelectorResult()
    object PreparedToPickImage : ImageSelectorResult()
    object PreparedToTakePhoto : ImageSelectorResult()
    data class ResultImage(val image: ByteArray) : ImageSelectorResult()
    data class ResultPhoto(val image: ByteArray) : ImageSelectorResult()
    data class Error(val message: StringDesc) : ImageSelectorResult()
}