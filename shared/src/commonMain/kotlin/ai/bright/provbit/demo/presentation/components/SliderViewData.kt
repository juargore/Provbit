package ai.bright.provbit.demo.presentation.components

import ai.bright.provbit.architecture.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.abs

data class SliderViewData(
    val currentValue: Int,
    val stepValue: Int,
    val range: IntRange,
    val setTo: (Int) -> Unit
)

fun ViewScope.sliderComponent(
    initialValue: Int,
    stepValue: Int,
    range: IntRange,
): CommonStateFlow<SliderViewData> {

    val value = MutableStateFlow(initialValue.coerceIn(range))

    return map(
        value
    ) { currentValue ->
        SliderViewData(
            currentValue,
            stepValue,
            range,
        ) { newValue ->
            if (abs(currentValue - newValue) == stepValue && range.contains(newValue)) {
                value.value = newValue
            }
        }
    }.asCommonStateFlow()

}

