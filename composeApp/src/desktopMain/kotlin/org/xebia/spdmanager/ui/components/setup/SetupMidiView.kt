package org.xebia.spdmanager.ui.components.setup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.setup.*
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.IntStepSliderWithLabel
import org.xebia.spdmanager.ui.components.common.SwitchWithLabel

@Composable
fun SetupMidiView(setupConfig: SetupConfig, onUpdate: (SetupConfig) -> Unit) {
    var midiCh by remember { mutableStateOf(setupConfig.midiCh) } // MIDI Setup dropdown (0..16)
    var midiSync by remember { mutableStateOf(setupConfig.midiSync) } // MIDI Setup enum dropdown
    var localCtrl by remember { mutableStateOf(setupConfig.localCtrl) } // MIDI Setup enum dropdown
    var softThru by remember { mutableStateOf(setupConfig.softThru) } // MIDI Setup enum dropdown
    var usbMIDIThru by remember { mutableStateOf(setupConfig.usbMIDIThru) } // MIDI Setup enum dropdown
    var midiPCCtrl by remember { mutableStateOf(setupConfig.midiPCCtrl) } // MIDI CTRL enum dropdown
    var midiCCCtrl by remember { mutableStateOf(setupConfig.midiCCCtrl) } // MIDI CTRL enum dropdown
    var midiFxSelCc by remember { mutableStateOf(setupConfig.midiFxSelCc) } // MIDI CTRL slider (0..95)
    var mstrFxCtrl1Cc by remember { mutableStateOf(setupConfig.mstrFxCtrl1Cc) } // MIDI CTRL slider (0..95)
    var mstrFxCtrl2Cc by remember { mutableStateOf(setupConfig.mstrFxCtrl2Cc) } // MIDI CTRL slider (0..95)

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            DropdownSelector(
                label = "MIDI Channel",
                selectedItem = midiCh,
                items = (0..16).toList(),
                onItemSelected = { midiCh = it; onUpdate(setupConfig.copy(midiCh = it)) }
            )
            DropdownSelector(
                label = "MIDI Sync",
                selectedItem = midiSync,
                items = MidiSync.entries,
                onItemSelected = { midiSync = it; onUpdate(setupConfig.copy(midiSync = it)) }
            )
        }
        Row (
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
        ) {
                SwitchWithLabel("Local Control", localCtrl) {
                    localCtrl = SyncSwitch.fromBoolean(it);
                    onUpdate(setupConfig.copy(localCtrl = SyncSwitch.fromBoolean(it)))
                }

                SwitchWithLabel("Soft Thru", softThru) {
                    softThru = SyncSwitch.fromBoolean(it);
                    onUpdate(setupConfig.copy(softThru = SyncSwitch.fromBoolean(it)))
                }

                SwitchWithLabel("USB MIDI Thru", usbMIDIThru) {
                    usbMIDIThru = SyncSwitch.fromBoolean(it);
                    onUpdate(setupConfig.copy(usbMIDIThru = SyncSwitch.fromBoolean(it)))
                }

            }
        Row(
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
        ) {
            SwitchWithLabel("MIDI PC Control", midiPCCtrl) {
                midiPCCtrl = SyncSwitch.fromBoolean(it);
                onUpdate(setupConfig.copy(midiPCCtrl = SyncSwitch.fromBoolean(it)))
            }
            SwitchWithLabel("MIDI CC Control", midiCCCtrl) {
                midiCCCtrl = SyncSwitch.fromBoolean(it);
                onUpdate(setupConfig.copy(midiCCCtrl = SyncSwitch.fromBoolean(it)))
            }
        }


        IntStepSliderWithLabel(
            label = "MIDI FX Select CC",
            value = midiFxSelCc,
            range = 0..95,
            onValueChange = { midiFxSelCc = it.toInt(); onUpdate(setupConfig.copy(midiFxSelCc = it)) }
        )

        IntStepSliderWithLabel(
            label = "Master FX Control 1 CC",
            value = mstrFxCtrl1Cc,
            range = 0..95,
            onValueChange = { mstrFxCtrl1Cc = it; onUpdate(setupConfig.copy(mstrFxCtrl1Cc = it)) }
        )

        IntStepSliderWithLabel(
            label = "Master FX Control 2 CC",
            value = mstrFxCtrl2Cc,
            range = 0..95,
            onValueChange = { mstrFxCtrl2Cc = it; onUpdate(setupConfig.copy(mstrFxCtrl2Cc = it)) }
        )
    }
}
