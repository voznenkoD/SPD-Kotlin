package org.xebia.spdmanager.model.system.vControl


sealed class Bank {
    data object Off : Bank()
    data class BankNumber(val number: Int) : Bank() {
        init {
            require(number in 0..127) { "Note number must be between 0 and 127" }
        }
        override fun toString(): String = number.toString()
    }

    companion object {
        fun fromValue(value: Int): Bank {
            return when (value) {
                -1 -> Off
                in 0..127 -> BankNumber(value)
                else -> throw IllegalArgumentException("Value must be between -1 and 127")
            }
        }
    }
}