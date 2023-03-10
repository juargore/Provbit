package ai.bright.provbit.demo.domain.uses.items

import ai.bright.provbit.architecture.Result
import ai.bright.provbit.architecture.ResultInteractor
import ai.bright.provbit.architecture.catchResult
import ai.bright.provbit.demo.adapters.realm.models.RealmItem
import ai.bright.provbit.demo.domain.ports.ItemRepository
import ai.bright.provbit.demo.entities.Item
import me.tatarka.inject.annotations.Inject

interface UpdateItemUseCase : ResultInteractor<ItemUpdate, Unit, Unit> {

    @Inject
    class Default(
        private val itemRepository: ItemRepository
    ) : UpdateItemUseCase {
        override suspend fun invoke(input: ItemUpdate): Result<Unit, Unit> = catchResult {
            itemRepository.putItem(
                item = input.item,
                name = input.name,
                desc = input.desc,
            )
        }
    }
}

data class ItemUpdate(
    val item: Item,
    val name: String,
    val desc: String
)
