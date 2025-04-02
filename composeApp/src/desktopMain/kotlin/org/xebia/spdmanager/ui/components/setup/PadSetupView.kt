package org.xebia.spdmanager.ui.components.setup

import org.xebia.spdmanager.model.kit.pad.PadNumber

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.setup.*
import org.xebia.spdmanager.model.system.PadFsControl
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.IntStepSliderWithLabel

@Composable
fun PadSetupView(setupConfig: SetupConfig, padNumber: PadNumber, padFsControl: PadFsControl) {
    if (padNumber in PadNumber.PAD_1..PadNumber.PAD_9) {
        setupConfig.intPads.getOrNull(padNumber.ordinal)?.let { IntPadView(it, padFsControl) }
    } else if (padNumber in PadNumber.TRIG_1..PadNumber.TRIG_4) {
        setupConfig.extPads.getOrNull(padNumber.ordinal - PadNumber.TRIG_1.ordinal)
            ?.let { ExtPadView(it, padFsControl) }
    } else if (padNumber == PadNumber.FS_1) {
        FootSwitchView(setupConfig.fs1Polarity, padFsControl)
    } else if (padNumber == PadNumber.FS_2) {
        FootSwitchView(setupConfig.fs2Polarity, padFsControl)
    }
}

@Composable
fun IntPadView(intPad: IntPad, padControl: PadFsControl) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(0.4f).padding(16.dp)) {
            IntStepSliderWithLabel(label = "Sensitivity", value = intPad.sens, range = 1..32)
            IntStepSliderWithLabel(label = "Threshold", value = intPad.threshold, range = 0..31)
            Column(modifier = Modifier.width(200.dp)) {
                DropdownSelector(
                    label = "Curve",
                    selectedItem = intPad.curve,
                    items = VeloCurve.entries,
                    onItemSelected = {})
                DropdownSelector(
                    label = "Control",
                    selectedItem = padControl,
                    items = PadFsControl.entries,
                    onItemSelected = {})
            }
        }
        Column(modifier = Modifier.weight(0.6f).padding(16.dp)) {

        }
    }
}

@Composable
fun ExtPadView(extPad: ExtPad, padControl: PadFsControl) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(0.55f).padding(16.dp)) {
            IntStepSliderWithLabel(label = "Sensitivity", value = extPad.sens, range = 1..32)
            IntStepSliderWithLabel(label = "Threshold", value = extPad.threshold, range = 0..31)
            Column(modifier = Modifier.width(200.dp)) {
                DropdownSelector(
                    label = "Curve",
                    selectedItem = extPad.curve,
                    items = VeloCurve.entries,
                    onItemSelected = {})
                DropdownSelector(
                    label = "Control",
                    selectedItem = padControl,
                    items = PadFsControl.entries,
                    onItemSelected = {})
            }
        }
        Column(modifier = Modifier.weight(0.10f).padding(16.dp)) {
            DropdownSelector(
                label = "Input Mode",
                selectedItem = extPad.inputMode,
                items = InputMode.entries,
                onItemSelected = {})
            DropdownSelector(
                label = "Pad Type",
                selectedItem = extPad.padType,
                items = TrigType.entries,
                onItemSelected = {})
        }
        Column(modifier = Modifier.weight(0.35f).padding(16.dp)) {
            IntStepSliderWithLabel(label = "Scan Time (ms)", value = extPad.scanTime, range = 1..40, step = 1)
            IntStepSliderWithLabel(label = "Retrig Cancel", value = extPad.retrigCxl, range = 1..16)
            IntStepSliderWithLabel(label = "Mask Time", value = extPad.maskTime, range = 1..64)
            IntStepSliderWithLabel(label = "Xtalk Cancel (%)", value = extPad.xtalkCxl, range = 0..80)
        }
    }
}

@Composable
fun FootSwitchView(polarity: FootSwitchPolarity, padControl: PadFsControl) {
    Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.width(200.dp)) {
            DropdownSelector(
                label = "Polarity",
                selectedItem = polarity,
                items = FootSwitchPolarity.entries,
                onItemSelected = {})
            DropdownSelector(
                label = "Control",
                selectedItem = padControl,
                items = PadFsControl.entries,
                onItemSelected = {})
        }
    }
}