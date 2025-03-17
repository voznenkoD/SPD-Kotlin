package org.xebia.spdmanager.model.system.fx.common

enum class EqFreq(val index: Int, val description: String) {
    HZ_20(0, "20 Hz"),
    HZ_25(1, "25 Hz"),
    HZ_31_5(2, "31.5 Hz"),
    HZ_40(3, "40 Hz"),
    HZ_50(4, "50 Hz"),
    HZ_63(5, "63 Hz"),
    HZ_80(6, "80 Hz"),
    HZ_100(7, "100 Hz"),
    HZ_125(8, "125 Hz"),
    HZ_160(9, "160 Hz"),
    HZ_200(10, "200 Hz"),
    HZ_250(11, "250 Hz"),
    HZ_315(12, "315 Hz"),
    HZ_400(13, "400 Hz"),
    HZ_500(14, "500 Hz"),
    HZ_630(15, "630 Hz"),
    HZ_800(16, "800 Hz"),
    HZ_1000(17, "1.00 kHz"),
    HZ_1250(18, "1.25 kHz"),
    HZ_1600(19, "1.60 kHz"),
    HZ_2000(20, "2.00 kHz"),
    HZ_2500(21, "2.50 kHz"),
    HZ_3150(22, "3.15 kHz"),
    HZ_4000(23, "4.00 kHz"),
    HZ_5000(24, "5.00 kHz"),
    HZ_6300(25, "6.30 kHz"),
    HZ_8000(26, "8.00 kHz"),
    HZ_10000(27, "10.0 kHz");

    companion object {
        fun fromIndex(index: Int): EqFreq {
            return entries.find { it.index == index } ?: HZ_20
        }
    }
}
