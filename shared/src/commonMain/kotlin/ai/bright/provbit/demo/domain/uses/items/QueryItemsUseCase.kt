package ai.bright.provbit.demo.domain.uses.items

import ai.bright.provbit.architecture.FlowInteractor
import ai.bright.provbit.demo.domain.ports.ItemRepository
import ai.bright.provbit.demo.domain.uses.user.ActiveUserFlow
import ai.bright.provbit.demo.entities.Item
import kotlinx.coroutines.flow.*
import me.tatarka.inject.annotations.Inject

interface QueryItemsUseCase: FlowInteractor<ItemQuery, List<Item>> {

    @Inject
    class Default(
        private val itemRepository: ItemRepository
    ): QueryItemsUseCase {
        override fun invoke(input: ItemQuery): Flow<List<Item>> {
            val items = itemRepository.getItems()
            return when (input) {
                ItemQuery.All -> items
                is ItemQuery.NameContains -> items
                    .map { it.filter { item -> item.name.lowercase().contains(input.substring.lowercase()) } }
            }
        }
    }

}

/**
 * note: this type cannot be nested under [ActiveUserFlow] since it
 * breaks realm compilation... hmm.
 */
sealed class ItemQuery {
    object All: ItemQuery()
    data class NameContains(val substring: String): ItemQuery()
}

