package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.subtypes.TapeEcho
import org.xebia.spdmanager.model.system.fx.subtypes.TapeEchoMode
import org.xebia.spdmanager.ui.components.ButtonRow
import org.xebia.spdmanager.ui.components.SliderWithLabel

@Composable
fun TapeEchoView(tapeEcho: TapeEcho) {
    var selectedMode by remember { mutableStateOf(tapeEcho.mode) }
    var bass by remember { mutableStateOf(tapeEcho.bass) }
    var treble by remember { mutableStateOf(tapeEcho.treble) }
    var headSPan by remember { mutableStateOf(tapeEcho.headSPan) }
    var headMPan by remember { mutableStateOf(tapeEcho.headMPan) }
    var headLPan by remember { mutableStateOf(tapeEcho.headLPan) }
    var tapeDist by remember { mutableStateOf(tapeEcho.tapeDist) }
    var wfRate by remember { mutableStateOf(tapeEcho.wfRate) }
    var wfDepth by remember { mutableStateOf(tapeEcho.wfDepth) }
    var echoLevel by remember { mutableStateOf(tapeEcho.echoLevel) }
    var directLevel by remember { mutableStateOf(tapeEcho.directLevel) }
    var level by remember { mutableStateOf(tapeEcho.level) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Tape Echo", fontSize = 18.sp)

        // Tape Echo Mode Selector (Row of Buttons)
        ButtonRow(
            label = "Mode",
            selectedItem = selectedMode,
            items = TapeEchoMode.entries.toTypedArray(),
            onItemSelected = { selectedMode = it }
        )

        // Bass (-15dB to +15dB)
        SliderWithLabel(
            label = "Bass",
            value = bass,
            onValueChange = { bass = it },
            valueRange = -15f..15f
        )

        // Treble (-15dB to +15dB)
        SliderWithLabel(
            label = "Treble",
            value = treble,
            onValueChange = { treble = it },
            valueRange = -15f..15f
        )

        // Head Pan Sliders (-64 to +63)
        SliderWithLabel(
            label = "Head S Pan",
            value = headSPan.toFloat(),
            onValueChange = { headSPan = it.toInt() },
            valueRange = -64f..63f
        )
        SliderWithLabel(
            label = "Head M Pan",
            value = headMPan.toFloat(),
            onValueChange = { headMPan = it.toInt() },
            valueRange = -64f..63f
        )
        SliderWithLabel(
            label = "Head L Pan",
            value = headLPan.toFloat(),
            onValueChange = { headLPan = it.toInt() },
            valueRange = -64f..63f
        )

        // Tape Distortion (0..5)
        SliderWithLabel(
            label = "Tape Distortion",
            value = tapeDist.toFloat(),
            onValueChange = { tapeDist = it.toInt().coerceIn(0, 5) },
            valueRange = 0f..5f
        )

        // Waveform Rate & Depth (0..127)
        SliderWithLabel(
            label = "Waveform Rate",
            value = wfRate.toFloat(),
            onValueChange = { wfRate = it.toInt().coerceIn(0, 127) },
            valueRange = 0f..127f
        )
        SliderWithLabel(
            label = "Waveform Depth",
            value = wfDepth.toFloat(),
            onValueChange = { wfDepth = it.toInt().coerceIn(0, 127) },
            valueRange = 0f..127f
        )

        // Levels (0..100)
        SliderWithLabel(
            label = "Echo Level",
            value = echoLevel.toFloat(),
            onValueChange = { echoLevel = it.toInt().coerceIn(0, 100) },
            valueRange = 0f..100f
        )
        SliderWithLabel(
            label = "Direct Level",
            value = directLevel.toFloat(),
            onValueChange = { directLevel = it.toInt().coerceIn(0, 100) },
            valueRange = 0f..100f
        )
        SliderWithLabel(
            label = "Level",
            value = level.toFloat(),
            onValueChange = { level = it.toInt().coerceIn(0, 100) },
            valueRange = 0f..100f
        )
    }
}
