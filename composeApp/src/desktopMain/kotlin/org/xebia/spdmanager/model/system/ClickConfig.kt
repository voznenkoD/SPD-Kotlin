package org.xebia.spdmanager.model.system

data class ClickConfig(
    val soundGroup: SoundGroup,
    val sound: ClickSound,
    val wave: Int,  // Assuming wave is a raw integer value
    val interval: Interval,
    val pan: Pan,
    val output: Output,
    val level: Int    // Assuming level is a raw integer value
) {
    companion object {
        fun fromValues(
            soundGroup: Int,
            sound: Int,
            wave: Int,
            interval: Int,
            pan: Int,
            output: Int,
            level: Int
        ): ClickConfig {
            return ClickConfig(
                soundGroup = SoundGroup.fromValue(soundGroup),
                sound = ClickSound.fromValue(sound),
                wave = wave,
                interval = Interval.fromValue(interval),
                pan = Pan.fromValue(pan),
                output = Output.fromValue(output),
                level = level
            )
        }
    }
}


enum class SoundGroup(val value: Int) {
    PRESET(0),
    USER(1);

    companion object {
        fun fromValue(value: Int): SoundGroup = entries.find { it.value == value } ?: PRESET
    }
}

enum class ClickSound(val value: Int) {
    ELECTRONIC(0),
    BEEP(1),
    PULSE(2),
    SWEEP(3),
    OLD_STYLE(4),
    DRUM_STICKS(5),
    SNARE(6),
    CLAVES(7),
    COWBELL(8),
    SHAKER(9);

    companion object {
        fun fromValue(value: Int): ClickSound = entries.find { it.value == value } ?: ELECTRONIC
    }
}

enum class Interval(val value: Int) {
    ONE_FOURTH(0),
    ONE_EIGHTH(1),
    ONE_TWELFTH(2);

    companion object {
        fun fromValue(value: Int): Interval = entries.find { it.value == value } ?: ONE_FOURTH
    }
}

data class Pan(val value: Int) {
    override fun toString(): String {
        return when {
            value < 0 -> "L${-value}"
            value > 0 -> "R${value}"
            else -> "C" // Center
        }
    }

    companion object {
        fun fromValue(rawValue: Int): Pan {
            // Map range 0..30 to -15..15
            val mappedValue = rawValue - 15
            return Pan(mappedValue.coerceIn(-15, 15))
        }
    }
}



