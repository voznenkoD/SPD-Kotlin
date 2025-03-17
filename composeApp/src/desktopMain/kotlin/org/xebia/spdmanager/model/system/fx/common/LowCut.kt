package org.xebia.spdmanager.model.system.fx.common

enum class LowCut(val value: Int) {
    FLAT(0),
    HZ_55(1),
    HZ_110(2),
    HZ_165(3),
    HZ_200(4),
    HZ_280(5),
    HZ_340(6),
    HZ_400(7),
    HZ_500(8),
    HZ_630(9),
    HZ_800(10);

    companion object {
        fun fromValue(value: Int): LowCut {
            return entries.find { it.value == value } ?: HZ_280
        }
    }
}