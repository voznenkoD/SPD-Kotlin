package org.xebia.spdmanager.model.system.vControl


sealed class Bank {
    abstract fun toValue(): Int

    data object Off : Bank() {
        override fun toValue(): Int = -1
    }
    data class BankNumber(val number: Int) : Bank() {
        override fun toValue(): Int = number
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