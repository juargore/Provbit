package ai.bright.provbit.demo.presentation

import ai.bright.provbit.MR
import ai.bright.provbit.architecture.*
import ai.bright.provbit.demo.domain.uses.ai.ImageRecognitionUseCase
import ai.bright.provbit.demo.entities.ImageAnalysis
import ai.bright.provbit.demo.presentation.components.*
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.resources.format
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

data class ImageAnalysisViewData(
    val title: StringDesc,
    val topImageFromAssets: ImageViewData,
    val detectTopButton: ButtonViewData,
    val topImageAnalysisResults: List<ImageAnalysis>,
    val imageSelector: ImageSelectorViewData,
    val pickGalleryButton: ButtonViewData,
    val takePhotoButton: ButtonViewData,
    val detectBottomButton: ButtonViewData,
    val bottomImageAnalysisResults: List<ImageAnalysis>
)

sealed class ImageAnalysisEvent

@Inject
class ImageAnalysisProcessor(
    private val imageRecognitionUseCase: ImageRecognitionUseCase
) : BaseProcessor<ImageAnalysisViewData, ImageAnalysisEvent>() {

    private val mTitle = MutableStateFlow(MR.strings.image_analysis_title.desc())
    private val imageViewData = MutableStateFlow(imageViewComponent())
    private val imageSelectorResult = MutableStateFlow<ImageSelectorResult>(ImageSelectorResult.Idle)
    private val resultsFromTopImageAnalysis: MutableStateFlow<List<ImageAnalysis>> = MutableStateFlow(listOf())
    private val resultsFromBottomImageAnalysis: MutableStateFlow<List<ImageAnalysis>> = MutableStateFlow(listOf())
    private var currentImageFromGalleryOrCamera: ByteArray = byteArrayOf()

    private val pickImageButton = ButtonViewData(
        MR.strings.image_analysis_button_pick_from_gallery.desc()
    ) {
        imageSelectorResult.value = ImageSelectorResult.PreparedToPickImage
        resultsFromBottomImageAnalysis.value = listOf()
    }

    private val takePhotoButton = ButtonViewData(
        MR.strings.image_analysis_button_take_photo.desc()
    ) {
        imageSelectorResult.value = ImageSelectorResult.PreparedToTakePhoto
        resultsFromBottomImageAnalysis.value = listOf()
    }

    private val detectTestImageButton = ButtonViewData(
        MR.strings.image_analysis_button_analyze_image.desc()
    ) {
        launch {
            imageViewData.value.value.imageByteArray?.let { img ->
                resultsFromTopImageAnalysis.value =
                    imageRecognitionUseCase(img).fold({ listOf() }) { it }
            }
        }
    }

    private val detectBottomButton = ButtonViewData(
        MR.strings.image_analysis_button_analyze_image.desc()
    ) {
        launch {
            resultsFromBottomImageAnalysis.value =
                imageRecognitionUseCase(currentImageFromGalleryOrCamera).fold({ listOf() }) { it }
        }
    }

    override val viewData: CommonStateFlow<ImageAnalysisViewData> =
        combine(
            imageViewData,
            imageSelectorResult,
            resultsFromTopImageAnalysis,
            resultsFromBottomImageAnalysis,
        ) { ivData,
            imgSelector,
            resultsPytorchTest,
            resultsPytorchGallery ->

            val image = when (imgSelector) {
                is ImageSelectorResult.Idle -> {
                    ImageSelectorViewData.None
                }
                is ImageSelectorResult.PreparedToPickImage -> {
                    ImageSelectorViewData.PickImage(
                        onPickImageSuccess = {
                            imageSelectorResult.value = ImageSelectorResult.ResultImage(it)
                        },
                        onPickImageError = {
                            imageSelectorResult.value = ImageSelectorResult
                                .Error(MR.strings.image_analysis_error.desc())
                        }
                    )
                }
                is ImageSelectorResult.PreparedToTakePhoto -> {
                    ImageSelectorViewData.TakePhoto(
                        onPhotoSuccess = {
                            imageSelectorResult.value = ImageSelectorResult.ResultPhoto(it)
                        },
                        onPhotoError = {
                            imageSelectorResult.value = ImageSelectorResult
                                .Error(MR.strings.image_analysis_error.desc())
                        }
                    )
                }
                is ImageSelectorResult.ResultImage -> {
                    currentImageFromGalleryOrCamera = imgSelector.image
                    ImageSelectorViewData.ResultImage(
                        resultBitmapAsArray = imgSelector.image
                    )
                }
                is ImageSelectorResult.ResultPhoto -> {
                    currentImageFromGalleryOrCamera = imgSelector.image
                    ImageSelectorViewData.ResultPhoto(
                        resultBitmapAsArray = imgSelector.image
                    )
                }
                is ImageSelectorResult.Error -> {
                    ImageSelectorViewData.Error(
                        errorText = MR.strings.image_analysis_error_desc.format(imgSelector.message)
                    )
                }
            }

            ImageAnalysisViewData(
                title = mTitle.value,
                takePhotoButton = takePhotoButton,
                pickGalleryButton = pickImageButton,
                detectTopButton = detectTestImageButton,
                detectBottomButton = detectBottomButton,
                topImageAnalysisResults = resultsPytorchTest,
                bottomImageAnalysisResults = resultsPytorchGallery,
                topImageFromAssets = ivData.value,
                imageSelector = image
            )
        }.asCommonStateFlow()
}
