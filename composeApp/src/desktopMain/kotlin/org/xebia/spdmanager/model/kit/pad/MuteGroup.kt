package org.xebia.spdmanager.model.kit.pad


sealed class MuteGroup {
    abstract fun toValue(): Int

    data object Off : MuteGroup() {
        override fun toValue(): Int = -1
    }
    data class Group(val number: Int) : MuteGroup() {
        override fun toValue(): Int = number
        init {
            require(number in 0..9) { "MuteGroup number must be between 0 and 9" }
        }
        override fun toString(): String = number.toString()
    }

    companion object {
        fun fromValue(value: Int): MuteGroup {
            return when (value) {
                -1 -> Off
                in 0..9 -> Group(value)
                else -> throw IllegalArgumentException("Value must be between -1 and 9")
            }
        }
    }
}
