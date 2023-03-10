package ai.bright.provbit.demo.domain.uses.hardware

import ai.bright.provbit.architecture.Result
import ai.bright.provbit.architecture.ResultInteractor
import ai.bright.provbit.architecture.catchResult
import ai.bright.provbit.demo.domain.ports.HardwareRepository
import ai.bright.provbit.demo.entities.HardwareId
import ai.bright.provbit.demo.entities.HardwareRecord
import me.tatarka.inject.annotations.Inject

interface GetHardwareUseCase: ResultInteractor<HardwareId, Unit, HardwareRecord> {

    @Inject
    class Default(
        private val hardwareRepository: HardwareRepository,
    ): GetHardwareUseCase {
        override suspend fun invoke(input: HardwareId): Result<Unit, HardwareRecord> = catchResult {
            hardwareRepository.getItemById(hardwareId = input)
        }

    }
}