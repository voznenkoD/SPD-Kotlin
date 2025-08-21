package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.TapeEcho
import org.xebia.spdmanager.model.system.fx.subtypes.TapeEchoMode
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun TapeEchoView(
    tapeEcho: TapeEcho,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Tape Echo", fontSize = 18.sp)

        ButtonRow(
            label = "Mode",
            selectedItem = tapeEcho.mode,
            items = TapeEchoMode.entries.toTypedArray(),
            onItemSelected = { newMode ->
                onFxChange(tapeEcho.copy(mode = newMode))
            }
        )

        SliderWithLabel(
            label = "Bass",
            value = tapeEcho.bass,
            onValueChange = { newBass ->
                onFxChange(tapeEcho.copy(bass = newBass))
            },
            valueRange = -15f..15f
        )

        SliderWithLabel(
            label = "Treble",
            value = tapeEcho.treble,
            onValueChange = { newTreble ->
                onFxChange(tapeEcho.copy(treble = newTreble))
            },
            valueRange = -15f..15f
        )

        SliderWithLabel(
            label = "Head S Pan",
            value = tapeEcho.headSPan.toFloat(),
            onValueChange = { newHeadSPan ->
                onFxChange(tapeEcho.copy(headSPan = newHeadSPan.toInt()))
            },
            valueRange = -64f..63f
        )

        SliderWithLabel(
            label = "Head M Pan",
            value = tapeEcho.headMPan.toFloat(),
            onValueChange = { newHeadMPan ->
                onFxChange(tapeEcho.copy(headMPan = newHeadMPan.toInt()))
            },
            valueRange = -64f..63f
        )

        SliderWithLabel(
            label = "Head L Pan",
            value = tapeEcho.headLPan.toFloat(),
            onValueChange = { newHeadLPan ->
                onFxChange(tapeEcho.copy(headLPan = newHeadLPan.toInt()))
            },
            valueRange = -64f..63f
        )

        SliderWithLabel(
            label = "Tape Distortion",
            value = tapeEcho.tapeDist.toFloat(),
            onValueChange = { newTapeDist ->
                onFxChange(tapeEcho.copy(tapeDist = newTapeDist.toInt().coerceIn(0, 5)))
            },
            valueRange = 0f..5f
        )

        SliderWithLabel(
            label = "Waveform Rate",
            value = tapeEcho.wfRate.toFloat(),
            onValueChange = { newWfRate ->
                onFxChange(tapeEcho.copy(wfRate = newWfRate.toInt().coerceIn(0, 127)))
            },
            valueRange = 0f..127f
        )

        SliderWithLabel(
            label = "Waveform Depth",
            value = tapeEcho.wfDepth.toFloat(),
            onValueChange = { newWfDepth ->
                onFxChange(tapeEcho.copy(wfDepth = newWfDepth.toInt().coerceIn(0, 127)))
            },
            valueRange = 0f..127f
        )

        SliderWithLabel(
            label = "Echo Level",
            value = tapeEcho.echoLevel.toFloat(),
            onValueChange = { newEchoLevel ->
                onFxChange(tapeEcho.copy(echoLevel = newEchoLevel.toInt().coerceIn(0, 100)))
            },
            valueRange = 0f..100f
        )

        SliderWithLabel(
            label = "Direct Level",
            value = tapeEcho.directLevel.toFloat(),
            onValueChange = { newDirectLevel ->
                onFxChange(tapeEcho.copy(directLevel = newDirectLevel.toInt().coerceIn(0, 100)))
            },
            valueRange = 0f..100f
        )

        SliderWithLabel(
            label = "Level",
            value = tapeEcho.level.toFloat(),
            onValueChange = { newLevel ->
                onFxChange(tapeEcho.copy(level = newLevel.toInt().coerceIn(0, 100)))
            },
            valueRange = 0f..100f
        )
    }
}
