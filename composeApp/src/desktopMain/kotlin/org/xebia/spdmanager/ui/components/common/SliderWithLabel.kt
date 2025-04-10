package org.xebia.spdmanager.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun SliderWithLabel(label: String, value: Float, onValueChange: (Float) -> Unit, valueRange: ClosedFloatingPointRange<Float>) {
    Column {
        Text("$label: ${value.toInt()}", style = MaterialTheme.typography.bodyLarge)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}