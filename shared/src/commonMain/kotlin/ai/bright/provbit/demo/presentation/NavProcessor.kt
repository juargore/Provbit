package ai.bright.provbit.demo.presentation

import ai.bright.provbit.architecture.*
import ai.bright.provbit.architecture.BaseProcessor
import ai.bright.provbit.architecture.combine
import ai.bright.provbit.architecture.invoke
import ai.bright.provbit.demo.domain.uses.theme.CurrentThemeFlow
import ai.bright.provbit.demo.domain.uses.user.ActiveUserFlow
import ai.bright.provbit.demo.domain.uses.user.UserState
import ai.bright.provbit.theme.ProvbitTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

data class NavViewData(
    val state: NavState,
    val theme: ProvbitTheme,
)

sealed class NavState {
    object Splash : NavState()
    object Unauthenticated : NavState()
    object Authenticated : NavState()
}


sealed class NavEvent

@Inject
class NavProcessor(
    private val activeUserFlow: ActiveUserFlow,
    private val currentThemeFlow: CurrentThemeFlow,
) : BaseProcessor<NavViewData, NavEvent>() {

    private val navigationFlow: StateFlow<NavState> = initialCommonStateFlow<NavState>(NavState.Splash) {
        delay(500)
        activeUserFlow()
            .map {
                when (it) {
                    UserState.None -> NavState.Unauthenticated
                    is UserState.Active -> NavState.Authenticated
                }
            }
            .collect { emit(it) }
    }

    override val viewData: CommonStateFlow<NavViewData> = combine(
        navigationFlow,
        currentThemeFlow()
    ) { navState: NavState, provbitTheme: ProvbitTheme ->
        NavViewData(navState, provbitTheme)
    }.asCommonStateFlow()


}
