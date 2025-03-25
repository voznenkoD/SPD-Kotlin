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
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        // Wave Display
        TextField(
            value = sound.wave.toString(),
            onValueChange = {},
            label = { Text("Wave") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Volume Slider
        SliderWithLabel(
            label = "Volume",
            value = sound.volume.toFloat(),
            onValueChange = { /* Handle volume change */ },
            valueRange = 0f..100f
        )

        // Pan Control (Custom PadPan Slider)
        PadPanSlider(
            label = "Pan",
            pan = sound.pan,
            onPanChange = { /* Handle pan change */ }
        )
    }
}