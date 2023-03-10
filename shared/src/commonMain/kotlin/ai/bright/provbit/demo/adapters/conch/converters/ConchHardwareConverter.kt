package ai.bright.provbit.demo.adapters.conch.converters

import ai.bright.provbit.architecture.Converter
import ai.bright.provbit.demo.entities.HardwareRecord
import ai.bright.provbit.client.conch.models.Hardware as ConchHardware

class ConchHardwareConverter: Converter<ConchHardware, HardwareRecord> {

    override fun convertInbound(value: ConchHardware): HardwareRecord = HardwareRecord(
        publicKey = value.publicKey,
        hardwareId = value.hardwareId,
        serialNumber = value.serialNumber
    )
}