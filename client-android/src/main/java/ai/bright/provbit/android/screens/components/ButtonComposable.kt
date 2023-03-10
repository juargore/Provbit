package ai.bright.provbit.android.screens.components

import ai.bright.provbit.android.extensions.localized
import ai.bright.provbit.android.screens.LocalProvbitTheme
import ai.bright.provbit.demo.presentation.components.ButtonViewData
import ai.bright.provbit.theme.ProvbitButtonType
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ButtonComposable(
    state: ButtonViewData,
    modifier: Modifier = Modifier,
) {
    val buttonColors: ButtonColors
    val textColor: Color
    val elevation: ButtonElevation?
    when (state.style) {
        ProvbitButtonType.Primary -> {
            elevation = ButtonDefaults.elevation()
            textColor = LocalProvbitTheme.current.buttonPrimaryTextColor.localized()
            buttonColors = ButtonDefaults.buttonColors(
                backgroundColor = LocalProvbitTheme.current.primaryColor.localized()
            )
        }
        ProvbitButtonType.Secondary -> {
            elevation = ButtonDefaults.elevation()
            textColor = LocalProvbitTheme.current.buttonSecondaryTextColor.localized()
            buttonColors = ButtonDefaults.buttonColors(
                backgroundColor = LocalProvbitTheme.current.secondaryColor.localized()
            )
        }
        ProvbitButtonType.Tertiary -> {
            elevation = null
            textColor = LocalProvbitTheme.current.buttonSecondaryTextColor.localized()
            buttonColors = ButtonDefaults.buttonColors(
                backgroundColor = LocalProvbitTheme.current.secondaryColor.localized()
            )
        }
    }
    Button(modifier = modifier, onClick = state.onClick, colors = buttonColors, elevation = elevation) {
        Text(text = state.content.localized(), color = textColor)
    }
}