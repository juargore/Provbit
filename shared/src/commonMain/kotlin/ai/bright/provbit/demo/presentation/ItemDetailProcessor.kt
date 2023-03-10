package ai.bright.provbit.demo.presentation

//import ai.bright.provbit.demo.entities.Item
import ai.bright.provbit.architecture.*
import ai.bright.provbit.demo.adapters.realm.models.RealmItem
import ai.bright.provbit.demo.domain.uses.items.GetItemDetailUseCase
import ai.bright.provbit.demo.domain.uses.items.ItemUpdate
import ai.bright.provbit.demo.domain.uses.items.UpdateItemUseCase
import ai.bright.provbit.demo.entities.Item
import ai.bright.provbit.demo.presentation.components.ButtonViewData
import ai.bright.provbit.demo.presentation.components.EditTextViewData
import ai.bright.provbit.demo.presentation.components.editTextComponent
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

sealed class ItemDetailViewData {

    object Loading : ItemDetailViewData()

    object Error : ItemDetailViewData()

    data class ShowItem(
        val name: CommonStateFlow<EditTextViewData>,
        val desc: CommonStateFlow<EditTextViewData>,
        val save: ButtonViewData
    ) : ItemDetailViewData()

}

sealed class ItemDetailEvent {
    object Back : ItemDetailEvent()
}

@Inject
class ItemDetailProcessor(
    private val getItemDetailUseCase: GetItemDetailUseCase,
    private val updateItemUseCase: UpdateItemUseCase,
    val guid: String,
) : BaseProcessor<ItemDetailViewData, ItemDetailEvent>() {

    private val itemDetail = MutableStateFlow<Result<Unit, Item>?>(null)
    private val nameEdit = editTextComponent()
    private val descEdit = editTextComponent()

    init {
        launch {
            val result = getItemDetailUseCase(guid)
            result.okOrNull()?.let {
                nameEdit.value.onTextChanged(it.name)
                descEdit.value.onTextChanged(it.desc)
            }
            itemDetail.value = result
        }
    }

    override val viewData: CommonStateFlow<ItemDetailViewData> =
        map(itemDetail) {
            if (it == null) {
                ItemDetailViewData.Loading
            } else {
                it.fold({
                    ItemDetailViewData.Error
                }, {
                    ItemDetailViewData.ShowItem(
                        nameEdit,
                        descEdit,
                        ButtonViewData(
                            "Save".desc()
                        ) {
                            onSaveClick(
                                it,
                                nameEdit.currentValue.text,
                                descEdit.currentValue.text
                            )
                        }
                    )
                })
            }
        }.asCommonStateFlow()

    private fun onSaveClick(
        item: Item,
        name: String,
        desc: String,
    ) {
        launch {
            updateItemUseCase(
                ItemUpdate(
                    item,
                    name,
                    desc,
                )
            ).fold(
                { print("error saving item") },
                { sendEvent(ItemDetailEvent.Back) }
            )
        }
    }
}
