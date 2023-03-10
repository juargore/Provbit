package ai.bright.provbit.demo.adapters.realm.converters

import ai.bright.provbit.architecture.Converter
import ai.bright.provbit.demo.entities.Item
import ai.bright.provbit.demo.adapters.realm.models.RealmItem as RealmItem

class RealmItemConverter: Converter<RealmItem, Item> {

    /*
    Possible TODO:
    We probably would not want to realm a new realm item on conversion.
    Better to query for the existing realm item by guid.
     */

    override fun convertOutbound(value: Item): RealmItem = RealmItem().apply {
        guid = value.guid
        name = value.name
        desc = value.desc
    }

    override fun convertInbound(value: RealmItem): Item = Item(
        value.guid,
        value.name,
        value.desc
    )
}
