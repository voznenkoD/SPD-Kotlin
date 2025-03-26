package org.xebia.spdmanager.ui.components.setup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.setup.*
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch

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
        Text("MIDI Setup", fontSize = 12.sp, modifier = Modifier.padding(bottom = 5.dp))

        Row(modifier = Modifier.weight(0.5f).fillMaxWidth().padding(5.dp)) {
            DropdownWithLabel(
                label = "MIDI Channel",
                selected = midiCh,
                options = (0..16).toList(),
                onSelected = { midiCh = it; onUpdate(setupConfig.copy(midiCh = it)) }
            )

        }


        DropdownWithLabel(
            label = "MIDI Sync",
            selected = midiSync,
            options = MidiSync.entries,
            onSelected = { midiSync = it; onUpdate(setupConfig.copy(midiSync = it)) }
        )

        DropdownWithLabel(
            label = "Local Control",
            selected = localCtrl,
            options = SyncSwitch.entries,
            onSelected = { localCtrl = it; onUpdate(setupConfig.copy(localCtrl = it)) }
        )

        DropdownWithLabel(
            label = "Soft Thru",
            selected = softThru,
            options = SyncSwitch.entries,
            onSelected = { softThru = it; onUpdate(setupConfig.copy(softThru = it)) }
        )

        DropdownWithLabel(
            label = "USB MIDI Thru",
            selected = usbMIDIThru,
            options = SyncSwitch.entries,
            onSelected = { usbMIDIThru = it; onUpdate(setupConfig.copy(usbMIDIThru = it)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // MIDI Control Section
        Text("MIDI Control", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))

        DropdownWithLabel(
            label = "MIDI PC Control",
            selected = midiPCCtrl,
            options = SyncSwitch.entries,
            onSelected = { midiPCCtrl = it; onUpdate(setupConfig.copy(midiPCCtrl = it)) }
        )

        DropdownWithLabel(
            label = "MIDI CC Control",
            selected = midiCCCtrl,
            options = SyncSwitch.entries,
            onSelected = { midiCCCtrl = it; onUpdate(setupConfig.copy(midiCCCtrl = it)) }
        )

        SliderWithLabel(
            label = "MIDI FX Select CC",
            value = midiFxSelCc.toFloat(),
            valueRange = 0f..95f,
            onValueChange = { midiFxSelCc = it.toInt(); onUpdate(setupConfig.copy(midiFxSelCc = it.toInt())) }
        )

        SliderWithLabel(
            label = "Master FX Control 1 CC",
            value = mstrFxCtrl1Cc.toFloat(),
            valueRange = 0f..95f,
            onValueChange = { mstrFxCtrl1Cc = it.toInt(); onUpdate(setupConfig.copy(mstrFxCtrl1Cc = it.toInt())) }
        )

        SliderWithLabel(
            label = "Master FX Control 2 CC",
            value = mstrFxCtrl2Cc.toFloat(),
            valueRange = 0f..95f,
            onValueChange = { mstrFxCtrl2Cc = it.toInt(); onUpdate(setupConfig.copy(mstrFxCtrl2Cc = it.toInt())) }
        )
    }
}
