package ai.bright.provbit.demo.domain.uses.items

import ai.bright.provbit.architecture.Result
import ai.bright.provbit.architecture.ResultInteractor
import ai.bright.provbit.architecture.catchResult
import ai.bright.provbit.demo.adapters.realm.models.RealmItem
import ai.bright.provbit.demo.domain.ports.ItemRepository
import ai.bright.provbit.demo.entities.Item
import me.tatarka.inject.annotations.Inject

interface DeleteItemUseCase: ResultInteractor<DeleteItem, Unit, Unit> {

    @Inject
    class Default(
        private val itemRepository: ItemRepository
    ): DeleteItemUseCase {
        override suspend fun invoke(input: DeleteItem): Result<Unit, Unit> = catchResult {
            when (input) {
                is DeleteItem.Delete -> itemRepository.deleteItem(input.item)
            }
        }
    }
}

sealed class DeleteItem {
    data class Delete(val item: Item): DeleteItem()
}
