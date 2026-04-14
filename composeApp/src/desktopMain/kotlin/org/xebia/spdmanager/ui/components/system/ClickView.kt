package org.xebia.spdmanager.ui.components.system

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.ColorSurface
import org.xebia.spdmanager.ui.theme.ColorSurfaceSelected
import org.xebia.spdmanager.ui.theme.ColorTextOnDark
import org.xebia.spdmanager.ui.theme.ColorTextPrimary
import org.xebia.spdmanager.ui.theme.Spacing
import org.xebia.spdmanager.ui.theme.Typography as AppTypography
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.model.system.*
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClickView(
    clickConfig: ClickConfig,
    waves: List<Wave>,
    onUpdate: (ClickConfig) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(Spacing.xxl)) {
        Text("Sound Group", style = AppTypography.title, color = ColorTextPrimary)
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
            SoundGroup.entries.forEach { group ->
                Button(
                    onClick = {
                        onUpdate(clickConfig.copy(soundGroup = group))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (clickConfig.soundGroup == group) ColorSurfaceSelected else ColorSurface
                    )
                ) {
                    Text(group.name, color = if (clickConfig.soundGroup == group) ColorTextOnDark else ColorTextPrimary)
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

        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
            ClickPanSlider("Pan", clickConfig.clickPan) { pan ->
                onUpdate(clickConfig.copy(clickPan = pan))
            }

            SliderWithLabel(
                label = "Level",
                value = clickConfig.level.toFloat(),
                onValueChange = { level ->
                    onUpdate(clickConfig.copy(level = level.toInt()))
                },
                valueRange = 0f..100f,
            )
        }

        DropdownSelector(
            label = "Output",
            selectedItem = clickConfig.output,
            onItemSelected = { output ->
                onUpdate(clickConfig.copy(output = output))
            },
            items = Output.entries.toList()
        )
    }
}