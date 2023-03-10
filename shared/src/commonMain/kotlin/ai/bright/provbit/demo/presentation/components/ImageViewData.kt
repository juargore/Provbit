package ai.bright.provbit.demo.presentation.components

import ai.bright.provbit.MR
import ai.bright.provbit.architecture.CommonStateFlow
import ai.bright.provbit.architecture.ViewScope
import ai.bright.provbit.architecture.asCommonStateFlow
import ai.bright.provbit.architecture.combine
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.MutableStateFlow

data class ImageViewData(
    var loadImage: LoadImage,
    val contentDescription: StringDesc? = null,
    var imageByteArray: ByteArray? = null
)

sealed class LoadImage {
    data class FromUrl(val url: String) : LoadImage()
    data class FromByteArray(val byteArray: ByteArray) : LoadImage()
    data class FromResource(val imageResource: ImageResource) : LoadImage()
}

fun ViewScope.imageViewComponent(
    image: LoadImage = LoadImage.FromResource(MR.images.image_placeholder),
    contentDescription: StringDesc? = null,
    imageByteArray: ByteArray? = byteArrayOf()
): CommonStateFlow<ImageViewData> {

    val loadImage = MutableStateFlow(image)
    val imgByteArray = MutableStateFlow(imageByteArray)

    return combine(loadImage, imgByteArray) { img, byteArray ->
        ImageViewData(
            loadImage = img,
            contentDescription = contentDescription,
            byteArray
        )
    }.asCommonStateFlow()
}
