package org.xebia.spdmanager.model.system.fx.common

enum class EqQ(val index: Int, val description: String) {
    Q_0_5(0, "Q 0.5"),
    Q_1(1, "Q 1"),
    Q_2(2, "Q 2"),
    Q_4(3, "Q 4"),
    Q_8(4, "Q 8"),
    Q_16(5, "Q 16");

    companion object {
        fun fromIndex(index: Int): EqQ {
            return entries.find { it.index == index } ?: Q_0_5
        }
    }
}
