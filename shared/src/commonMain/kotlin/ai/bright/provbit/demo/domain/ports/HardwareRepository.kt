package ai.bright.provbit.demo.domain.ports

import ai.bright.provbit.demo.entities.HardwareId
import ai.bright.provbit.demo.entities.HardwareRecord

interface HardwareRepository {
    suspend fun getItemById(hardwareId: HardwareId): HardwareRecord
}