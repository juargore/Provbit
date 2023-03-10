package ai.bright.provbit.android.extensions

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.getColor

@Composable
@ReadOnlyComposable
fun StringDesc.localized(): String = this.toString(LocalContext.current)

@Composable
@ReadOnlyComposable
fun ColorResource.localized(): Color = getColor(LocalContext.current)
    .let { Color(it.red, it.green, it.blue, it.alpha) }

@SuppressLint("UnrememberedMutableState")
@Composable
fun loadImageFromUrl(
    url: String,
    @DrawableRes defaultImage: Int
): MutableState<Bitmap?> {

    val bitmapState: MutableState<Bitmap?> = mutableStateOf(null)

    Glide.with(LocalContext.current)
        .asBitmap()
        .placeholder(defaultImage)
        .load(url)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmapState.value = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })

    return bitmapState
}