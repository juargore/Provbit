package ai.bright.provbit.demo.domain.uses.items

import ai.bright.provbit.architecture.Result
import ai.bright.provbit.architecture.ResultInteractor
import ai.bright.provbit.architecture.catchResult
import ai.bright.provbit.demo.adapters.realm.models.RealmItem
import ai.bright.provbit.demo.domain.ports.ItemRepository
import ai.bright.provbit.demo.entities.Item
import me.tatarka.inject.annotations.Inject

/**
 * Pulls details for the an item with the matching guid.
 */
interface GetItemDetailUseCase: ResultInteractor<String, Unit, Item> {

    @Inject
    class Default(
        private val itemRepository: ItemRepository
    ): GetItemDetailUseCase {
        override suspend fun invoke(input: String): Result<Unit, Item> = catchResult {
            itemRepository.getItem(input)
        }
    }

}