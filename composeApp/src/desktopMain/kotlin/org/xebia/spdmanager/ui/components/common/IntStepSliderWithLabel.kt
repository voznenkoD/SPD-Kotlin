package org.xebia.spdmanager.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*

@Composable
fun IntStepSliderWithLabel(
    label: String,
    value: Int,
    range: IntRange,
    step: Int = 1,
    onValueChange: (Int) -> Unit = {}
) {
    Column {
        Text("$label: $value", style = MaterialTheme.typography.bodyLarge)
        Slider(
            value = value.toFloat(),
            onValueChange = { newValue ->
                onValueChange(newValue.toInt())
            },
            valueRange = range.first.toFloat()..range.last.toFloat(),
            steps = (range.last - range.first) / step - 1
        )
    }
}