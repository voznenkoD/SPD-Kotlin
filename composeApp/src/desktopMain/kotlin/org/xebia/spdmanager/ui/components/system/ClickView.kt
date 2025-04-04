package org.xebia.spdmanager.ui.components.system

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.model.system.*
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun ClickView(clickConfig: ClickConfig, waves: List<Wave>) {
    var selectedSoundGroup by remember { mutableStateOf(clickConfig.soundGroup) }
    var selectedSound by remember { mutableStateOf(clickConfig.sound) }
    var selectedWave by remember { mutableStateOf(clickConfig.wave) }
    var selectedInterval by remember { mutableStateOf(clickConfig.interval) }
    var selectedPan by remember { mutableStateOf(clickConfig.clickPan) }
    var selectedOutput by remember { mutableStateOf(clickConfig.output) }
    var level by remember { mutableStateOf(clickConfig.level.toFloat()) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Sound Group", style = MaterialTheme.typography.bodyLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SoundGroup.entries.forEach { group ->
                Button(
                    onClick = { selectedSoundGroup = group },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedSoundGroup == group) Color.Gray else Color.LightGray
                    )
                ) {
                    Text(group.name)
                }
            }
        }

        if (selectedSoundGroup == SoundGroup.PRESET) {
            DropdownSelector(
                label = "Sound",
                selectedItem = selectedSound,
                onItemSelected = { selectedSound = it },
                items = ClickSound.entries.toList()
            )
        } else {
            DropdownSelector(
                label = "Wave",
                selectedItem =  waves.firstOrNull { it.number == selectedWave } ?: waves.first(),
                onItemSelected = { selectedWave = it.number },
                items = waves,
                content = { wave ->
                    Text("${wave.number} - ${wave.name}")
                }
            )
        }

        DropdownSelector(
            label = "Interval",
            selectedItem = selectedInterval,
            onItemSelected = { selectedInterval = it },
            items = Interval.entries.toList()
        )

        ClickPanSlider("Pan", selectedPan) { selectedPan = it }

        DropdownSelector(
            label = "Output",
            selectedItem = selectedOutput,
            onItemSelected = { selectedOutput = it },
            items = Output.entries.toList()
        )

        SliderWithLabel(
            label = "Level",
            value = level,
            onValueChange = { level = it },
            valueRange = 0f..100f,
        )
    }
}