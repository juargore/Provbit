package ai.bright.provbit.demo.adapters.bluetooth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.CoreBluetooth.*
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.Foundation.NSUUID
import platform.darwin.NSObject

actual class NativeBluetoothConnectionGateway actual constructor(
    val context: BluetoothContext
) : BluetoothGateway {

    private val _discoveredDevices = mutableMapOf<HardwareId, BluetoothDevice>()

    private val _state = MutableStateFlow<BluetoothScanState>(BluetoothScanState.NotStarted)
    override val state: StateFlow<BluetoothScanState> = _state.asStateFlow()

    private val peripheralDelegate = object : NSObject(), CBPeripheralDelegateProtocol {
        /**
         * Tells the delegate that peripheral service discovery succeeded.
         * Core Bluetooth invokes this method when your app calls the discoverServices(_:) method.
         * If the peripheral successfully discovers services, you can access them through the peripheral’s services property.
         * If successful, the error parameter is nil. If unsuccessful, the error parameter returns the cause of the failure.
         */
        override fun peripheral(peripheral: CBPeripheral, didDiscoverServices: NSError?) {
            println("iOS - didDiscoverServices")
        }

        /**
         *Tells the delegate that the peripheral found characteristics for a service.
         * Core Bluetooth invokes this method when your app calls the discoverCharacteristics(_:for:) method.
         * If the peripheral successfully discovers the characteristics of the specified service, you can access them through the service’s characteristics property.
         * If successful, the error parameter is nil. If unsuccessful, the error parameter returns the cause of the failure.
         */
        override fun peripheral(
            peripheral: CBPeripheral,
            didDiscoverCharacteristicsForService: CBService,
            error: NSError?
        ) {
            println("iOS - didDiscoverCharacteristicsForService")
        }

        /**
         * Tells the delegate that retrieving the specified characteristic’s value succeeded, or that the characteristic’s value changed.
         * Core Bluetooth invokes this method when your app calls the readValue(for:) method.
         * A peripheral also invokes this method to notify your app of a change to the value of the characteristic for which
         * the app previously enabled notifications by calling setNotifyValue(_:for:).
         * If successful, the error parameter is nil. If unsuccessful, the error parameter returns the cause of the failure.
         */
        override fun peripheral(
            peripheral: CBPeripheral,
            didUpdateValueForCharacteristic: CBCharacteristic,
            error: NSError?
        ) {
            println("iOS - didUpdateValueForCharacteristic")
        }
    }

    private val centralManagerDelegate = object : NSObject(), CBCentralManagerDelegateProtocol {
        /**
         * Tells the delegate the central manager’s state updated.
         * You implement this required method to ensure that the central device supports Bluetooth low energy
         * and that it’s available to use.
         */
        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            println("iOS - centralManagerDidUpdateState method called")
            when (central.state) {
                CBCentralManagerStatePoweredOn -> println("centralManager poweredOn")
                CBCentralManagerStatePoweredOff -> println("centralManager poweredOff")
                CBCentralManagerStateResetting -> println("centralManager resetting")
                CBCentralManagerStateUnauthorized -> println("centralManager unauthorized")
                CBCentralManagerStateUnknown -> println("centralManager unknown")
                CBCentralManagerStateUnsupported -> println("centralManager unsupported")
            }
        }

        /**
         * Tells the delegate the central manager discovered a peripheral while scanning for devices.
         * You can access the advertisement data with the keys listed in Advertisement Data Retrieval Keys.
         * You must retain a local copy of the peripheral if you want to perform commands on it.
         * Use the RSSI data to determine the proximity of a discoverable peripheral device, and whether you want to connect to it automatically.
         */
        override fun centralManager(
            central: CBCentralManager,
            didDiscoverPeripheral: CBPeripheral,
            advertisementData: Map<Any?, *>,
            RSSI: NSNumber  //can be used to check how close the peripheral is.
        ) {
            println("iOS - didDiscoverPeripheral called: ${didDiscoverPeripheral.name}, ${RSSI.toString()}")
            val deviceId = didDiscoverPeripheral.identifier.toString()
            val connectionStatus = _discoveredDevices[deviceId]?.connectionStatus
                ?: BluetoothDevice.ConnectionStatus.Disconnected
            val discoveredDevice = BluetoothDevice(
                name = didDiscoverPeripheral.name,
                connectionStatus = connectionStatus
            )
            _discoveredDevices[deviceId] = discoveredDevice
            _state.value = BluetoothScanState.Active(_discoveredDevices.toMap())
        }

        /**
         * Tells the delegate that the central manager connected to a peripheral.
         * The manager invokes this method when a call to connect(_:options:) succeeds.
         * You typically implement this method to set the peripheral’s delegate and discover its services.
         */
        override fun centralManager(central: CBCentralManager, didConnectPeripheral: CBPeripheral) {
            println("iOS - didConnectPeripheral called: ${didConnectPeripheral.name}")
            val connectedDevice = BluetoothDevice(
                didConnectPeripheral.name,
                BluetoothDevice.ConnectionStatus.Connected
            )
            _discoveredDevices[didConnectPeripheral.identifier.toString()] = connectedDevice
            _state.value = BluetoothScanState.Active(_discoveredDevices.toMap())
        }

        /**
         * NOTE: The "CONFLICTING_OVERLOADS" suppression is used due to the language differences between Kotlin and Swift.
         * In iOS (Objc and Swift) you can have a method/function with the same name and same parameter type but different
         * parameter name.  This is not allowed in Kotlin hence this suppression.
         *
         * Tells the delegate the central manager failed to create a connection with a peripheral.
         *The manager invokes this method when a connection initiated with the connect(_:options:) method fails to complete.
         * Because connection attempts don’t time out, a failed connection usually indicates a transient issue,
         * in which case you may attempt connecting to the peripheral again.
         */
        @Suppress("CONFLICTING_OVERLOADS")
        override fun centralManager(
            central: CBCentralManager,
            didFailToConnectPeripheral: CBPeripheral,
            error: NSError?
        ) {
            val device = BluetoothDevice(
                didFailToConnectPeripheral.name,
                BluetoothDevice.ConnectionStatus.Disconnected
            )
            _discoveredDevices[didFailToConnectPeripheral.identifier.toString()] = device
            _state.value = BluetoothScanState.Error
        }

        /**
         * Tells the delegate that the central manager disconnected from a peripheral.
         * The manager invokes this method when disconnecting a peripheral previously connected with the connect(_:options:) method.
         * The error parameter contains the reason for the disconnection, unless the disconnect resulted from a call to cancelPeripheralConnection(_:).
         * After this method executes, the peripheral device’s CBPeripheralDelegate object receives no further method calls.
         * All services, characteristics, and characteristic descriptors a peripheral become invalidated after it disconnects.
         */
        @Suppress("CONFLICTING_OVERLOADS")
        override fun centralManager(
            central: CBCentralManager,
            didDisconnectPeripheral: CBPeripheral,
            error: NSError?
        ) {
            println("iOS - didDisconnectPeripheral called")
            val disconnectedDevice = BluetoothDevice(
                didDisconnectPeripheral.name,
                BluetoothDevice.ConnectionStatus.Disconnected
            )
            _discoveredDevices[didDisconnectPeripheral.identifier.toString()] = disconnectedDevice
            _state.value = BluetoothScanState.Active(_discoveredDevices.toMap())
        }
    }

    /**
     * An object that scans for, discovers, connects to, and manages peripherals.
     * `CBCentralManager` objects manage discovered or connected remote peripheral devices (represented by CBPeripheral objects),
     * including scanning for, discovering, and connecting to advertising peripherals.
     */
    private val centralManager = CBCentralManager()

    /**
     * The `CBPeripheral` class represents remote peripheral devices that your app discovers with a central manager (an instance of CBCentralManager).
     * Peripherals use universally unique identifiers (UUIDs), represented by NSUUID objects, to identify themselves.
     * Peripherals may contain one or more services or provide useful information about their connected signal strength.
     * Source: https://developer.apple.com/documentation/corebluetooth/cbperipheral
     */
    private val peripheral = CBPeripheral()

    init {
        centralManager.delegate = centralManagerDelegate
        peripheral.delegate = peripheralDelegate
    }
    /*
    Helper Methods
     */

    private fun getCbperipheralBy(id: HardwareId): CBPeripheral? {
        val uuids = listOf<NSUUID>(NSUUID(id))
        val peripherals = centralManager.retrievePeripheralsWithIdentifiers(identifiers = uuids)
            .map { it as CBPeripheral }
        return peripherals.findLast { it.identifier.toString() == id }
    }

    /*
    Method impls
     */

    override fun startScanning() {
        println("startScanning called.")
        centralManager.scanForPeripheralsWithServices(null, null)
    }

    override fun stopScanning() {
        println("stopScanning called.")
        centralManager.stopScan()
        _state.value = BluetoothScanState.NotStarted
        _discoveredDevices.clear()
    }

    override fun connect(id: HardwareId) {
        println("connect method called.")
        getCbperipheralBy(id)?.let { cbPeripheral ->
            val connectingDevice = BluetoothDevice(
                cbPeripheral.name,
                BluetoothDevice.ConnectionStatus.Connecting
            )
            _discoveredDevices[cbPeripheral.identifier.toString()] = connectingDevice
            _state.value = BluetoothScanState.Active(_discoveredDevices.toMap())

            centralManager.connectPeripheral(cbPeripheral, null)
        }
    }

    override fun disconnect(id: HardwareId) {
        println("disconnect method called.")
        getCbperipheralBy(id)?.let { cbPeripheral ->
            centralManager.cancelPeripheralConnection(cbPeripheral)
        }
    }

}
