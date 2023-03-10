package ai.bright.provbit.demo.adapters.bluetooth

import ai.bright.provbit.demo.adapters.bluetooth.BluetoothDevice.ConnectionStatus.Disconnected
import ai.bright.provbit.types.uuidString
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject

@Inject
class CannedBluetoothGateway : BluetoothGateway {

    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    private val cannedDevices = mutableMapOf(
        uuidString() to BluetoothDevice("Andrew's Macbook", Disconnected),
        uuidString() to BluetoothDevice("Josh's Macbook", Disconnected),
        uuidString() to BluetoothDevice("Ben's iPhone", Disconnected),
        uuidString() to BluetoothDevice("Arturo's Pixel 6", Disconnected),
        uuidString() to BluetoothDevice(null, Disconnected),
    )

    private val _state = MutableStateFlow<BluetoothScanState>(BluetoothScanState.NotStarted)

    override val state: StateFlow<BluetoothScanState> = _state.asStateFlow()

    override fun startScanning() {
        scope.launch {
            delay(1000)
            _state.value = BluetoothScanState.Active(cannedDevices.toMap())
        }
    }

    override fun stopScanning() = Unit

    override fun connect(id: HardwareId) {
        val device = cannedDevices[id] ?: return
        cannedDevices[id] = device.copy(
            connectionStatus = BluetoothDevice.ConnectionStatus.Connecting
        )
        _state.value = BluetoothScanState.Active(cannedDevices.toMap())
        scope.launch {
            delay(1500)
            cannedDevices[id] = device.copy(
                connectionStatus = BluetoothDevice.ConnectionStatus.Connected
            )
            _state.value = BluetoothScanState.Active(cannedDevices.toMap())
        }
    }

    override fun disconnect(id: HardwareId) {
        val device = cannedDevices[id] ?: return
        cannedDevices[id] = device.copy(connectionStatus = Disconnected)
        _state.value = BluetoothScanState.Active(cannedDevices.toMap())
    }
}
