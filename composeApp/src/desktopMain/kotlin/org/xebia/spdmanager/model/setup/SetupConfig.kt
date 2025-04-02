package org.xebia.spdmanager.model.setup

import org.xebia.spdmanager.data.model.raw.system.SetupPrm
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch

data class SetupConfig(
    val lcdContrast: Int, //LCD section slider 1..10
    val lcdBrightness: Int, //LCD section slider 0..10
    val padIndication: PadIndication, //LCD section dropdown
    val tempoIndication: SyncSwitch, //LCD section dropdown
    val dispMode: DispMode, //LCD section dropdown
    val padLock: SyncSwitch, // Option section dropdown
    val autoPowerOff: AutoOff, //Option section dropdown
    val usbDevMode: UsbMidiMode, //Option Section dropdown
    val midiCh: Int, //MIDI section Setup dropdown for 0..16
    val midiSync: MidiSync, // MIDI section Setup enum dropdown
    val localCtrl: SyncSwitch, // MIDI section Setup enum dropdown
    val softThru: SyncSwitch, // MIDI section Setup enum dropdown
    val usbMIDIThru: SyncSwitch, // MIDI section Setup enum dropdown
    val midiPCCtrl: SyncSwitch, //MIDI section CTRL enum dropdown
    val midiCCCtrl: SyncSwitch, //MIDI section CTRL enum dropdown
    val midiFxSelCc: Int, //MIDI section CTRL slider 0..95
    val mstrFxCtrl1Cc: Int, //MIDI section CTRL slider 0..95
    val mstrFxCtrl2Cc: Int, //MIDI section CTRL slider 0..95
    val multiView: Int,
    val startupKit: Int,
    val fs1Polarity: FootSwitchPolarity,
    val fs2Polarity: FootSwitchPolarity,
    val intPads: List<IntPad>,
    val extPads: List<ExtPad>
) {
    companion object {
        fun fromValues(
            lcdContrast: Int, lcdBright: Int, padIllumi: Int, tempoIndi: Int,
            fs1Polarity: Int, fs2Polarity: Int, midiCh: Int, midiSync: Int, localCtrl: Int,
            softThru: Int, midiPCCtrl: Int, midiCCCtrl: Int, midiFxSelCc: Int,
            mstrFxCtrl1Cc: Int, mstrFxCtrl2Cc: Int, usbMIDIThru: Int,
            padLock: Int, autoPowerOff: Int, dispMode: Int, multiView: Int,
            usbDevMode: Int, startupKit: Int, intPads: List<List<Int>>, extPads: List<List<Int>>
        ) = SetupConfig(
            lcdContrast = lcdContrast,
            lcdBrightness = lcdBright,
            padIndication = PadIndication.fromValue(padIllumi),
            tempoIndication = SyncSwitch.fromValue(tempoIndi),
            fs1Polarity = FootSwitchPolarity.fromValue(fs1Polarity),
            fs2Polarity = FootSwitchPolarity.fromValue(fs2Polarity),
            midiCh = midiCh,
            midiSync = MidiSync.fromValue(midiSync),
            localCtrl = SyncSwitch.fromValue(localCtrl),
            softThru = SyncSwitch.fromValue(softThru),
            midiPCCtrl = SyncSwitch.fromValue(midiPCCtrl),
            midiCCCtrl = SyncSwitch.fromValue(midiCCCtrl),
            midiFxSelCc = midiFxSelCc,
            mstrFxCtrl1Cc = mstrFxCtrl1Cc,
            mstrFxCtrl2Cc = mstrFxCtrl2Cc,
            usbMIDIThru = SyncSwitch.fromValue(usbMIDIThru),
            padLock = SyncSwitch.fromValue(padLock),
            autoPowerOff = AutoOff.fromValue(autoPowerOff),
            dispMode = DispMode.fromValue(dispMode),
            multiView = multiView,
            usbDevMode = UsbMidiMode.fromValue(usbDevMode),
            startupKit = startupKit,
            intPads = intPads.map { IntPad.fromRawValues(it[0], it[1], it[2]) },
            extPads = extPads.map { ExtPad.fromRawValues(it[0], it[1], it[2], it[3], it[4], it[5], it[6], it[7], it[8], it[9], it[10], it[11]) }
        )
    }
}


data class ExtPad(
    val inputMode: InputMode,
    val padType: TrigType,
    val sens: Int,
    val threshold: Int,
    val curve: VeloCurve,
    val scanTime: Int,
    val retrigCxl: Int,
    val maskTime: Int,
    val xtalkCxl: Int,
    val rimAdjust: Int,
    val rimGain: Int,
    val noiseCxl: Int
) {
    companion object {
        fun fromRawValues(
            inputMode: Int, padType: Int, sens: Int, threshold: Int, curve: Int,
            scanTime: Int, retrigCxl: Int, maskTime: Int, xtalkCxl: Int,
            rimAdjust: Int, rimGain: Int, noiseCxl: Int
        ) = ExtPad(
            inputMode = InputMode.fromValue(inputMode),
            padType = TrigType.fromValue(padType),
            sens = sens,
            threshold = threshold,
            curve = VeloCurve.fromValue(curve),
            scanTime = scanTime,
            retrigCxl = retrigCxl,
            maskTime = maskTime,
            xtalkCxl = xtalkCxl,
            rimAdjust = rimAdjust,
            rimGain = rimGain,
            noiseCxl = noiseCxl
        )
    }
}

data class IntPad(
    val sens: Int,
    val threshold: Int,
    val curve: VeloCurve
) {
    companion object {
        fun fromRawValues(sens: Int, threshold: Int, curve: Int) = IntPad(
            sens = sens,
            threshold = threshold,
            curve = VeloCurve.fromValue(curve)
        )
    }
}

enum class UsbMidiMode(val value: Int) {
    WAVEMGR(0), AUDIO_MIDI(1);

    companion object {
        fun fromValue(value: Int) = entries.find { it.value == value } ?: WAVEMGR
    }
}

enum class InputMode(val value: Int) {
    HEAD_RIM(0), TRIG_X2(1);

    companion object {
        fun fromValue(value: Int) = entries.find { it.value == value } ?: HEAD_RIM
    }
}

enum class FootSwitchPolarity(val value: Int) {
    NORMAL(0), INVERSE(1);

    companion object {
        fun fromValue(value: Int) = entries.find { it.value == value } ?: NORMAL
    }
}

enum class MidiSync(val value: Int) {
    OFF(0), AUTO(1);

    companion object {
        fun fromValue(value: Int) = entries.find { it.value == value } ?: OFF
    }
}

enum class AutoOff(val value: Int) {
    OFF(0), FOUR_HRS(1);

    companion object {
        fun fromValue(value: Int) = entries.find { it.value == value } ?: OFF
    }
}

enum class VeloCurve(val value: Int) {
    LINEAR(0), EXP1(1), EXP2(2), LOG1(3), LOG2(4),
    SPLINE(5), LOUD1(6), LOUD2(7);

    companion object {
        fun fromValue(value: Int) = entries.find { it.value == value } ?: LINEAR
    }
}

enum class PadIndication(val value: Int) {
    OFF(0), DYNAMIC(1), STATE(2), ALL_ON(3);

    companion object {
        fun fromValue(value: Int) = entries.find { it.value == value } ?: OFF
    }
}

enum class DispMode(val value: Int) {
    SUBNAME(0), LEVEL(1);

    companion object {
        fun fromValue(value: Int) = entries.find { it.value == value } ?: SUBNAME
    }
}

fun SetupConfig.Companion.fromRaw(raw: SetupPrm): SetupConfig {
    return SetupConfig(
        lcdContrast = raw.lcdContrast,
        lcdBrightness = raw.lcdBright,
        padIndication = PadIndication.fromValue(raw.padIllumi),
        tempoIndication = SyncSwitch.fromValue(raw.tempoIndi),
        fs1Polarity = FootSwitchPolarity.fromValue(raw.fs1Porality),
        fs2Polarity = FootSwitchPolarity.fromValue(raw.fs2Porality),
        midiCh = raw.midiCh,
        midiSync = MidiSync.fromValue(raw.midiSync),
        localCtrl = SyncSwitch.fromValue(raw.localCtrl),
        softThru = SyncSwitch.fromValue(raw.softThru),
        midiPCCtrl = SyncSwitch.fromValue(raw.midiPCCtrl),
        midiCCCtrl = SyncSwitch.fromValue(raw.midiCCCtrl),
        midiFxSelCc = raw.mefctCCSel,
        mstrFxCtrl1Cc = raw.mefctCCKnob1,
        mstrFxCtrl2Cc = raw.mefctCCKnob2,
        usbMIDIThru = SyncSwitch.fromValue(raw.usbMIDIThru),
        padLock = SyncSwitch.fromValue(raw.padLock),
        autoPowerOff = AutoOff.fromValue(raw.autoPowerOff),
        dispMode = DispMode.fromValue(raw.dispMode),
        multiView = raw.multiView,
        usbDevMode = UsbMidiMode.fromValue(raw.usbDevMode),
        startupKit = raw.startupKit,
        intPads = raw.intPads.map { IntPad.fromRawValues(it.sens, it.threshold, it.curve) },
        extPads = raw.extPads.map { ExtPad.fromRawValues(
            it.inputMode, it.padType, it.sens, it.threshold, it.curve,
            it.scanTime, it.retrigCxl, it.maskTime, it.xtalkCxl,
            it.rimAdjust, it.rimGain, it.noiseCxl) }
    )
}


