package ai.bright.provbit.demo.adapters.bluetooth

import me.tatarka.inject.annotations.Inject

@Inject
expect class NativeBluetoothConnectionGateway(context: BluetoothContext): BluetoothGateway