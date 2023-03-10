@file:Suppress("DEPRECATION")
package ai.bright.provbit.android.screens.components

import ai.bright.provbit.android.extensions.localized
import ai.bright.provbit.demo.presentation.components.ImageSelectorViewData
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.ByteArrayOutputStream

@Composable
fun ImagePickerComposable(
    viewData: ImageSelectorViewData,
    result: (ByteArray) -> Unit
) {
    when(viewData) {
        is ImageSelectorViewData.Error -> {
            Text(text = viewData.errorText.localized())
        }
        is ImageSelectorViewData.ResultImage -> {
            result(viewData.resultBitmapAsArray)
        }
        is ImageSelectorViewData.PickImage -> {
            val context = LocalContext.current
            val bitmap =  remember {
                mutableStateOf<Bitmap?>(null)
            }

            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()
            ) { uri: Uri? ->

                uri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap.value = MediaStore.Images
                            .Media.getBitmap(context.contentResolver, it)
                    } else {
                        val source = ImageDecoder
                            .createSource(context.contentResolver, it)
                        bitmap.value = ImageDecoder.decodeBitmap(source)
                    }

                    if (bitmap.value == null) {
                        viewData.onPickImageError("Bitmap is null!")
                    } else {
                        viewData.onPickImageSuccess(bitmapToByteArray(bitmap.value!!))
                    }
                }
            }

            SideEffect {
                launcher.launch("image/*")
            }
        }
        else -> Unit
    }
}

fun byteArrayToBitmap(imageData: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
}

fun bitmapToByteArray(bitmap: Bitmap) : ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
    return stream.toByteArray()
}