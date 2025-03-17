package org.xebia.spdmanager.model.system.fx.subtypes

enum class FXType(val value: Int) {
    THRU(0),
    STEREO_DLY(1),
    SYNC_DELAY(2),
    TAPE_ECHO(3),
    CHORUS(4),
    FLANGER(5),
    STEP_FLNGR(6),
    PHASER(7),
    STEP_PHASR(8),
    EQ(9),
    COMPRESSOR(10),
    FILTER(11),
    FILT_DRIVE(12),
    ISOLATOR(13),
    TOUCH_WAH(14),
    DISTORTION(15),
    RINGMOD(16),
    PITCHSHIFT(17),
    VIBRATO(18),
    REVERB(19),
    SLICER(20);

    companion object {
        fun fromValue(value: Int): FXType {
            return entries.find { it.value == value } ?: THRU
        }
    }
}