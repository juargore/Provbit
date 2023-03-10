package ai.bright.provbit.demo.presentation.components

import ai.bright.provbit.architecture.*
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.MutableStateFlow

data class ToggleViewData(
    val state: Boolean,
    val label: StringDesc? = null,
    val set: (Boolean) -> Unit
)

fun ViewScope.toggleComponent(
    initialState: Boolean,
    label: StringDesc? = null
): CommonStateFlow<ToggleViewData> {

    val state = MutableStateFlow(initialState)

    return map(
        state
    ) { current ->
        ToggleViewData(
            state = current,
            label = label,
            set = { state.value = it}
        )
    }.asCommonStateFlow()
}