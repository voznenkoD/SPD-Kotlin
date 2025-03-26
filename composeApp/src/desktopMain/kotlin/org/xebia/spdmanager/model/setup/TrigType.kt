package org.xebia.spdmanager.model.setup

enum class TrigType(val value: Int) {
    KD_7(0),
    KD_8(1),
    KD_9(2),
    KD_85(3),
    KD_120(4),
    KD_140(5),
    KT_10(6),
    PD_8(7),
    PDX_6(8),
    PDX_8(9),
    PDX_100(10),
    PD_85(11),
    PD_105(12),
    PD_105X(13),
    PD_108(14),
    PD_125(15),
    PD_125X(16),
    PD_128(17),
    CY_5(18),
    CY_8(19),
    CY_12C(20),
    CY_13R(21),
    CY_12RC(22),
    CY_14C(23),
    CY_15R(24),
    BT_1(25),
    RT_10K(26),
    RT_10S(27),
    RT_10T(28),
    RT_30K(29),
    RT_30HR(30),
    RT30H_SN(31),
    RT30H_TM(32);

    companion object {
        fun fromValue(value: Int) = entries.find { it.value == value } ?: KD_7
    }
}
