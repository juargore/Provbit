package ai.bright.provbit.demo.domain.uses.theme

import ai.bright.provbit.MR
import ai.bright.provbit.architecture.StateFlowInteractor
import ai.bright.provbit.demo.AppScope
import ai.bright.provbit.theme.ProvbitTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.inject.annotations.Inject

/**
 * Provides a flow that emits when the current theme of the application changes. Currently
 * this only contains a single hard coded theme, but this could be extended to emit multiple
 * themes or change theme based on other conditions.
 */
interface CurrentThemeFlow : StateFlowInteractor<Unit, ProvbitTheme> {

    @Inject
    @AppScope
    class Default : CurrentThemeFlow {
        override fun invoke(input: Unit): StateFlow<ProvbitTheme> = MutableStateFlow(DEFAULT_THEME)
    }
}

val DEFAULT_THEME = ProvbitTheme(
    buttonPrimaryTextColor = MR.colors.buttonPrimaryTextColor,
    buttonSecondaryTextColor = MR.colors.buttonSecondaryTextColor,
    primaryColor = MR.colors.primaryColor,
    secondaryColor = MR.colors.secondaryColor,
)

private val SECONDARY_THEME = ProvbitTheme(
    buttonPrimaryTextColor = MR.colors.theme2buttonPrimaryTextColor,
    buttonSecondaryTextColor = MR.colors.theme2buttonSecondaryTextColor,
    primaryColor = MR.colors.theme2primaryColor,
    secondaryColor = MR.colors.theme2secondaryColor,
)
