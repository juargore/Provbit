package ai.bright.provbit.demo.adapters.conch.ports

import ai.bright.provbit.architecture.Converter
import ai.bright.provbit.client.conch.apis.HardwareApi
import ai.bright.provbit.client.conch.models.Hardware
import ai.bright.provbit.demo.AppScope
import ai.bright.provbit.demo.adapters.conch.converters.ConchHardwareConverter
import ai.bright.provbit.demo.domain.ports.HardwareRepository
import ai.bright.provbit.demo.entities.HardwareId
import ai.bright.provbit.demo.entities.HardwareRecord
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class ConchHardwareRepository(
    private val hardwareApi: HardwareApi
): HardwareRepository {

    private val converter: Converter<Hardware, HardwareRecord> = ConchHardwareConverter()

    override suspend fun getItemById(hardwareId: HardwareId): HardwareRecord =
        hardwareApi.getHardware(hardwareId = hardwareId)
            .body
            .let(converter::convertInbound)
}