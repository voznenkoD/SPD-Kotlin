package org.xebia.spdmanager.ui.components.pad

import androidx.compose.runtime.Composable
import org.xebia.spdmanager.model.kit.pad.PadPan
import org.xebia.spdmanager.ui.components.common.KnobControl
import org.xebia.spdmanager.ui.components.common.KnobMode
import org.xebia.spdmanager.ui.components.common.parsePanInput

@Composable
fun PadPanSlider(label: String, pan: PadPan, onPanChange: (PadPan) -> Unit) {
    KnobControl(
        label = label,
        value = pan.value.toFloat(),
        onValueChange = { onPanChange(PadPan(it.toInt())) },
        valueRange = -15f..15f,
        mode = KnobMode.Bipolar,
        steps = 30,
        valueDisplay = pan.toString(),
        parseInput = ::parsePanInput
    )
}