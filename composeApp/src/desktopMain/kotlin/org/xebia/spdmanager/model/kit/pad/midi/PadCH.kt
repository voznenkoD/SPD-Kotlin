package org.xebia.spdmanager.model.kit.pad.midi


sealed class PadCH {
    abstract fun toValue(): Int

    data object Global : PadCH() {
        override fun toValue(): Int = -1
    }
    data class Channel(val number: Int) : PadCH() {
        override fun toValue(): Int = number
        init {
            require(number in 1..16) { "Channel number must be between 1 and 16" }
        }
        override fun toString(): String = number.toString()
    }

    companion object {
        fun fromValue(value: Int): PadCH {
            return when (value) {
                -1 -> Global
                in 1..16 -> Channel(value)
                else -> throw IllegalArgumentException("Value must be between -1 and 16 (-1 = Global)")
            }
        }
    }
}
