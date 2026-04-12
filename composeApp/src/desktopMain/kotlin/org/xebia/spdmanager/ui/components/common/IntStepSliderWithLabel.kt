package org.xebia.spdmanager.ui.components.common

import androidx.compose.runtime.Composable

@Composable
fun IntStepSliderWithLabel(
    label: String,
    value: Int,
    range: IntRange,
    step: Int = 1,
    onValueChange: (Int) -> Unit = {}
) {
    KnobControl(
        label = label,
        value = value.toFloat(),
        onValueChange = { onValueChange(it.toInt()) },
        valueRange = range.first.toFloat()..range.last.toFloat(),
        mode = KnobMode.Unipolar,
        steps = (range.last - range.first) / step
    )
}