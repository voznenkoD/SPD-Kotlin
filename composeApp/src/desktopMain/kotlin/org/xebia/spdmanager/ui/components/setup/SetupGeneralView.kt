package org.xebia.spdmanager.ui.components.setup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.setup.SetupConfig
import org.xebia.spdmanager.model.setup.PadIndication
import org.xebia.spdmanager.model.setup.TempoIndication
import org.xebia.spdmanager.model.setup.DispMode
import org.xebia.spdmanager.model.setup.AutoOff
import org.xebia.spdmanager.model.setup.UsbMidiMode
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch

@Composable
fun SetupGeneralView(setupConfig: SetupConfig, onUpdate: (SetupConfig) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("LCD Settings", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))

        SliderWithLabel(
            label = "LCD Contrast",
            value = setupConfig.lcdContrast.toFloat(),
            valueRange = 1f..10f,
            onValueChange = { onUpdate(setupConfig.copy(lcdContrast = it.toInt())) }
        )

        SliderWithLabel(
            label = "LCD Brightness",
            value = setupConfig.lcdBrightness.toFloat(),
            valueRange = 0f..10f,
            onValueChange = { onUpdate(setupConfig.copy(lcdBrightness = it.toInt())) }
        )

        DropdownWithLabel(
            label = "Pad Indication",
            selected = setupConfig.padIndication,
            options = PadIndication.entries,
            onSelected = { onUpdate(setupConfig.copy(padIndication = it)) }
        )

        DropdownWithLabel(
            label = "Tempo Indication",
            selected = setupConfig.tempoIndication,
            options = TempoIndication.entries,
            onSelected = { onUpdate(setupConfig.copy(tempoIndication = it)) }
        )

        DropdownWithLabel(
            label = "Display Mode",
            selected = setupConfig.dispMode,
            options = DispMode.entries,
            onSelected = { onUpdate(setupConfig.copy(dispMode = it)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Option Section
        Text("Option Settings", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))

        DropdownWithLabel(
            label = "Pad Lock",
            selected = setupConfig.padLock,
            options = SyncSwitch.entries,
            onSelected = { onUpdate(setupConfig.copy(padLock = it)) }
        )

        DropdownWithLabel(
            label = "Auto Power Off",
            selected = setupConfig.autoPowerOff,
            options = AutoOff.entries,
            onSelected = { onUpdate(setupConfig.copy(autoPowerOff = it)) }
        )

        DropdownWithLabel(
            label = "USB Device Mode",
            selected = setupConfig.usbDevMode,
            options = UsbMidiMode.entries,
            onSelected = { onUpdate(setupConfig.copy(usbDevMode = it)) }
        )
    }
}

@Composable
fun SliderWithLabel(label: String, value: Float, valueRange: ClosedFloatingPointRange<Float>, onValueChange: (Float) -> Unit) {
    Column {
        Text("$label: ${value.toInt()}", fontSize = 14.sp)
        Slider(value = value, onValueChange = onValueChange, valueRange = valueRange)
    }
}

@Composable
fun <T> DropdownWithLabel(label: String, selected: T, options: List<T>, onSelected: (T) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label, fontSize = 14.sp)
        Box {
            Button(onClick = { expanded = true }) {
                Text(selected.toString())
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(text = { Text(option.toString()) }, onClick = {
                        onSelected(option)
                        expanded = false
                    })
                }
            }
        }
    }
}
