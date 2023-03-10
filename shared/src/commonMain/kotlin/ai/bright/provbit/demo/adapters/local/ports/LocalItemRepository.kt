package ai.bright.provbit.demo.adapters.local.ports

import ai.bright.provbit.demo.AppScope
import ai.bright.provbit.demo.domain.ports.ItemRepository
import ai.bright.provbit.demo.entities.Item
import ai.bright.provbit.types.uuidString
import me.tatarka.inject.annotations.Inject

//@Inject
//@AppScope
//class LocalItemRepository: ItemRepository {
//
//    private val itemList by lazy {
//        mutableListOf(
//            Item(uuidString(), "Laptop", "For typing on the go."),
//            Item(uuidString(), "Television", "For watching your favorite shows."),
//            Item(uuidString(), "Telephone", "For calling your friends and family"),
//            Item(uuidString(), "Stereo", "For listening to John Denver."),
//        )
//    }
//
//    override fun addItem() {
//        itemList.add(itemList.random())
//    }
//
//    override suspend fun getItems(): List<Item> {
//        return itemList
//    }
//
//    override suspend fun getItem(guid: String): Item = itemList.find {
//        it.guid == guid
//    } ?: throw Throwable()
//
//    override suspend fun putItem(item: Item, name: String, desc: String) {
//        item.name = name
//        item.desc = desc
//        itemList.add(item)
//    }
//}
