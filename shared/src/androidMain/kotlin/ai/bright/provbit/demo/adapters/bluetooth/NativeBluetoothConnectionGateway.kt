@file:SuppressLint("MissingPermission")
package ai.bright.provbit.demo.adapters.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice.TRANSPORT_LE
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.bluetooth.BluetoothDevice as BLE

private const val SCAN_TIME = 5000L

actual class NativeBluetoothConnectionGateway actual constructor(
    private val context: BluetoothContext
) : BluetoothGateway {

    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private val bluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

    private lateinit var bleDevice: BLE
    private lateinit var bluetoothDevice: BluetoothDevice
    private lateinit var hardwareId: HardwareId
    private var mBluetoothGatt: BluetoothGatt? = null

    private var bleDevices = mutableMapOf<HardwareId, BLE>()
    private var bluetoothDevices = mutableMapOf<HardwareId, BluetoothDevice>()

    private val _scanState = MutableStateFlow<BluetoothScanState>(BluetoothScanState.NotStarted)
    override val state: StateFlow<BluetoothScanState> = _scanState.asStateFlow()

    override fun startScanning() {
        scope.launch {
            bluetoothAdapter.bluetoothLeScanner.startScan(bleScanner)
            delay(SCAN_TIME)
            stopScanning()
        }
    }

    private val bleScanner = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.let { handleDeviceFound(it.device) }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            _scanState.value = BluetoothScanState.Error
        }
    }

    private fun handleDeviceFound(newDevice: BLE) {
        val mHardwareId = newDevice.address
        bluetoothDevices = when(val state = _scanState.value) {
            BluetoothScanState.Error -> mutableMapOf()
            BluetoothScanState.NotStarted -> mutableMapOf()
            is BluetoothScanState.Active -> state.devices.toMutableMap()
        }

        val existingDevice = bluetoothDevices.containsKey(mHardwareId)

        if (existingDevice) {
            bluetoothDevices.replace(mHardwareId, newDevice.toKmmDevice())
            bleDevices.replace(mHardwareId, newDevice)
        } else {
            bluetoothDevices[mHardwareId] = newDevice.toKmmDevice()
            bleDevices[mHardwareId] = newDevice
        }

        _scanState.value = BluetoothScanState.Active(bluetoothDevices.toMap())
    }

    override fun stopScanning() {
        bluetoothAdapter.bluetoothLeScanner.stopScan(bleScanner)
    }

    override fun connect(id: HardwareId) {
        bleDevice = bleDevices[id] ?: return
        bluetoothDevice = bluetoothDevices[id] ?: return
        hardwareId = id

        bluetoothDevices[id] = bluetoothDevice.copy(
            connectionStatus = BluetoothDevice.ConnectionStatus.Connecting
        )

        _scanState.value = BluetoothScanState.Active(bluetoothDevices.toMap())
        mBluetoothGatt = bleDevice.connectGatt(context, false, bluetoothGattCallback, TRANSPORT_LE)
    }

    override fun disconnect(id: HardwareId) {
        hardwareId = id
        mBluetoothGatt?.disconnect()
    }

    private var bluetoothGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                bluetoothDevices[hardwareId] = bluetoothDevice.copy(
                    connectionStatus = BluetoothDevice.ConnectionStatus.Connected
                )

                _scanState.value = BluetoothScanState.Active(bluetoothDevices.toMap())
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                bluetoothDevices[hardwareId] = bluetoothDevice.copy(
                    connectionStatus = BluetoothDevice.ConnectionStatus.Disconnected
                )

                _scanState.value = BluetoothScanState.Active(bluetoothDevices.toMap())
            }
        }
    }
}

fun BLE.toKmmDevice(): BluetoothDevice =
    BluetoothDevice(
        name = this.name,
        connectionStatus = BluetoothDevice.ConnectionStatus.Disconnected
    )