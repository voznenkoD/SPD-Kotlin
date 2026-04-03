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
) {
    fun toRaw(): PadPrm = PadPrm(
        Wv = main.toRawWave(),
        WvLevel = main.volume,
        WvPan = main.toRawPan(),
        PlayMode = padMode.template.value,
        OutAsgn = output.value,
        MuteGrp = muteGroup.toValue(),
        TempoSync = tempoSync.value,
        PadMidiCh = midiParams.padCH.toValue(),
        NoteNum = midiParams.midiNote.toValue(),
        MidiCtrl = midiParams.externalControl.value,
        Loop = padMode.loop.value,
        TrigType = padMode.trigType.value,
        GateTime = midiParams.gate.toValue(),
        Dynamics = padMode.dynamics.value,
        VoiceAsgn = padMode.polyMono.value,
        Reverse = 0,
        SubWv = sub.toRawWave(),
        SubWvLevel = sub.volume,
        SubWvPan = sub.toRawPan()
    )

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
