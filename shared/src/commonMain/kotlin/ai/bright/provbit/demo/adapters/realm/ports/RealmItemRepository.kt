package ai.bright.provbit.demo.adapters.realm.ports

import ai.bright.provbit.architecture.Converter
import ai.bright.provbit.demo.adapters.realm.converters.RealmItemConverter
import ai.bright.provbit.demo.adapters.realm.models.RealmItem
import ai.bright.provbit.demo.domain.ports.ItemRepository
import ai.bright.provbit.demo.entities.Item
import ai.bright.provbit.types.uuidString
import io.realm.Realm
import io.realm.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import me.tatarka.inject.annotations.Inject

@Inject
class RealmItemRepository(
    private val realm: Realm
) : ItemRepository {

    private val itemConverter: Converter<RealmItem, Item> = RealmItemConverter()

    init {
        /*
        Uncomment to see where the local realm is saved on your machine.
         */
//        println("realm path: ${realm.configuration.path}")
    }

    private val itemList by lazy {
        mutableListOf(
            Item(uuidString(), "Laptop (Realm)", "For typing on the go."),
            Item(uuidString(), "Television (Realm)", "For watching your favorite shows."),
            Item(uuidString(), "Telephone (Realm)", "For calling your friends and family"),
            Item(uuidString(), "Stereo (Realm)", "For listening to John Denver."),
        )
    }

    override fun addItem() {
        val randomItem = itemList.random()
        realm.writeBlocking {
            val realmItem = RealmItem()
            realmItem.guid = uuidString()   //ensures the id is different on each creation.
            realmItem.name = randomItem.name
            realmItem.desc = randomItem.desc

            copyToRealm(realmItem)
        }
    }

    override fun deleteItem(item: Item) {
        realm.writeBlocking {
            val realmItem: RealmItem? = realm.query<RealmItem>("guid == $0", item.guid)
                .first()
                .find()
            if (realmItem != null) {
                findLatest(realmItem)?.also { delete(it) }
            }
        }
    }

    override fun getItems(): Flow<List<Item>> {
        return realm.query<RealmItem>()
            .asFlow()
            .transform {
                emit(it.list.map {
                    itemConverter.convertInbound(it)
                })
            }
    }

    override suspend fun getItem(guid: String): Item {
        val realmItem = realm.query<RealmItem>("guid = $0", guid)
            .first()
            .find()
            ?: throw Exception("Couldn't find matching element")

        return itemConverter.convertInbound(realmItem)
    }

    override suspend fun putItem(item: Item, updatedName: String, updatedDesc: String) {
        realm.writeBlocking {
            val realmItem: RealmItem? = realm.query<RealmItem>("guid == $0", item.guid)
                .first()
                .find()
            if (realmItem != null) {
                findLatest(realmItem)?.name = updatedName
                findLatest(realmItem)?.desc = updatedDesc
            }
        }
    }

}
