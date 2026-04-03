package org.xebia.spdmanager.model.kit.pad.midi


sealed class Gate {
    abstract fun toValue(): Int

    data object Off : Gate() {
        override fun toValue(): Int = -1
    }
    data object Alt : Gate() {
        override fun toValue(): Int = 0
    }

    data class GateVal(val number: Int) : Gate() {
        override fun toValue(): Int = number
        init {
            require(number in 1..16) { "Channel number must be between 1 and 16" }
        }
        override fun toString(): String = number.toString()
    }

    companion object {
        fun fromValue(value: Int): Gate {
            return when (value) {
                -1 -> Off
                0 -> Alt
                in 1..16 -> GateVal(value)
                else -> throw IllegalArgumentException("Value must be between 0 and 16 (0 = Global)")
            }
        }
    }
}
