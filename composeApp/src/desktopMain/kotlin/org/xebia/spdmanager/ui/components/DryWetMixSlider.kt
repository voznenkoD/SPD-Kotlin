package org.xebia.spdmanager.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*

@Composable
fun DryWetMixSlider(
    label: String,
    value: DryWetMix,
    onValueChange: (DryWetMix) -> Unit
) {
    var sliderValue by remember { mutableStateOf((value.dry + value.wet * 2).toFloat()) }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dry: ${value.dry}%", modifier = Modifier.weight(1f))

            Slider(
                value = sliderValue,
                onValueChange = { newValue ->
                    sliderValue = newValue
                    onValueChange(DryWetMix.fromInt(newValue.toInt()))
                },
                valueRange = 0f..200f,
                modifier = Modifier.weight(3f)
            )

            Text("Wet: ${value.wet}%", modifier = Modifier.weight(1f))
        }
    }
}
