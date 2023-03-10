package ai.bright.provbit.android.screens

import ai.bright.provbit.android.ProcessorViewModel
import ai.bright.provbit.android.extensions.localized
import ai.bright.provbit.android.screens.components.*
import ai.bright.provbit.demo.SharedComponent
import ai.bright.provbit.demo.presentation.ImageAnalysisEvent
import ai.bright.provbit.demo.presentation.ImageAnalysisViewData
import ai.bright.provbit.demo.presentation.components.LoadImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageAnalysisViewModel @Inject constructor(
    sc: SharedComponent
) : ProcessorViewModel<ImageAnalysisViewData, ImageAnalysisEvent>(
    sc.imageAnalysisProcessor()
)

@ExperimentalPermissionsApi
@Composable
fun ImageAnalysisComposable(
    vm: ImageAnalysisViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by vm.viewData.collectAsState()

    val imageSelected = remember { mutableStateOf(byteArrayOf()) }
    val imgAnimals = context.assets.open("animals.jpg").readBytes()
    val imgDogBicycle = context.assets.open("dog_bicycle.jpg").readBytes()
    val imgDogFamily = context.assets.open("dog_family.jpg").readBytes()
    val imgDogSample = context.assets.open("dog_sample.jpg").readBytes()
    val imagesList = listOf(imgAnimals, imgDogBicycle, imgDogFamily, imgDogSample)

    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // ---------------- title ----------------- //
        Text(
            text = state.title.localized(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth()
        )

        // ---------------- Top Image from Assets ----------------- //
        Divider()

        if (imageSelected.value.isEmpty()) {
            imageSelected.value = imagesList.random()
        }
        state.topImageFromAssets.loadImage = LoadImage.FromByteArray(imageSelected.value)

        ImageViewComposable(
            viewData = state.topImageFromAssets,
            modifier = Modifier.height(200.dp)
        )
        ButtonComposable(state.detectTopButton)
        ResultViewCompose(results = state.topImageAnalysisResults)

        // ---------------- Gallery Picker and Camera Photo section ----------------- //
        Divider()
        var mByteArray by rememberSaveable { mutableStateOf(byteArrayOf()) }

        if (mByteArray.isNotEmpty()) {
            Image(bitmap = byteArrayToBitmap(mByteArray).asImageBitmap(), contentDescription = null)
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            ButtonComposable(state.pickGalleryButton)
            ButtonComposable(state.takePhotoButton)
            ButtonComposable(state.detectBottomButton)
            ResultViewCompose(state.bottomImageAnalysisResults)
            ImagePickerComposable(state.imageSelector) { mByteArray = it }
            PhotoCameraComposable(state.imageSelector) { mByteArray = it }
        }
    }
}

@Composable
fun Divider() {
    Spacer(modifier = Modifier.height(40.dp))
}
