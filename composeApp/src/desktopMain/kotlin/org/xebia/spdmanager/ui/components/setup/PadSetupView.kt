package org.xebia.spdmanager.ui.components.setup

import org.xebia.spdmanager.model.kit.pad.PadNumber

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.model.setup.*
import org.xebia.spdmanager.model.system.PadFsControl
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.IntStepSliderWithLabel

@Composable
fun PadSetupView(
    setupConfig: SetupConfig,
    padNumber: PadNumber,
    padFsControl: PadFsControl,
    onUpdateSetupConfig: (SetupConfig) -> Unit,
    onUpdatePadControl: (PadFsControl) -> Unit
) {
    when (padNumber) {
        PadNumber.PAD_1,
        PadNumber.PAD_2,
        PadNumber.PAD_3,
        PadNumber.PAD_4,
        PadNumber.PAD_5,
        PadNumber.PAD_6,
        PadNumber.PAD_7,
        PadNumber.PAD_8,
        PadNumber.PAD_9 -> {
            val intPadIndex = padNumber.ordinal

            setupConfig.intPads.getOrNull(intPadIndex)?.let { intPad ->
                IntPadView(
                    intPad = intPad,
                    padControl = padFsControl,
                    onUpdateIntPad = { updatedPad ->
                        val updatedPads = setupConfig.intPads.toMutableList()
                        updatedPads[intPadIndex] = updatedPad
                        onUpdateSetupConfig(setupConfig.copy(intPads = updatedPads))
                    },
                    onUpdatePadControl = onUpdatePadControl
                )
            }
        }
        PadNumber.TRIG_1,
        PadNumber.TRIG_2,
        PadNumber.TRIG_3,
        PadNumber.TRIG_4 -> {
            val extPadIndex = padNumber.ordinal - PadNumber.TRIG_1.ordinal

            setupConfig.extPads.getOrNull(extPadIndex)?.let { extPad ->
                ExtPadView(
                    extPad = extPad,
                    padControl = padFsControl,
                    onUpdateExtPad = { updatedPad ->
                        val updatedPads = setupConfig.extPads.toMutableList()
                        updatedPads[extPadIndex] = updatedPad
                        onUpdateSetupConfig(setupConfig.copy(extPads = updatedPads))
                    },
                    onUpdatePadControl = onUpdatePadControl
                )
            }
        }
        PadNumber.FS_1 -> {
            FootSwitchView(
                polarity = setupConfig.fs1Polarity,
                padControl = padFsControl,
                onUpdatePolarity = { polarity ->
                    onUpdateSetupConfig(setupConfig.copy(fs1Polarity = polarity))
                },
                onUpdatePadControl = onUpdatePadControl
            )
        }
        PadNumber.FS_2 -> {
            FootSwitchView(
                polarity = setupConfig.fs2Polarity,
                padControl = padFsControl,
                onUpdatePolarity = { polarity ->
                    onUpdateSetupConfig(setupConfig.copy(fs2Polarity = polarity))
                },
                onUpdatePadControl = onUpdatePadControl
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IntPadView(
    intPad: IntPad,
    padControl: PadFsControl,
    onUpdateIntPad: (IntPad) -> Unit,
    onUpdatePadControl: (PadFsControl) -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(0.2f).padding(Spacing.xxl)) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
                IntStepSliderWithLabel(
                    label = "Sensitivity",
                    value = intPad.sens,
                    range = 1..32,
                    onValueChange = { newSens ->
                        onUpdateIntPad(intPad.copy(sens = newSens))
                    }
                )

                IntStepSliderWithLabel(
                    label = "Threshold",
                    value = intPad.threshold,
                    range = 0..31,
                    onValueChange = { newThreshold ->
                        onUpdateIntPad(intPad.copy(threshold = newThreshold))
                    }
                )
            }
        }

        Column(modifier = Modifier.weight(0.2f).padding(Spacing.xxl)) {
                DropdownSelector(
                    label = "Curve",
                    selectedItem = intPad.curve,
                    items = VeloCurve.entries,
                    onItemSelected = { newCurve ->
                        onUpdateIntPad(intPad.copy(curve = newCurve))
                    }
                )

                DropdownSelector(
                    label = "Control",
                    selectedItem = padControl,
                    items = PadFsControl.entries,
                    onItemSelected = { newControl ->
                        onUpdatePadControl(newControl)
                    }
                )
        }
        Column(modifier = Modifier.weight(0.6f).padding(Spacing.xxl)) {
            // Additional controls can be added here if needed
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExtPadView(
    extPad: ExtPad,
    padControl: PadFsControl,
    onUpdateExtPad: (ExtPad) -> Unit,
    onUpdatePadControl: (PadFsControl) -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(0.2f).padding(Spacing.xxl)) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
                IntStepSliderWithLabel(
                    label = "Sensitivity",
                    value = extPad.sens,
                    range = 1..32,
                    onValueChange = { newSens ->
                        onUpdateExtPad(extPad.copy(sens = newSens))
                    }
                )

                IntStepSliderWithLabel(
                    label = "Threshold",
                    value = extPad.threshold,
                    range = 0..31,
                    onValueChange = { newThreshold ->
                        onUpdateExtPad(extPad.copy(threshold = newThreshold))
                    }
                )
            }
        }

        Column(modifier = Modifier.weight(0.35f).padding(Spacing.xxl)) {
            DropdownSelector(
                label = "Curve",
                selectedItem = extPad.curve,
                items = VeloCurve.entries,
                onItemSelected = { newCurve ->
                    onUpdateExtPad(extPad.copy(curve = newCurve))
                }
            )

            DropdownSelector(
                label = "Control",
                selectedItem = padControl,
                items = PadFsControl.entries,
                onItemSelected = { newControl ->
                    onUpdatePadControl(newControl)
                }
            )
        }

        Column(modifier = Modifier.weight(0.10f).padding(Spacing.xxl)) {
            DropdownSelector(
                label = "Input Mode",
                selectedItem = extPad.inputMode,
                items = InputMode.entries,
                onItemSelected = { newMode ->
                    onUpdateExtPad(extPad.copy(inputMode = newMode))
                }
            )

            DropdownSelector(
                label = "Pad Type",
                selectedItem = extPad.padType,
                items = TrigType.entries,
                onItemSelected = { newType ->
                    onUpdateExtPad(extPad.copy(padType = newType))
                }
            )
        }

        Column(modifier = Modifier.weight(0.35f).padding(Spacing.xxl)) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
                IntStepSliderWithLabel(
                    label = "Scan Time (ms)",
                    value = extPad.scanTime,
                    range = 1..40,
                    step = 1,
                    onValueChange = { newScanTime ->
                        onUpdateExtPad(extPad.copy(scanTime = newScanTime))
                    }
                )

                IntStepSliderWithLabel(
                    label = "Retrig Cancel",
                    value = extPad.retrigCxl,
                    range = 1..16,
                    onValueChange = { newRetrigCxl ->
                        onUpdateExtPad(extPad.copy(retrigCxl = newRetrigCxl))
                    }
                )

                IntStepSliderWithLabel(
                    label = "Mask Time",
                    value = extPad.maskTime,
                    range = 1..64,
                    onValueChange = { newMaskTime ->
                        onUpdateExtPad(extPad.copy(maskTime = newMaskTime))
                    }
                )

                IntStepSliderWithLabel(
                    label = "Xtalk Cancel (%)",
                    value = extPad.xtalkCxl,
                    range = 0..80,
                    onValueChange = { newXtalkCxl ->
                        onUpdateExtPad(extPad.copy(xtalkCxl = newXtalkCxl))
                    }
                )
            }
        }
    }
}

@Composable
fun FootSwitchView(
    polarity: FootSwitchPolarity,
    padControl: PadFsControl,
    onUpdatePolarity: (FootSwitchPolarity) -> Unit,
    onUpdatePadControl: (PadFsControl) -> Unit
) {
    Row(modifier = Modifier.fillMaxSize().padding(Spacing.xxl)) {
        Column(modifier = Modifier.width(200.dp)) {
            DropdownSelector(
                label = "Polarity",
                selectedItem = polarity,
                items = FootSwitchPolarity.entries,
                onItemSelected = { newPolarity ->
                    onUpdatePolarity(newPolarity)
                }
            )

            DropdownSelector(
                label = "Control",
                selectedItem = padControl,
                items = PadFsControl.entries,
                onItemSelected = { newControl ->
                    onUpdatePadControl(newControl)
                }
            )
        }
    }
}