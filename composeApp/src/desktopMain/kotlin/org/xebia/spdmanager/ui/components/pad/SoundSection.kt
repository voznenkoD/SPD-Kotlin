package org.xebia.spdmanager.ui.components.pad

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.LocalDeviceManager
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.model.kit.pad.Sound
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun SoundSection(title: String, sound: Sound) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        var selectedWave = sound.wave
        val waves = LocalDeviceManager.current.device!!.waves.sortedBy { wave: Wave -> wave.number  }

        DropdownSelector(
            label = title,
            selectedItem = waves.firstOrNull { it.number == selectedWave } ?: waves.first(),
            onItemSelected = { selectedWave = it.number },
            items = waves,
            content = { wave ->
                Text("${wave.number} - ${wave.name}")
            },
            width = 300.dp
        )

        SliderWithLabel(
            label = "Volume",
            value = sound.volume.toFloat(),
            onValueChange = { /* Handle volume change */ },
            valueRange = 0f..100f
        )

        PadPanSlider(
            label = "Pan",
            pan = sound.pan,
            onPanChange = { /* Handle pan change */ }
        )
    }
}