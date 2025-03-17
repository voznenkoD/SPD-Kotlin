package org.xebia.spdmanager.model.system

data class SystemAudioConfig(
    val audioInVolume: Int,
    val usbInVolume: Int,
    val subOutVolume: Int,
    val systemGain: SystemGain,
    val audioInOutput: Output,
    val fx2Output: FxOutput,
    val systemEq: SystemEq
) {
    companion object {
        fun fromValues(
            audioInVolume: Int,
            usbInVolume: Int,
            subOutVolume: Int,
            systemGain: Int,
            audioInOutput: Int,
            fx2Output: Int,
            lowGain: Int,
            midFreq: Int,
            midGain: Int,
            highGain: Int
        ): SystemAudioConfig {
            return SystemAudioConfig(
                audioInVolume = audioInVolume,
                usbInVolume = usbInVolume,
                subOutVolume = subOutVolume,
                systemGain = SystemGain.fromValue(systemGain),
                audioInOutput = Output.fromValue(audioInOutput),
                fx2Output = FxOutput.fromValue(fx2Output),
                systemEq = SystemEq.fromValues(lowGain, midFreq, midGain, highGain)
            )
        }
    }
}

enum class SystemGain (val value: Int, val description: String) {
    ZERO(0, "0 dB"),
    SIX_DB(1, "6 dB"),
    TWELVE_DB(2, "12 dB");

    companion object {
        fun fromValue(value: Int): SystemGain {
            return entries.find { it.value == value } ?: ZERO
        }
    }
}

enum class FxOutput(val value: Int) {
    MASTER(0),
    SUB(1);

    companion object {
        fun fromValue(value: Int): FxOutput {
            return entries.find { it.value == value } ?: MASTER
        }
    }
}