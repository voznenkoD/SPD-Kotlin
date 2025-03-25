package org.xebia.spdmanager.ui.components.pad

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.kit.pad.PadPan

@Composable
fun PadPanSlider(label: String, pan: PadPan, onPanChange: (PadPan) -> Unit) {
    var sliderValue by remember { mutableStateOf((pan.value + 15).toFloat()) }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text("$label: ${pan.toString()}", fontSize = 16.sp, fontWeight = FontWeight.Medium)

        Slider(
            value = sliderValue,
            onValueChange = {
                sliderValue = it
                onPanChange(PadPan((it - 15).toInt()))
            },
            valueRange = 0f..30f,
            steps = 29,  // Allows precise adjustments
            modifier = Modifier.fillMaxWidth()
        )

        // Display L (Left), C (Center), and R (Right) indicators
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("L", fontSize = 14.sp)
            Text("C", fontSize = 14.sp)
            Text("R", fontSize = 14.sp)
        }
    }
}
