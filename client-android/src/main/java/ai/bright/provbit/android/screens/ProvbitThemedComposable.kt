package ai.bright.provbit.android.screens

import ai.bright.provbit.demo.domain.uses.theme.DEFAULT_THEME
import ai.bright.provbit.theme.ProvbitTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

val LocalProvbitTheme = compositionLocalOf { DEFAULT_THEME }

@Composable
fun ProvbitThemed(theme: ProvbitTheme, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalProvbitTheme provides theme) {
        content()
    }
}

