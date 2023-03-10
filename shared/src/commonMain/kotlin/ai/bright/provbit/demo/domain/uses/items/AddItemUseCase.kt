package ai.bright.provbit.demo.domain.uses.items

import ai.bright.provbit.architecture.Result
import ai.bright.provbit.architecture.ResultInteractor
import ai.bright.provbit.architecture.catchResult
import ai.bright.provbit.demo.adapters.realm.models.RealmItem
import ai.bright.provbit.demo.domain.ports.ItemRepository
import ai.bright.provbit.demo.domain.uses.user.ActiveUserFlow
import me.tatarka.inject.annotations.Inject

interface AddItemUseCase: ResultInteractor<AddItem, Unit, Unit> {

    @Inject
    class Default(
        private val itemRepository: ItemRepository
    ): AddItemUseCase {
        override suspend fun invoke(input: AddItem): Result<Unit, Unit> = catchResult {
            itemRepository.addItem()
        }
    }
}

sealed class AddItem {
    object Add: AddItem()
}
