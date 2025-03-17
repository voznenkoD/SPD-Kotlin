package org.xebia.spdmanager.model.system.fx.common

enum class DistortionType(val value: Int) {
    MID_BOOST(0),
    CLEAN_BOOST(1),
    TREBLE_BST(2),
    BLUES_OD(3),
    CRUNCH(4),
    NATURAL_OD(5),
    OD_1(6),
    T_SCREAM(7),
    TURBO_OD(8),
    WARM_OD(9),
    MILD_DS(10),
    MID_DS(11),
    RAT(12),
    GUV_DS(13),
    DST_PLUS(14),
    MODERN_DS(15),
    SOLID_DS(16),
    STACK(17),
    LOUD(18),
    METAL_ZONE(19),
    LEAD(20),
    SIXTIES_FUZZ(21),
    OCT_FUZZ(22),
    MUFF_FUZZ(23);


    companion object {
        fun fromValue(value: Int): DistortionType {
            return entries.find { it.value == value } ?: MID_BOOST
        }
    }
}
