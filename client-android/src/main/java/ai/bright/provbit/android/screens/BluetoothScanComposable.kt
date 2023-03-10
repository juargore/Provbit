package ai.bright.provbit.android.screens

import ai.bright.provbit.android.ProcessorViewModel
import ai.bright.provbit.android.extensions.localized
import ai.bright.provbit.android.screens.components.ButtonComposable
import ai.bright.provbit.demo.SharedComponent
import ai.bright.provbit.demo.presentation.BluetoothDeviceRowViewData
import ai.bright.provbit.demo.presentation.BluetoothDeviceRowViewData.ConnectionStateViewData.*
import ai.bright.provbit.demo.presentation.BluetoothScanEvent
import ai.bright.provbit.demo.presentation.BluetoothScanViewData
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BluetoothScanComposable @Inject constructor(
    sc: SharedComponent
) : ProcessorViewModel<BluetoothScanViewData, BluetoothScanEvent>(
    sc.bluetoothScanProcessor()
)

@ExperimentalPermissionsApi
@Composable
fun BluetoothScanComposable(
    vm: BluetoothScanComposable = hiltViewModel()
) {
    val viewData by vm.viewData.collectAsState()

    val permissionsList = mutableListOf(android.Manifest.permission.ACCESS_FINE_LOCATION)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        permissionsList.add(android.Manifest.permission.BLUETOOTH_SCAN)
        permissionsList.add(android.Manifest.permission.BLUETOOTH_CONNECT)
    }

    val locationPermission = rememberMultiplePermissionsState(permissionsList)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    locationPermission.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    if (locationPermission.allPermissionsGranted) {
        BluetoothScanScreen(viewData = viewData)
    } else {
        Text("All necessary permissions have not been granted")
        LaunchedEffect(key1 = true) {
            locationPermission.launchMultiplePermissionRequest()
        }
    }
}

@Composable
private fun BluetoothScanScreen(viewData: BluetoothScanViewData) {
    when (viewData) {
        is BluetoothScanViewData.DeviceList -> DeviceList(viewData)
        BluetoothScanViewData.Loading -> Loading()
        is BluetoothScanViewData.PreScan -> PreScan(viewData)
        is BluetoothScanViewData.ScanError -> ScanError(viewData)
    }
}

@Composable
private fun ScanError(viewData: BluetoothScanViewData.ScanError) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = viewData.errorText.localized())
        ButtonComposable(state = viewData.retryButton)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DeviceList(viewData: BluetoothScanViewData.DeviceList) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(start = 8.dp, top = 12.dp, bottom = 8.dp)
                    .alpha(.75f),
                text = viewData.connectedHeader.localized(),
                letterSpacing = 2.sp,
                style = MaterialTheme.typography.h6,
            )
        }
        items(viewData.connectedItems, { item -> item.id }) {
            DeviceRow(viewData = it, modifier = Modifier.animateItemPlacement())
        }
        item {
            Text(
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(start = 8.dp, top = 12.dp, bottom = 8.dp)
                    .alpha(.75f),
                text = viewData.notConnectedHeader.localized(),
                letterSpacing = 2.sp,
                style = MaterialTheme.typography.h6,
            )
        }
        items(viewData.disconnectedItems, { item -> item.id }) {
            DeviceRow(viewData = it, modifier = Modifier.animateItemPlacement())
        }
    }
}


@Composable
private fun DeviceRow(
    modifier: Modifier = Modifier,
    viewData: BluetoothDeviceRowViewData
) = Card(
    modifier = modifier
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .fillMaxWidth(),
    elevation = 4.dp
) {
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = viewData.name.localized(),
            style = MaterialTheme.typography.h6,
        )
        Text(
            text = viewData.id,
            fontSize = 14.sp,
            style = MaterialTheme.typography.overline,
        )
        when (val connectionState = viewData.connectionState) {
            is Connected -> ButtonComposable(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                state = connectionState.button,
            )
            is NotConnected -> ButtonComposable(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                state = connectionState.button,
            )
            Connecting -> {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 20.dp)
                )
            }
        }
    }
}

@Composable
private fun PreScan(viewData: BluetoothScanViewData.PreScan) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        ButtonComposable(state = viewData.startScanButton)
    }
}

@Composable
private fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Searching for devices...",
        )
    }
}
