package org.xebia.spdmanager.ui.components.pad

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.kit.pad.Sound
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun SoundSection(title: String, sound: Sound) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Text(title + "  " + sound.wave.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold)

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