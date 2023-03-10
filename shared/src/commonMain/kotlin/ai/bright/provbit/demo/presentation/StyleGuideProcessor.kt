package ai.bright.provbit.demo.presentation

import ai.bright.provbit.MR
import ai.bright.provbit.architecture.BaseProcessor
import ai.bright.provbit.architecture.CommonStateFlow
import ai.bright.provbit.demo.presentation.components.ButtonViewData
import ai.bright.provbit.theme.ProvbitButtonType
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import me.tatarka.inject.annotations.Inject

@Inject
class StyleGuideProcessor : BaseProcessor<StyleGuideViewData, StyleGuideEvent>() {
    override val viewData = CommonStateFlow.singleton(StyleGuideViewData())
}

sealed class StyleGuideEvent

data class StyleGuideViewData(
    val headerButtonSection: StringDesc = MR.strings.styleguide_button_section_title.desc(),
    val headerTextSection: StringDesc = MR.strings.styleguide_text_section_title.desc(),
    val textLabelH1: StringDesc = MR.strings.styleguide_text_headline_text.desc(),
    val textLabelH2: StringDesc = MR.strings.styleguide_text_headline_text2.desc(),
    val textLabelPlain: StringDesc = MR.strings.styleguide_text_plain.desc(),
    val primaryButton: ButtonViewData = ButtonViewData(
        content = MR.strings.styleguide_button_primary_label.desc(),
        style = ProvbitButtonType.Primary,
        onClick = {},
    ),
    val secondaryButton: ButtonViewData = ButtonViewData(
        content = MR.strings.styleguide_button_secondary_label.desc(),
        style = ProvbitButtonType.Secondary,
        onClick = {},
    ),
    val tertiaryButton: ButtonViewData = ButtonViewData(
        content = MR.strings.styleguide_button_tertiary_label.desc(),
        style = ProvbitButtonType.Tertiary,
        onClick = {},
    ),
)