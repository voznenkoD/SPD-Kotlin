package org.xebia.spdmanager.model.system.fx.common

enum class LowCut(val value: Int, val displayName: String) {
    FLAT(0, "Flat"),
    HZ_55(1, "55"),
    HZ_110(2, "110"),
    HZ_165(3, "165"),
    HZ_200(4, "200"),
    HZ_280(5, "280"),
    HZ_340(6, "340"),
    HZ_400(7, "400"),
    HZ_500(8, "500"),
    HZ_630(9, "630"),
    HZ_800(10, "800");

    override fun toString() = displayName

    companion object {
        fun fromValue(value: Int): LowCut {
            return entries.find { it.value == value } ?: HZ_280
        }
    }
}