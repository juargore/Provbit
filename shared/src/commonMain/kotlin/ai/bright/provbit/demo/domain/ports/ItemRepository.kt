package ai.bright.provbit.demo.domain.ports

import ai.bright.provbit.demo.entities.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun addItem()
    fun deleteItem(item: Item)
    fun getItems(): Flow<List<Item>>
    suspend fun getItem(guid: String): Item
    suspend fun putItem(item: Item, name: String, desc: String)
}