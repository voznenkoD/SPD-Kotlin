package org.xebia.spdmanager.model.setup

import org.xebia.spdmanager.data.model.raw.system.SetupPrm

data class SetupConfig(
    val lcdContrast: Int,
    val lcdBrightness: Int,
    val padIndication: PadIndication,
    val tempoIndication: TempoIndication,
    val fs1Polarity: FootSwitchPolarity,
    val fs2Polarity: FootSwitchPolarity,
    val midiCh: Int,
    val midiSync: MidiSync,
    val localCtrl: LocalControl,
    val softThru: LocalControl,
    val midiPCCtrl: LocalControl,
    val midiCCCtrl: LocalControl,
    val usbMIDIThru: LocalControl,
    val padLock: LocalControl,
    val autoPowerOff: Int,
    val dispMode: DispMode,
    val multiView: Int,
    val usbDevMode: UsbMidiMode,
    val startupKit: Int,
    val intPads: List<IntPad>,
    val extPads: List<ExtPad>
) {
    companion object {
        fun fromValues(
            lcdContrast: Int, lcdBright: Int, padIllumi: Int, tempoIndi: Int,
            fs1Polarity: Int, fs2Polarity: Int, midiCh: Int, midiSync: Int, localCtrl: Int,
            softThru: Int, midiPCCtrl: Int, midiCCCtrl: Int, usbMIDIThru: Int,
            padLock: Int, autoPowerOff: Int, dispMode: Int, multiView: Int,
            usbDevMode: Int, startupKit: Int, intPads: List<List<Int>>, extPads: List<List<Int>>
        ) = SetupConfig(
            lcdContrast = lcdContrast,
            lcdBrightness = lcdBright,
            padIndication = PadIndication.fromValue(padIllumi),
            tempoIndication = TempoIndication.fromValue(tempoIndi),
            fs1Polarity = FootSwitchPolarity.fromValue(fs1Polarity),
            fs2Polarity = FootSwitchPolarity.fromValue(fs2Polarity),
            midiCh = midiCh,
            midiSync = MidiSync.fromValue(midiSync),
            localCtrl = LocalControl.fromValue(localCtrl),
            softThru = LocalControl.fromValue(softThru),
            midiPCCtrl = LocalControl.fromValue(midiPCCtrl),
            midiCCCtrl = LocalControl.fromValue(midiCCCtrl),
            usbMIDIThru = LocalControl.fromValue(usbMIDIThru),
            padLock = LocalControl.fromValue(padLock),
            autoPowerOff = autoPowerOff,
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
    val padType: Int, // Could be mapped to an enum if required
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
            padType = padType,
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
        fun fromValue(value: Int) = values().find { it.value == value } ?: WAVEMGR
    }
}

enum class InputMode(val value: Int) {
    HEAD_RIM(0), TRIG_X2(1);

    companion object {
        fun fromValue(value: Int) = values().find { it.value == value } ?: HEAD_RIM
    }
}

enum class FootSwitchPolarity(val value: Int) {
    NORMAL(0), INVERSE(1);

    companion object {
        fun fromValue(value: Int) = values().find { it.value == value } ?: NORMAL
    }
}

enum class MidiSync(val value: Int) {
    OFF(0), AUTO(1);

    companion object {
        fun fromValue(value: Int) = values().find { it.value == value } ?: OFF
    }
}

enum class LocalControl(val value: Int) {
    OFF(0), ON(1);

    companion object {
        fun fromValue(value: Int) = values().find { it.value == value } ?: ON
    }
}

enum class VeloCurve(val value: Int) {
    LINEAR(0), EXP1(1), EXP2(2), LOG1(3), LOG2(4),
    SPLINE(5), LOUD1(6), LOUD2(7);

    companion object {
        fun fromValue(value: Int) = values().find { it.value == value } ?: LINEAR
    }
}

enum class PadIndication(val value: Int) {
    OFF(0), DYNAMIC(1), STATE(2), ALL_ON(3);

    companion object {
        fun fromValue(value: Int) = values().find { it.value == value } ?: OFF
    }
}

enum class TempoIndication(val value: Int) {
    OFF(0), ON(1);

    companion object {
        fun fromValue(value: Int) = values().find { it.value == value } ?: OFF
    }
}

enum class DispMode(val value: Int) {
    SUBNAME(0), LEVEL(1);

    companion object {
        fun fromValue(value: Int) = values().find { it.value == value } ?: SUBNAME
    }
}

fun SetupConfig.Companion.fromRaw(raw: SetupPrm): SetupConfig {
    return SetupConfig(
        lcdContrast = raw.lcdContrast,
        lcdBrightness = raw.lcdBright,
        padIndication = PadIndication.fromValue(raw.padIllumi),
        tempoIndication = TempoIndication.fromValue(raw.tempoIndi),
        fs1Polarity = FootSwitchPolarity.fromValue(raw.fs1Porality),
        fs2Polarity = FootSwitchPolarity.fromValue(raw.fs2Porality),
        midiCh = raw.midiCh,
        midiSync = MidiSync.fromValue(raw.midiSync),
        localCtrl = LocalControl.fromValue(raw.localCtrl),
        softThru = LocalControl.fromValue(raw.softThru),
        midiPCCtrl = LocalControl.fromValue(raw.midiPCCtrl),
        midiCCCtrl = LocalControl.fromValue(raw.midiCCCtrl),
        usbMIDIThru = LocalControl.fromValue(raw.usbMIDIThru),
        padLock = LocalControl.fromValue(raw.padLock),
        autoPowerOff = raw.autoPowerOff,
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


