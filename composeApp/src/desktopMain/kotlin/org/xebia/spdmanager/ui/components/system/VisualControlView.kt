package org.xebia.spdmanager.ui.components.system

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.model.system.vControl.Bank
import org.xebia.spdmanager.model.system.vControl.KnobCC
import org.xebia.spdmanager.model.system.vControl.VControlMode
import org.xebia.spdmanager.model.system.vControl.VisualControl
import org.xebia.spdmanager.ui.components.setup.DropdownWithLabel

@Composable
fun VisualControlView(visualControl: VisualControl, onVisualControlChanged: (VisualControl) -> Unit) {
    var selectedVisualControlSwitch by remember { mutableStateOf(visualControl.visualControlSwitch) }
    var selectedVControlMode by remember { mutableStateOf(visualControl.vControlMode) }
    var selectedBank by remember { mutableStateOf(visualControl.bank) }
    var selectedChannel by remember { mutableStateOf(visualControl.ch) }
    var selectedCtrlKnob1CC by remember { mutableStateOf(visualControl.ctrlKnob1CC) }
    var selectedCtrlKnob2CC by remember { mutableStateOf(visualControl.ctrlKnob2CC) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Visual Control: ")
            Switch(
                checked = selectedVisualControlSwitch == SyncSwitch.ON,
                onCheckedChange = {
                    selectedVisualControlSwitch = if (it) SyncSwitch.ON else SyncSwitch.OFF
                    onVisualControlChanged(visualControl.copy(visualControlSwitch = selectedVisualControlSwitch))
                }
            )
        }

        DropdownWithLabel(
            label = "Control Mode",
            selected = selectedVControlMode,
            onSelected = {
                selectedVControlMode = it
                onVisualControlChanged(visualControl.copy(vControlMode = selectedVControlMode))
            },
            options = VControlMode.entries.toList()
        )

        DropdownWithLabel(
            label = "Bank",
            selected = selectedBank,
            onSelected = {
                selectedBank = it
                onVisualControlChanged(visualControl.copy(bank = selectedBank))
            },
            options = listOf(
                Bank.Off,
                *List(128) { Bank.BankNumber(it) }.toTypedArray()
            ).toList()
        )

        DropdownWithLabel(
            label = "MIDI Channel",
            selected = selectedChannel,
            options = (0..16).toList(),
            onSelected = { selectedChannel = it; onVisualControlChanged(visualControl.copy(ch = it)) }
        )

        DropdownWithLabel(
            label = "KnobCC1",
            selected = selectedCtrlKnob1CC,
            onSelected = {
                selectedCtrlKnob1CC = it
                onVisualControlChanged(visualControl.copy(ctrlKnob1CC = selectedCtrlKnob1CC))
            },
            options = listOf(
                KnobCC.Off,
                *List(128) { KnobCC.KnobCcNumber(it) }.toTypedArray()
            ).toList()
        )

        DropdownWithLabel(
            label = "KnobCC2",
            selected = selectedCtrlKnob2CC,
            onSelected = {
                selectedCtrlKnob2CC = it
                onVisualControlChanged(visualControl.copy(ctrlKnob2CC = selectedCtrlKnob2CC))
            },
            options = listOf(
                KnobCC.Off,
                *List(128) { KnobCC.KnobCcNumber(it) }.toTypedArray()
            ).toList()
        )
    }
}
