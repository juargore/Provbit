package ai.bright.provbit.demo.adapters.sqlite.ports

import ai.bright.provbit.architecture.Converter
import ai.bright.provbit.demo.AppScope
import ai.bright.provbit.demo.adapters.sqlite.converters.SqlItemConverter
import ai.bright.provbit.demo.adapters.sqlite.models.ItemDb
import ai.bright.provbit.demo.adapters.sqlite.models.ItemDbQueries
import ai.bright.provbit.demo.domain.ports.ItemRepository
import ai.bright.provbit.demo.entities.Item
import ai.bright.provbit.types.uuidString
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class SqlItemRepository(
    private val queries: ItemDbQueries
) : ItemRepository {

    private val itemList =
        listOf(
            Item(uuidString(), "SQLDelight - Laptop", "For typing on the go."),
            Item(uuidString(), "SQLDelight - Television", "For watching your favorite shows."),
            Item(uuidString(), "SQLDelight - Telephone", "For calling your friends and family"),
            Item(uuidString(), "SQLDelight - Stereo", "For listening to John Denver."),
        )

    private val randomItem: Item
        get() = itemList.random()

    private val converter: Converter<ItemDb, Item> = SqlItemConverter()

    override fun getItems(): Flow<List<Item>> =
        queries
            .selectAll()
            .asFlow()
            .mapToList()
            .map { it.map { converter.convertInbound(it) } }

    override fun addItem() {
        val randomItemDb = converter.convertOutbound(randomItem)
        queries.insertItem(
            guid = uuidString(),
            name = randomItemDb.name,
            desc = randomItemDb.desc
        )
    }

    override fun deleteItem(item: Item) {
        val convertedItem = converter.convertOutbound(item)
        queries.deleteItem(convertedItem.guid)
    }

    override suspend fun getItem(guid: String): Item =
        queries
            .selectById(guid)
            .executeAsOne()
            .let(converter::convertInbound)

    override suspend fun putItem(item: Item, name: String, desc: String) {
        val itemDb = converter.convertOutbound(item)
        queries.updateItem(
            guid = itemDb.guid,
            name = name,
            desc = desc
        )
    }
}
