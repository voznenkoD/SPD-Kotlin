package org.xebia.spdmanager.model.system.fx.common

enum class DelayTimeEnum(val index: Int, val value: String) {
    THIRTY_SECOND(0, "1/32"),
    SIXTEENTH(1, "1/16"),
    EIGHTH_TRIPLET(2, "1/8^3"),
    SIXTEENTH_DOTTED(3, "1/16."),
    EIGHTH(4, "1/8"),
    QUARTER_TRIPLET(5, "1/4^3"),
    EIGHTH_DOTTED(6, "1/8."),
    QUARTER(7, "1/4"),
    HALF_TRIPLET(8, "1/2^3"),
    QUARTER_DOTTED(9, "1/4."),
    HALF(10, "1/2"),
    HALF_DOTTED(11, "1/2."),
    WHOLE(12, "1");

    companion object {
        fun fromIndex(index: Int): DelayTimeEnum {
            return entries.find { it.index == index } ?: EIGHTH
        }
    }
}