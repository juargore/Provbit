package ai.bright.provbit.demo.presentation.components

import ai.bright.provbit.theme.ProvbitButtonType
import dev.icerock.moko.resources.desc.StringDesc

data class ButtonViewData(
    val content: StringDesc,
    val style: ProvbitButtonType = ProvbitButtonType.Primary,
    val onClick: () -> Unit
)
