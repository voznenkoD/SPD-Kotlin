package org.xebia.spdmanager.ui.components.common

import androidx.compose.runtime.Composable

@Composable
fun SliderWithLabel(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    bipolar: Boolean = false
) {
    KnobControl(
        label = label,
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        mode = if (bipolar) KnobMode.Bipolar else KnobMode.Unipolar
    )
}