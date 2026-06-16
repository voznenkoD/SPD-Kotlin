package org.xebia.spdmanager.ui.components.system

import androidx.compose.runtime.Composable
import org.xebia.spdmanager.model.system.ClickPan
import org.xebia.spdmanager.ui.components.common.KnobControl
import org.xebia.spdmanager.ui.components.common.KnobMode
import org.xebia.spdmanager.ui.components.common.parsePanInput

@Composable
fun ClickPanSlider(label: String, pan: ClickPan, onPanChange: (ClickPan) -> Unit) {
    KnobControl(
        label = label,
        value = pan.value.toFloat(),
        onValueChange = { onPanChange(ClickPan(it.toInt())) },
        valueRange = -15f..15f,
        mode = KnobMode.Bipolar,
        steps = 30,
        valueDisplay = pan.toString(),
        parseInput = ::parsePanInput
    )
}