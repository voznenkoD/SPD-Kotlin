package org.xebia.spdmanager.model.system.vControl

sealed class KnobCC {
    data object Off : KnobCC()
    data class KnobCcNumber(val number: Int) : KnobCC() {
        init {
            require(number in 1..95) { "Note number must be between 0 and 95" }
        }
        override fun toString(): String = number.toString()
    }

    companion object {
        fun fromValue(value: Int): KnobCC {
            return when (value) {
                0 -> Off
                in 1..127 -> KnobCcNumber(value)
                else -> throw IllegalArgumentException("Value must be between 0 and 95")
            }
        }
    }
}