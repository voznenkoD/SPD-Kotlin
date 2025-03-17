package org.xebia.spdmanager.model.system.fx.common

enum class WahMode(val value: Int) {
    LPF(0),
    BPF(1);


    companion object {
        fun fromValue(value: Int): WahMode {
            return entries.find { it.value == value } ?: LPF
        }
    }
}
