package ai.bright.provbit.theme

import dev.icerock.moko.resources.ColorResource

sealed class ProvbitButtonType {
    object Primary: ProvbitButtonType()
    object Secondary: ProvbitButtonType()
    object Tertiary: ProvbitButtonType()
}

sealed class ProvbitTextType {
    object Button: ProvbitTextType()
    object Headline1: ProvbitTextType()
    object Headline2: ProvbitTextType()
    object Plain: ProvbitTextType()
}

data class ProvbitTheme (
    val buttonPrimaryTextColor: ColorResource.Themed,
    val buttonSecondaryTextColor: ColorResource.Themed,
    val primaryColor: ColorResource.Themed,
    val secondaryColor: ColorResource.Themed,
)
