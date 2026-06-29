package org.xebia.spdmanager.model.kit

import org.xebia.spdmanager.data.encodeNamePadded
import org.xebia.spdmanager.data.encodeTempo
import org.xebia.spdmanager.data.model.raw.kit.KitPrm
import org.xebia.spdmanager.data.model.raw.kit.PadPrm
import org.xebia.spdmanager.model.kit.pad.PadNumber

/**
 * Source-of-truth defaults for an "initialized" (blank) kit. Modeled on the device's init-kit
 * export but kept device-neutral — no pad references any wave, so the result is valid on any device.
 *
 * Values are declared here as named constants expressed in terms of the raw [KitPrm]/[PadPrm] fields
 * (see Map_Kit.md / Map_Pad.md), so the relationship between each parameter and the device model is
 * explicit and editable in one place. Nothing is read from a `.spd` file at runtime.
 *
 * [create] builds the raw [KitPrm] from these constants and converts it through [Kit.fromValues],
 * reusing the exact same decode path as loading a kit from disk.
 */
object KitInitTemplate {

    // --- Kit-level defaults ---
    const val NAME = "INIT"
    const val SUB_NAME = ""
    const val TEMPO = 120.0          // BPM (encoded as 1540 in the raw Tempo field)
    const val LEVEL = 100            // Kit volume

    /** Pad-link pair (Map_Kit.md LinkPad0 / LinkPad1). */
    val LINK_PAD_0: PadNumber = PadNumber.PAD_1
    val LINK_PAD_1: PadNumber = PadNumber.PAD_2

    // --- FX defaults: both processors on, type 0, all params 0 ---
    const val FX_SW_ON = 1
    const val FX_TYPE = 0
    const val FX2_ASGN = 0
    private const val FX_PARAM = 0

    // --- Per-pad defaults for an empty pad (no main/sub wave); see Map_Pad.md ---
    const val PAD_EMPTY_WAVE = -1
    const val PAD_WV_LEVEL = 100
    const val PAD_WV_PAN = 15
    const val PAD_PLAY_MODE = 0
    const val PAD_OUT_ASGN = 0
    const val PAD_MUTE_GRP = -1     // -1 = Off (Map_Pad.md: Off, 1..9); 0 would mutually-mute all pads
    const val PAD_TEMPO_SYNC = 0
    const val PAD_MIDI_CH = -1
    const val PAD_MIDI_CTRL = 0
    const val PAD_LOOP = 0
    const val PAD_TRIG_TYPE = 0
    const val PAD_GATE_TIME = -1
    const val PAD_DYNAMICS = 1
    const val PAD_VOICE_ASGN = 1
    const val PAD_REVERSE = 0
    const val PAD_SUB_WV_LEVEL = 100
    const val PAD_SUB_WV_PAN = 15

    /** First MIDI note number; pads increment from here in [PadNumber] order (60..74). */
    const val FIRST_NOTE_NUM = 60

    /** A fresh, fully-initialized neutral kit. */
    fun create(): Kit = Kit.fromValues(toRawKitPrm())

    private fun toRawKitPrm(): KitPrm {
        val nm = encodeNamePadded(NAME, Kit.NAME_MAX_LENGTH)
        val snm = encodeNamePadded(SUB_NAME, Kit.SUB_NAME_MAX_LENGTH)
        // One empty pad per PadNumber, MIDI note incrementing from FIRST_NOTE_NUM in enum order.
        val pads = PadNumber.entries.mapIndexed { index, _ -> emptyPad(FIRST_NOTE_NUM + index) }
        return KitPrm(
            Level = LEVEL, Tempo = encodeTempo(TEMPO),
            Nm0 = nm[0], Nm1 = nm[1], Nm2 = nm[2], Nm3 = nm[3],
            Nm4 = nm[4], Nm5 = nm[5], Nm6 = nm[6], Nm7 = nm[7],
            SubNm0 = snm[0], SubNm1 = snm[1], SubNm2 = snm[2], SubNm3 = snm[3],
            SubNm4 = snm[4], SubNm5 = snm[5], SubNm6 = snm[6], SubNm7 = snm[7],
            SubNm8 = snm[8], SubNm9 = snm[9], SubNm10 = snm[10], SubNm11 = snm[11],
            SubNm12 = snm[12], SubNm13 = snm[13], SubNm14 = snm[14], SubNm15 = snm[15],
            Fx2Asgn = FX2_ASGN,
            LinkPad0 = LINK_PAD_0.value, LinkPad1 = LINK_PAD_1.value,
            Fx1Sw = FX_SW_ON, Fx1Type = FX_TYPE,
            Fx1Prm0 = FX_PARAM, Fx1Prm1 = FX_PARAM, Fx1Prm2 = FX_PARAM, Fx1Prm3 = FX_PARAM,
            Fx1Prm4 = FX_PARAM, Fx1Prm5 = FX_PARAM, Fx1Prm6 = FX_PARAM, Fx1Prm7 = FX_PARAM,
            Fx1Prm8 = FX_PARAM, Fx1Prm9 = FX_PARAM, Fx1Prm10 = FX_PARAM, Fx1Prm11 = FX_PARAM,
            Fx1Prm12 = FX_PARAM, Fx1Prm13 = FX_PARAM, Fx1Prm14 = FX_PARAM, Fx1Prm15 = FX_PARAM,
            Fx1Prm16 = FX_PARAM, Fx1Prm17 = FX_PARAM, Fx1Prm18 = FX_PARAM, Fx1Prm19 = FX_PARAM,
            Fx2Sw = FX_SW_ON, Fx2Type = FX_TYPE,
            Fx2Prm0 = FX_PARAM, Fx2Prm1 = FX_PARAM, Fx2Prm2 = FX_PARAM, Fx2Prm3 = FX_PARAM,
            Fx2Prm4 = FX_PARAM, Fx2Prm5 = FX_PARAM, Fx2Prm6 = FX_PARAM, Fx2Prm7 = FX_PARAM,
            Fx2Prm8 = FX_PARAM, Fx2Prm9 = FX_PARAM, Fx2Prm10 = FX_PARAM, Fx2Prm11 = FX_PARAM,
            Fx2Prm12 = FX_PARAM, Fx2Prm13 = FX_PARAM, Fx2Prm14 = FX_PARAM, Fx2Prm15 = FX_PARAM,
            Fx2Prm16 = FX_PARAM, Fx2Prm17 = FX_PARAM, Fx2Prm18 = FX_PARAM, Fx2Prm19 = FX_PARAM,
            PadPrm = pads
        )
    }

    private fun emptyPad(noteNum: Int) = PadPrm(
        Wv = PAD_EMPTY_WAVE, WvLevel = PAD_WV_LEVEL, WvPan = PAD_WV_PAN,
        PlayMode = PAD_PLAY_MODE, OutAsgn = PAD_OUT_ASGN, MuteGrp = PAD_MUTE_GRP,
        TempoSync = PAD_TEMPO_SYNC, PadMidiCh = PAD_MIDI_CH, NoteNum = noteNum,
        MidiCtrl = PAD_MIDI_CTRL, Loop = PAD_LOOP, TrigType = PAD_TRIG_TYPE,
        GateTime = PAD_GATE_TIME, Dynamics = PAD_DYNAMICS, VoiceAsgn = PAD_VOICE_ASGN,
        Reverse = PAD_REVERSE, SubWv = PAD_EMPTY_WAVE, SubWvLevel = PAD_SUB_WV_LEVEL,
        SubWvPan = PAD_SUB_WV_PAN
    )
}