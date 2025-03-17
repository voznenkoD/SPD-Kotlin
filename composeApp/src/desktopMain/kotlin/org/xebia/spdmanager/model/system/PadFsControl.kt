package org.xebia.spdmanager.model.system

enum class PadFsControl(val value: Int) {
    OFF(0),
    KIT_INC(1),
    KIT_DEC(2),
    CLICK(3),
    TAP_TEMPO(4),
    ALL_SOUND_OFF(5),
    FX1_ON_OFF(6),
    FX2_ON_OFF(7),
    PAD_CHECK(8);

    companion object {
        fun fromValue(value: Int): PadFsControl {
            return entries.find { it.value == value } ?: OFF
        }
    }
}
