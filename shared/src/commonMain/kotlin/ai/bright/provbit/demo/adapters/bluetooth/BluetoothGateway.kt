package ai.bright.provbit.demo.adapters.bluetooth

import kotlinx.coroutines.flow.StateFlow

typealias HardwareId = String

/**
 * Models a bluetooth device.
 */
data class BluetoothDevice(
    val name: String?,
    val connectionStatus: ConnectionStatus,
) {

    /**
     * Models the connections status of a bluetooth device.
     */
    sealed class ConnectionStatus {
        object Disconnected : ConnectionStatus()
        object Connected : ConnectionStatus()
        object Connecting : ConnectionStatus()
    }
}

/**
 * Models the current state of bluetooth scan.
 */
sealed class BluetoothScanState {

    /**
     * Scanning has not started.
     */
    object NotStarted : BluetoothScanState()

    /**
     * Represents an active bluetooth state with all available devices.
     */
    data class Active(val devices: Map<HardwareId, BluetoothDevice>) : BluetoothScanState()

    /**
     * Something went wrong scanning for devices.
     */
    object Error : BluetoothScanState()
}

/**
 * Gateway for managing bluetooth connections.
 *
 * Should be injected a singleton object.
 */
interface BluetoothGateway {

    /**
     * The current scan state of the native BluetoothGateway impl.
     */
    val state: StateFlow<BluetoothScanState>

    /**
     * Begin a bluetooth scan. Results will be reflected in [state].
     */
    fun startScanning()

    /**
     * Stop a bluetooth scan. Results will be reflected in [state].
     */
    fun stopScanning()

    /**
     * Connect to the device with the given hardware ID. Results will be reflected in [state].
     */
    fun connect(id: HardwareId)

    /**
     * Disconnect from the device with the given hardware ID. Results will be reflected in [state].
     */
    fun disconnect(id: HardwareId)
}
