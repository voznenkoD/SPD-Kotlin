package org.xebia.spdmanager.ui.components.setup

import org.xebia.spdmanager.model.kit.pad.PadNumber

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.setup.*
import org.xebia.spdmanager.model.system.PadFsControl

@Composable
fun PadSetupView(setupConfig: SetupConfig, padNumber: PadNumber, padFsControl: PadFsControl) {
    if (padNumber in PadNumber.PAD_1..PadNumber.PAD_9) {
        setupConfig.intPads.getOrNull(padNumber.ordinal)?.let { IntPadView(it, padFsControl) }
    } else if (padNumber in PadNumber.TRIG_1..PadNumber.TRIG_4) {
        setupConfig.extPads.getOrNull(padNumber.ordinal - PadNumber.TRIG_1.ordinal)?.let { ExtPadView(it, padFsControl) }
    } else if (padNumber == PadNumber.FS_1) {
        FootSwitchView(setupConfig.fs1Polarity, padFsControl)
    } else if (padNumber == PadNumber.FS_2) {
        FootSwitchView(setupConfig.fs2Polarity, padFsControl)
    }
}

@Composable
fun IntPadView(intPad: IntPad, padControl: PadFsControl) {
    Row (modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.weight(0.4f).padding(16.dp)) {
            SliderWithLabel(label = "Sensitivity", value = intPad.sens, range = 1..32)
            SliderWithLabel(label = "Threshold", value = intPad.threshold, range = 0..31)
            EnumDropdown(label = "Curve", selected = intPad.curve, values = VeloCurve.entries.toTypedArray())
            EnumDropdown(label = "Control", selected = padControl, values = PadFsControl.entries.toTypedArray())
        }
        Column(modifier = Modifier.weight(0.6f).padding(16.dp)) {

        }
    }
}

@Composable
fun ExtPadView(extPad: ExtPad, padControl: PadFsControl) {
    Row (modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.weight(0.4f).padding(16.dp)) {
            SliderWithLabel(label = "Sensitivity", value = extPad.sens, range = 1..32)
            SliderWithLabel(label = "Threshold", value = extPad.threshold, range = 0..31)
            EnumDropdown(label = "Curve", selected = extPad.curve, values = VeloCurve.entries.toTypedArray())
            EnumDropdown(label = "Control", selected = padControl, values = PadFsControl.entries.toTypedArray())
        }
        Column(modifier = Modifier.weight(0.15f).padding(16.dp)) {
            EnumDropdown(label = "Input Mode", selected = extPad.inputMode, values = InputMode.entries.toTypedArray())
            EnumDropdown(label = "Pad Type", selected = extPad.padType, values = TrigType.entries.toTypedArray())
        }
        Column(modifier = Modifier.weight(0.45f).padding(16.dp)) {
            SliderWithLabel(label = "Scan Time (ms)", value = extPad.scanTime, range = 1..40, step = 1)
            SliderWithLabel(label = "Retrig Cancel", value = extPad.retrigCxl, range = 1..16)
            SliderWithLabel(label = "Mask Time", value = extPad.maskTime, range = 1..64)
            SliderWithLabel(label = "Xtalk Cancel (%)", value = extPad.xtalkCxl, range = 0..80)
        }
    }
}

@Composable
fun FootSwitchView(polarity: FootSwitchPolarity, padControl: PadFsControl) {
    Row (modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.weight(0.4f).padding(16.dp)) {
            EnumDropdown(label = "Polarity", selected = polarity, values = FootSwitchPolarity.entries.toTypedArray())
            EnumDropdown(label = "Control", selected = padControl, values = PadFsControl.entries.toTypedArray())
        }
        Column(modifier = Modifier.weight(0.6f).padding(16.dp)) {
        }
    }
}

@Composable
fun <T : Enum<T>> EnumDropdown(label: String, selected: T, values: Array<T>, onSelected: (T) -> Unit = {}) {
    var expanded by remember { mutableStateOf(false) }
    var selectedValue by remember { mutableStateOf(selected) }

    Column {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Box {
            Button(onClick = { expanded = true }) {
                Text(selectedValue.name)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                values.forEach { value ->
                    DropdownMenuItem(text = { Text(value.name) }, onClick = {
                        selectedValue = value
                        onSelected(value)
                        expanded = false
                    })
                }
            }
        }
    }
}

@Composable
fun SliderWithLabel(label: String, value: Int, range: IntRange, step: Int = 1, onValueChange: (Int) -> Unit = {}) {
    var sliderValue by remember { mutableStateOf(value.toFloat()) }

    Column {
        Text("$label: ${sliderValue.toInt()}", style = MaterialTheme.typography.bodyLarge)
        Slider(
            value = sliderValue,
            onValueChange = { newValue ->
                sliderValue = newValue
                onValueChange(newValue.toInt())
            },
            valueRange = range.first.toFloat()..range.last.toFloat(),
            steps = (range.last - range.first) / step - 1
        )
    }
}
