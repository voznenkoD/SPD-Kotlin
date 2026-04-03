package org.xebia.spdmanager.model.system.vControl

sealed class KnobCC {
    abstract fun toValue(): Int

    data object Off : KnobCC() {
        override fun toValue(): Int = 0
    }
    data class KnobCcNumber(val number: Int) : KnobCC() {
        override fun toValue(): Int = number
        init {
            require(number in 0..127) { "CC must be between 0 and 127" }
        }
        override fun toString(): String = number.toString()
    }

    companion object {
        fun fromValue(value: Int): KnobCC {
            return when (value) {
                0 -> Off
                in 1..127 -> KnobCcNumber(value)
                else -> throw IllegalArgumentException("Value must be between 0 and 127")
            }
        }
    }
}