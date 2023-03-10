package ai.bright.provbit.android.screens

import ai.bright.provbit.android.ProcessorViewModel
import ai.bright.provbit.demo.SharedComponent
import ai.bright.provbit.demo.presentation.AppInfoEvent
import ai.bright.provbit.demo.presentation.AppInfoViewData
import ai.bright.provbit.demo.presentation.ToastViewData
import ai.bright.provbit.util.ProvbitLogger
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppInfoViewModel @Inject constructor(
    sc: SharedComponent
): ProcessorViewModel<AppInfoViewData, AppInfoEvent>(
    sc.appInfoProcessor()
)

private val log = ProvbitLogger("AppInfoComposable")

@Composable
fun AppInfoComposable(
    vm: AppInfoViewModel = hiltViewModel(),
    handle: (AppInfoEvent) -> Unit
) {
    log.v("Recompostion")
    val state by vm.viewData.collectAsState()
    vm.eventHandler = handle
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(state.versionInfoLabel.toString(LocalContext.current))
            Text(state.tapCountLabel.toString(LocalContext.current))
            Button(
                onClick = state.testButtonOnClick
            ) {
                Text(text = state.tapCountButtonLabel.toString(LocalContext.current))
            }
            Button(
                onClick = state.itemsButtonOnClick
            ) {
                Text(text = state.viewListButtonLabel.toString(LocalContext.current))
            }
            Button(
                onClick = state.itemSelectorButtonOnClick
            ) {
                Text(text = state.viewSelectorButtonLabel.toString(LocalContext.current))
            }
            Button(onClick = state.qrResultsButtonOnClick) {
                Text(text = "QR")
            }
            Button(
                onClick = state.viewStyleGuideOnClick
            ) {
                Text(text = state.viewStyleGuideButtonLabel.toString(LocalContext.current))
            }
            Button(
                onClick = state.imageAnalysisButtonOnClick
            ) {
                Text(text = state.imageAnalysisButtonLabel.toString(LocalContext.current))
            }
            Button(
                onClick = state.bluetoothButtonOnClick
            ) {
                Text(text = state.bluetoothButtonLabel.toString(LocalContext.current))
            }
        }

        when (val toast = state.toast) {
            ToastViewData.Hide -> Unit
            is ToastViewData.Show -> {
                Snackbar(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(text = toast.text.toString(LocalContext.current))
                }
            }
        }
    }
}

