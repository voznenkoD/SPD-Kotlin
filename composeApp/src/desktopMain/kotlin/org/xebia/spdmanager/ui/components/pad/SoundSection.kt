package org.xebia.spdmanager.ui.components.pad

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.LocalDeviceManager
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.model.kit.pad.Sound
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SoundSection(
    title: String,
    sound: Sound,
    onSoundChange: (Sound) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = Spacing.s)) {
        val waves = LocalDeviceManager.current.device?.waves?.sortedBy { wave: Wave -> wave.number } ?: emptyList()

        val selectedWave = waves.firstOrNull { it.number == sound.wave }

        if (waves.isNotEmpty()) {
            DropdownSelector(
                label = title,
                selectedItem = selectedWave ?: waves.first(),
                onItemSelected = { wave ->
                    onSoundChange(sound.copy(wave = wave.number))
                },
                items = waves,
                content = { wave ->
                    Text("${wave.number} - ${wave.name}")
                },
                width = 300.dp
            )
        } else {
            Text("$title: No waves available")
        }

        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
            SliderWithLabel(
                label = "Volume",
                value = sound.volume.toFloat(),
                onValueChange = { newVolume ->
                    onSoundChange(sound.copy(volume = newVolume.toInt()))
                },
                valueRange = 0f..100f
            )

            PadPanSlider(
                label = "Pan",
                pan = sound.pan,
                onPanChange = { newPan ->
                    onSoundChange(sound.copy(pan = newPan))
                }
            )
        }
    }
}