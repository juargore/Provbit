package ai.bright.provbit.android.screens.components

import ai.bright.provbit.android.R
import ai.bright.provbit.android.extensions.loadImageFromUrl
import ai.bright.provbit.demo.presentation.components.ImageViewData
import ai.bright.provbit.demo.presentation.components.LoadImage
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

@Composable
fun ImageViewComposable(
    modifier: Modifier,
    viewData: ImageViewData
) {

    when (val imageData = viewData.loadImage) {
        is LoadImage.FromByteArray -> {
            viewData.imageByteArray = imageData.byteArray
            Image(
                bitmap = byteArrayToBitmap(imageData.byteArray).asImageBitmap(),
                contentDescription = viewData.contentDescription.toString(),
                modifier = modifier
            )
        }
        is LoadImage.FromResource -> {
            val image = painterResource(imageData.imageResource.drawableResId)

            viewData.imageByteArray = bitmapToByteArray(
                BitmapFactory.decodeResource(
                    LocalContext.current.resources,
                    imageData.imageResource.drawableResId
                )
            )

            Image(
                painter = image,
                contentDescription = viewData.contentDescription.toString(),
                modifier = modifier
            )
        }
        is LoadImage.FromUrl -> {
            val image = loadImageFromUrl(imageData.url, R.drawable.image_placeholder).value

            image?.let {
                viewData.imageByteArray = bitmapToByteArray(image)

                Image(
                    bitmap = image.asImageBitmap(),
                    contentDescription = viewData.contentDescription.toString(),
                    modifier = modifier
                )
            }
        }
    }
}
