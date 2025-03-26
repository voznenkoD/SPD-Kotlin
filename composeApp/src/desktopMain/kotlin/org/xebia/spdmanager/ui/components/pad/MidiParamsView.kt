package org.xebia.spdmanager.ui.components.pad

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.model.kit.pad.MidiParams
import org.xebia.spdmanager.model.kit.pad.midi.ExternalControl
import org.xebia.spdmanager.model.kit.pad.midi.Gate
import org.xebia.spdmanager.model.kit.pad.midi.MidiNote
import org.xebia.spdmanager.model.kit.pad.midi.PadCH

@Composable
fun MidiParamsView(midiParams: MidiParams) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        DropdownSelector(
            label = "Pad Channel",
            selectedItem = midiParams.padCH,
            items = getPadCHOptions(),
            onItemSelected = { newPadCH ->
            }
        )

        DropdownSelector(
            label = "MIDI Note",
            selectedItem = midiParams.midiNote,
            items = getMidiNoteOptions(),
            onItemSelected = { newMidiNote ->
            }
        )

        ButtonRow(
            label = "External Control",
            items = ExternalControl.entries.toTypedArray(),
            selectedItem = midiParams.externalControl,
            onItemSelected = { newExternalControl ->
            }
        )

        DropdownSelector(
            label = "Gate",
            selectedItem = midiParams.gate,
            items = getGateOptions(),
            onItemSelected = { newGate ->
            }
        )
    }
}

fun getPadCHOptions(): List<PadCH> {
    return listOf(
        PadCH.Global,
        PadCH.Channel(1),
        PadCH.Channel(2),
        PadCH.Channel(3),
        PadCH.Channel(4),
        PadCH.Channel(5),
        PadCH.Channel(6),
        PadCH.Channel(7),
        PadCH.Channel(8),
        PadCH.Channel(9),
        PadCH.Channel(10),
        PadCH.Channel(11),
        PadCH.Channel(12),
        PadCH.Channel(13),
        PadCH.Channel(14),
        PadCH.Channel(15),
        PadCH.Channel(16)
    )
}

fun getMidiNoteOptions(): List<MidiNote> {
    return listOf(
        MidiNote.Off,
        MidiNote.Channel(0),
        MidiNote.Channel(1),
        MidiNote.Channel(2),
        MidiNote.Channel(3),
        MidiNote.Channel(4),
        MidiNote.Channel(5),
        MidiNote.Channel(6),
        MidiNote.Channel(7),
        MidiNote.Channel(8),
        MidiNote.Channel(9),
        MidiNote.Channel(10),
        MidiNote.Channel(11),
        MidiNote.Channel(12),
        MidiNote.Channel(13),
        MidiNote.Channel(14),
        MidiNote.Channel(15),
    )
}

fun getGateOptions(): List<Gate> {
    return listOf(
        Gate.Off,
        Gate.Alt,
        Gate.GateVal(1),
        Gate.GateVal(2),
        Gate.GateVal(3),
        Gate.GateVal(4),
        Gate.GateVal(5),
        Gate.GateVal(6),
        Gate.GateVal(7),
        Gate.GateVal(8),
        Gate.GateVal(9),
        Gate.GateVal(10),
        Gate.GateVal(11),
        Gate.GateVal(12),
        Gate.GateVal(13),
        Gate.GateVal(14),
        Gate.GateVal(15),
        Gate.GateVal(16)
    )
}
