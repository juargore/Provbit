package ai.bright.provbit.android.screens

import ai.bright.provbit.android.ProcessorViewModel
import ai.bright.provbit.android.extensions.localized
import ai.bright.provbit.demo.SharedComponent
import ai.bright.provbit.demo.presentation.CameraPermissionStatus.*
import ai.bright.provbit.demo.presentation.QRScanEvent
import ai.bright.provbit.demo.presentation.QRScanViewData
import android.annotation.SuppressLint
import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class QRScanViewModel @Inject constructor(
    sc: SharedComponent
) : ProcessorViewModel<QRScanViewData, QRScanEvent>(sc.qrScanProcessor.invoke())

@ExperimentalPermissionsApi
@Composable
fun QRScanScreen(
    vm: QRScanViewModel = hiltViewModel()
) {
    val viewData by vm.viewData.collectAsState()
    QRScanComposable(viewData = viewData)
}

@ExperimentalPermissionsApi
@Composable
private fun QRScanComposable(
    viewData: QRScanViewData,
) {
    var hasUserRespondedToPermissionsPrompt by remember { mutableStateOf(false) }
    val permissionState = rememberPermissionState(android.Manifest.permission.CAMERA) {
        hasUserRespondedToPermissionsPrompt = true
    }

    when (val status = permissionState.status) {
        PermissionStatus.Granted -> viewData.onCameraPermissionChange(Granted)
        is PermissionStatus.Denied -> if (hasUserRespondedToPermissionsPrompt) {
            viewData.onCameraPermissionChange(Denied.Android(status.shouldShowRationale))
        } else {
            viewData.onCameraPermissionChange(Undetermined)
        }
    }

    when (viewData) {
        is QRScanViewData.Error -> {
            Text(text = viewData.errorText.localized())
        }
        is QRScanViewData.Loading -> {
            if (permissionState.status.shouldShowRationale) {
                CameraPermissionRationale { permissionState.launchPermissionRequest() }
            } else {
                LaunchedEffect(key1 = true) {
                    permissionState.launchPermissionRequest()
                }
            }
        }
        is QRScanViewData.Result -> {
            Text(text = viewData.resultText.localized())
        }
        is QRScanViewData.Scan -> {
            QRCodeScanner(
                onScanResult = viewData.onCodeScanned,
                onScanError = viewData.onScanError
            )
        }
        is QRScanViewData.NoPermission.Android.NeedsSettings -> {
            Text(text = viewData.message.localized())
        }
        is QRScanViewData.NoPermission.Android.ShowRationale -> {
            CameraPermissionRationale { permissionState.launchPermissionRequest() }
        }
        is QRScanViewData.NoPermission.IOS -> Unit
    }
}

@Composable
private fun CameraPermissionRationale(onRequestPermissionClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Here's our rationale: we need camera permissions to scan an QR code.")
        Button(onClick = onRequestPermissionClick) {
            Text(text = "Grant Permission")
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
private fun QRCodeScanner(
    onScanResult: (String) -> Unit,
    onScanError: (String) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = androidx.camera.core.Preview.Builder().build()
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

    val previewView = remember { PreviewView(context) }
    val analysis = remember {
        ImageAnalysis.Builder()
            .build()
            .apply {
                val scanner = BarcodeScanning.getClient()
                setAnalyzer(
                    Executors.newSingleThreadExecutor()
                ) { proxy ->
                    proxy.image?.also { img ->
                        val inputimg =
                            InputImage.fromMediaImage(img, proxy.imageInfo.rotationDegrees)
                        scanner.process(inputimg)
                            .addOnSuccessListener { list ->
                                // Send the first result to the result handler.
                                list.getOrNull(0)?.rawValue?.also(onScanResult)
                            }
                            .addOnFailureListener {
                                onScanError.invoke(it.message ?: "Unknown error.")
                            }
                            .addOnCompleteListener {
                                proxy.close()
                            }

                    }
                }

            }
    }
    LaunchedEffect(true) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            analysis
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }


    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize()) { }
    }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}