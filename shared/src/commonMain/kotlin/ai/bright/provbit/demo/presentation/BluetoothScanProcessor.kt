package ai.bright.provbit.demo.presentation

import ai.bright.provbit.architecture.BaseProcessor
import ai.bright.provbit.architecture.CommonStateFlow
import ai.bright.provbit.architecture.asCommonStateFlow
import ai.bright.provbit.architecture.map
import ai.bright.provbit.demo.adapters.bluetooth.BluetoothDevice
import ai.bright.provbit.demo.adapters.bluetooth.BluetoothDevice.ConnectionStatus
import ai.bright.provbit.demo.adapters.bluetooth.BluetoothGateway
import ai.bright.provbit.demo.adapters.bluetooth.BluetoothScanState
import ai.bright.provbit.demo.adapters.bluetooth.HardwareId
import ai.bright.provbit.demo.presentation.BluetoothDeviceRowViewData.ConnectionStateViewData.*
import ai.bright.provbit.demo.presentation.BluetoothScanViewData.*
import ai.bright.provbit.demo.presentation.components.ButtonViewData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

sealed class BluetoothScanViewData {

    /**
     * Display a button allowing the user to begin a bluetooth scan.
     *
     * Note: for V1 of this demo, we are letting the native platforms handle permission requesting
     * and state management. If the [startScanButton] on click lambda is called, it is assumed that
     * requisite permissions are granted. If they are not, the scan will result in an error.
     */
    data class PreScan(
        val startScanButton: ButtonViewData,
    ) : BluetoothScanViewData()

    /**
     * Display a loading spinner.
     */
    object Loading : BluetoothScanViewData()

    /**
     * Display a scan error.
     */
    data class ScanError(
        val retryButton: ButtonViewData,
        val errorText: StringDesc,
    ) : BluetoothScanViewData()

    /**
     * Display a list of devices.
     */
    data class DeviceList(
        val connectedItems: List<BluetoothDeviceRowViewData>,
        val disconnectedItems: List<BluetoothDeviceRowViewData>,
    ) : BluetoothScanViewData() {
        val connectedHeader: StringDesc = "CONNECTED".desc()
        val notConnectedHeader: StringDesc = "AVAILABLE".desc()
    }
}

/**
 * Models rows in device list.
 */
data class BluetoothDeviceRowViewData(
    val id: String,
    val name: StringDesc,
    val connectionState: ConnectionStateViewData,
) {
    sealed class ConnectionStateViewData {
        data class Connected(private val onDisconnectClick: () -> Unit) :
            ConnectionStateViewData() {
            val button = ButtonViewData(content = "Disconnect".desc(), onClick = onDisconnectClick)
        }

        data class NotConnected(private val onConnectClick: () -> Unit) :
            ConnectionStateViewData() {
            val button = ButtonViewData(content = "Connect".desc(), onClick = onConnectClick)
        }

        object Connecting : ConnectionStateViewData()
    }
}

sealed class BluetoothScanEvent

@Inject
class BluetoothScanProcessor(
    private val bluetoothGateway: BluetoothGateway,
) : BaseProcessor<BluetoothScanViewData, BluetoothScanEvent>() {

    private val preScan = PreScan(
        startScanButton = ButtonViewData(
            content = "Start Scan".desc(),
            onClick = ::startScanClicked
        )
    )

    private val scanError = ScanError(
        errorText = "Something went wrong scanning for devices".desc(),
        retryButton = ButtonViewData(
            content = "Try Again".desc(),
            onClick = ::startScanClicked
        )
    )

    private val scanState = MutableStateFlow<ProcessorScanState>(ProcessorScanState.Uninitialized)

    override val viewData: CommonStateFlow<BluetoothScanViewData> =
        map(scanState) { it.toViewData() }.asCommonStateFlow()

    init {
        bluetoothGateway.state.onEach {
            scanState.value = ProcessorScanState.Initialized(it)
        }.launchIn(this)
    }

    override fun close() {
        super.close()
        bluetoothGateway.stopScanning()
    }

    private fun startScanClicked() {
        scanState.value = ProcessorScanState.Loading
        bluetoothGateway.startScanning()
    }

    private sealed class ProcessorScanState {
        object Uninitialized : ProcessorScanState()
        object Loading : ProcessorScanState()
        data class Initialized(val state: BluetoothScanState) : ProcessorScanState()
    }

    private fun ProcessorScanState.toViewData(): BluetoothScanViewData = when (this) {
        is ProcessorScanState.Initialized -> this.state.toViewData()
        is ProcessorScanState.Loading -> Loading
        is ProcessorScanState.Uninitialized -> preScan
    }

    private fun BluetoothScanState.toViewData(): BluetoothScanViewData = when (this) {
        BluetoothScanState.Error -> scanError
        BluetoothScanState.NotStarted -> preScan
        is BluetoothScanState.Active -> toViewData(devices)
    }

    private fun onDisconnectClick(id: HardwareId) = bluetoothGateway.disconnect(id)

    private fun onConnectClick(id: HardwareId) = bluetoothGateway.connect(id)

    private fun toViewData(devices: Map<HardwareId, BluetoothDevice>): DeviceList {
        val connectedDevices = mutableListOf<BluetoothDeviceRowViewData>()
        val notConnectedDevices = mutableListOf<BluetoothDeviceRowViewData>()
        devices.forEach {
            val connectionState = when (it.value.connectionStatus) {
                ConnectionStatus.Connected -> Connected { onDisconnectClick(it.key) }
                ConnectionStatus.Disconnected -> NotConnected { onConnectClick(it.key) }
                ConnectionStatus.Connecting -> Connecting
            }
            val deviceViewData = BluetoothDeviceRowViewData(
                id = it.key,
                name = it.value.name?.desc() ?: "No Name".desc(),
                connectionState = connectionState
            )
            when (deviceViewData.connectionState) {
                is Connected -> connectedDevices.add(deviceViewData)
                Connecting -> notConnectedDevices.add(deviceViewData)
                is NotConnected -> notConnectedDevices.add(deviceViewData)
            }
        }
        return DeviceList(
            connectedItems = connectedDevices.toList(),
            disconnectedItems = notConnectedDevices.toList(),
        )
    }
}
