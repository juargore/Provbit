package ai.bright.provbit.demo.presentation

import ai.bright.provbit.architecture.*
import ai.bright.provbit.demo.domain.uses.items.*
import ai.bright.provbit.demo.entities.Item
import ai.bright.provbit.demo.presentation.components.EditTextViewData
import ai.bright.provbit.demo.presentation.components.editTextComponent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

data class ItemListViewData(
    val searchText: CommonStateFlow<EditTextViewData>,
    var itemList: CommonStateFlow<ItemListContent>,
    var onAdd: () -> Unit,
    var onDelete: (Item) -> Unit
)

data class ItemListContent(
    var items: List<ItemViewData>
)

data class ItemViewData(
    val item: Item,
    val name: String,
    val desc: String,
    val onClick: () -> Unit,
) {
    constructor(item: Item, onClick: () -> Unit) : this(
        item = item,
        item.name,
        item.desc,
        onClick,
    )

    val getId: String
        get() = item.guid
}

sealed class ItemListEvent {
    data class ToDetail(val itemGuid: String) : ItemListEvent()
}

@Inject
class ItemListProcessor(
    private val addItemUseCase: AddItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val queryItemsUseCase: QueryItemsUseCase
) : BaseProcessor<ItemListViewData, ItemListEvent>() {

    private val items = MutableStateFlow(ItemListContent(emptyList()))
    private val filterText = editTextComponent()

    private var queryUseCaseJob: Job = Job().apply { complete() }

    private fun onItemSelected(item: Item) { sendEvent(ItemListEvent.ToDetail(item.guid)) }

    private fun onItemAdd() {
        launch {
            addItemUseCase(AddItem.Add)
        }
    }

    private fun onItemDelete(itemToDelete: Item) {
        launch {
            deleteItemUseCase(DeleteItem.Delete(itemToDelete))
        }
    }

    /*
    This method will listen for changes to the repository.
     */
    private fun observeQueryChanges() {
        queryUseCaseJob.cancel()
        queryUseCaseJob = launch {
            queryItemsUseCase(
                ItemQuery.All
            ).collect { listReturn ->
                items.value = ItemListContent(listReturn.map { ItemViewData(it) { onItemSelected(it) } })
            }
        }
    }

    override val viewData: CommonStateFlow<ItemListViewData> =
        CommonStateFlow.singleton(
            ItemListViewData(
                searchText = filterText,
                itemList = items.asCommonStateFlow(),
                onAdd =  this::onItemAdd,
                onDelete =  this::onItemDelete
            ))


    init {
        filterText
            .onEach { editText ->
                val searchQueryText = editText.text.trim()
                queryUseCaseJob.cancel()
                queryUseCaseJob = launch {
                    queryItemsUseCase(
                        if (searchQueryText.isEmpty())
                            ItemQuery.All
                        else
                            ItemQuery.NameContains(searchQueryText)
                    ).collect { listReturn ->
                        items.value = ItemListContent(listReturn.map { ItemViewData(it) { onItemSelected(it) } })
                    }
                }
            }.launchIn(this)

        observeQueryChanges()
    }
}
