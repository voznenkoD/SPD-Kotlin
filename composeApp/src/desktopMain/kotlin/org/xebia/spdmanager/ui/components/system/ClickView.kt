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
fun ClickView(
    clickConfig: ClickConfig,
    waves: List<Wave>,
    onUpdate: (ClickConfig) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Sound Group", style = MaterialTheme.typography.bodyLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SoundGroup.entries.forEach { group ->
                Button(
                    onClick = {
                        onUpdate(clickConfig.copy(soundGroup = group))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (clickConfig.soundGroup == group) Color.Gray else Color.LightGray
                    )
                ) {
                    Text(group.name)
                }
            }
        }

        if (clickConfig.soundGroup == SoundGroup.PRESET) {
            DropdownSelector(
                label = "Sound",
                selectedItem = clickConfig.sound,
                onItemSelected = { sound ->
                    onUpdate(clickConfig.copy(sound = sound))
                },
                items = ClickSound.entries.toList()
            )
        } else {
            DropdownSelector(
                label = "Wave",
                selectedItem = waves.firstOrNull { it.number == clickConfig.wave } ?: waves.first(),
                onItemSelected = { wave ->
                    onUpdate(clickConfig.copy(wave = wave.number))
                },
                items = waves,
                content = { wave ->
                    Text("${wave.number} - ${wave.name}")
                }
            )
        }

        DropdownSelector(
            label = "Interval",
            selectedItem = clickConfig.interval,
            onItemSelected = { interval ->
                onUpdate(clickConfig.copy(interval = interval))
            },
            items = Interval.entries.toList()
        )

        ClickPanSlider("Pan", clickConfig.clickPan) { pan ->
            onUpdate(clickConfig.copy(clickPan = pan))
        }

        DropdownSelector(
            label = "Output",
            selectedItem = clickConfig.output,
            onItemSelected = { output ->
                onUpdate(clickConfig.copy(output = output))
            },
            items = Output.entries.toList()
        )

        SliderWithLabel(
            label = "Level",
            value = clickConfig.level.toFloat(),
            onValueChange = { level ->
                onUpdate(clickConfig.copy(level = level.toInt()))
            },
            valueRange = 0f..100f,
        )
    }
}