package org.xebia.spdmanager.ui.components.system

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography as AppTypography
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.model.system.vControl.Bank
import org.xebia.spdmanager.model.system.vControl.KnobCC
import org.xebia.spdmanager.model.system.vControl.VControlMode
import org.xebia.spdmanager.model.system.vControl.VisualControl
import org.xebia.spdmanager.ui.components.common.DropdownSelector

@Composable
fun VisualControlView(
    visualControl: VisualControl,
    onUpdate: (VisualControl) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(Spacing.xxl)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Visual Control: ", style = AppTypography.title, color = ColorTextPrimary)
            Switch(
                checked = visualControl.visualControlSwitch == SyncSwitch.ON,
                onCheckedChange = { isChecked ->
                    onUpdate(
                        visualControl.copy(
                            visualControlSwitch = if (isChecked) SyncSwitch.ON else SyncSwitch.OFF
                        )
                    )
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = ColorAccentOrange,
                    uncheckedTrackColor = ColorDivider,
                    checkedThumbColor = ColorBackground
                )
            )
        }

        DropdownSelector(
            label = "Control Mode",
            selectedItem = visualControl.vControlMode,
            onItemSelected = { mode ->
                onUpdate(visualControl.copy(vControlMode = mode))
            },
            items = VControlMode.entries.toList()
        )

        DropdownSelector(
            label = "Bank",
            selectedItem = visualControl.bank,
            onItemSelected = { bank ->
                onUpdate(visualControl.copy(bank = bank))
            },
            items = listOf(
                Bank.Off,
                *List(128) { Bank.BankNumber(it) }.toTypedArray()
            )
        )

        DropdownSelector(
            label = "MIDI Channel",
            selectedItem = visualControl.ch,
            onItemSelected = { channel ->
                onUpdate(visualControl.copy(ch = channel))
            },
            items = (0..16).toList()
        )

        DropdownSelector(
            label = "KnobCC1",
            selectedItem = visualControl.ctrlKnob1CC,
            onItemSelected = { knobCC ->
                onUpdate(visualControl.copy(ctrlKnob1CC = knobCC))
            },
            items = listOf(
                KnobCC.Off,
                *List(128) { KnobCC.KnobCcNumber(it) }.toTypedArray()
            )
        )

        DropdownSelector(
            label = "KnobCC2",
            selectedItem = visualControl.ctrlKnob2CC,
            onItemSelected = { knobCC ->
                onUpdate(visualControl.copy(ctrlKnob2CC = knobCC))
            },
            items = listOf(
                KnobCC.Off,
                *List(128) { KnobCC.KnobCcNumber(it) }.toTypedArray()
            )
        )
    }
}