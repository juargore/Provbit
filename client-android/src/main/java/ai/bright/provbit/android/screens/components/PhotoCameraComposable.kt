package ai.bright.provbit.android.screens.components

import ai.bright.provbit.android.extensions.localized
import ai.bright.provbit.demo.presentation.components.ImageSelectorViewData
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect

@Composable
fun PhotoCameraComposable(
    viewData: ImageSelectorViewData,
    result: (ByteArray) -> Unit
) {
    when(viewData) {
        is ImageSelectorViewData.Error -> {
            Text(text = viewData.errorText.localized())
        }
        is ImageSelectorViewData.ResultPhoto -> {
            result(viewData.resultBitmapAsArray)
        }
        is ImageSelectorViewData.TakePhoto -> {
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.TakePicturePreview()
            ) { bmp ->

                if (bmp == null) {
                    viewData.onPhotoError("Bitmap is null!")
                } else {
                    viewData.onPhotoSuccess(bitmapToByteArray(bmp))
                }
            }

            SideEffect {
                launcher.launch(null)
            }
        }
        else -> Unit
    }
}