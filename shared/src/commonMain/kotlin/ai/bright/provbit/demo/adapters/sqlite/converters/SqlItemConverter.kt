package ai.bright.provbit.demo.adapters.sqlite.converters

import ai.bright.provbit.architecture.Converter
import ai.bright.provbit.demo.adapters.sqlite.models.ItemDb
import ai.bright.provbit.demo.entities.Item

class SqlItemConverter: Converter<ItemDb, Item> {

    override fun convertOutbound(value: Item): ItemDb = ItemDb(
        guid = value.guid,
        name = value.name,
        desc = value.desc
    )

    override fun convertInbound(value: ItemDb): Item = Item(
        guid = value.guid,
        name = value.name,
        desc = value.desc
    )
}