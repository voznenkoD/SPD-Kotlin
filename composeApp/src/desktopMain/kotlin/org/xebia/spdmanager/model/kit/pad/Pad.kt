package org.xebia.spdmanager.model.kit.pad

import org.xebia.spdmanager.data.model.raw.kit.PadPrm
import org.xebia.spdmanager.model.kit.pad.mode.PadMode
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch

data class Pad(
    val main: Sound,
    val sub: Sound,
    val muteGroup: MuteGroup,
    val tempoSync: SyncSwitch,
    val padMode: PadMode,
    val midiParams: MidiParams,
    val output: PadOutput
){
    companion object {
        fun fromValues(padPrm: PadPrm): Pad {
            val main = Sound.fromValues(padPrm.Wv, padPrm.WvLevel, padPrm.WvPan)
            val sub = Sound.fromValues(padPrm.SubWv, padPrm.SubWvLevel, padPrm.SubWvPan)

            val padMode = PadMode.fromValues(padPrm.PlayMode, padPrm.Loop, padPrm.TrigType,padPrm.Dynamics, padPrm.VoiceAsgn)
            val midiParams = MidiParams.fromValues(padPrm.PadMidiCh, padPrm.NoteNum, padPrm.MidiCtrl, padPrm.GateTime)

            return Pad(
                main = main,
                sub = sub,
                muteGroup = MuteGroup.fromValue(padPrm.MuteGrp),
                tempoSync = SyncSwitch.fromValue(padPrm.TempoSync),
                padMode = padMode,
                midiParams = midiParams,
                output = PadOutput.fromValue(padPrm.OutAsgn)
            )
        }
    }
}
