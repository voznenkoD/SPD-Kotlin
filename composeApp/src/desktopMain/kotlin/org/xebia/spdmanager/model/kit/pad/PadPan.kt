package org.xebia.spdmanager.model.kit.pad

data class PadPan (val value: Int) {
    override fun toString(): String {
        return when {
            value < 0 -> "L${-value}"
            value > 0 -> "R${value}"
            else -> "C"
        }
    }

    companion object {
        fun fromValue(raw: Int): PadPan {
            require(raw in 0..30) { "Raw pan value must be between 0 and 30" }
            return PadPan(raw - 15)
        }
    }
}
