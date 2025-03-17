package org.xebia.spdmanager.model.kit.pad

import org.xebia.spdmanager.model.kit.pad.midi.ExternalControl
import org.xebia.spdmanager.model.kit.pad.midi.Gate
import org.xebia.spdmanager.model.kit.pad.midi.MidiNote
import org.xebia.spdmanager.model.kit.pad.midi.PadCH

data class MidiParams(val padCH: PadCH, val midiNote: MidiNote, val externalControl: ExternalControl, val gate: Gate){
    companion object {
        fun fromValues(
            padCH: Int,
            midiNote: Int,
            externalControl: Int,
            gate: Int
        ): MidiParams {
            return MidiParams(
                PadCH.fromValue(padCH),
                MidiNote.fromValue(midiNote),
                ExternalControl.fromValue(externalControl),
                Gate.fromValue(gate)
            )
        }
    }
}
