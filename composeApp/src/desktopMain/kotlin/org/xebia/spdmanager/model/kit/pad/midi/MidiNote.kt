package org.xebia.spdmanager.model.kit.pad.midi


sealed class MidiNote {
    data object Off : MidiNote()
    data class Channel(val number: Int) : MidiNote() {
        init {
            require(number in 0..127) { "Note number must be between 0 and 127" }
        }
        override fun toString(): String = number.toString()
    }

    companion object {
        fun fromValue(value: Int): MidiNote {
            return when (value) {
                -1 -> Off
                in 0..127 -> Channel(value)
                else -> throw IllegalArgumentException("Value must be between 0 and 127")
            }
        }
    }
}
