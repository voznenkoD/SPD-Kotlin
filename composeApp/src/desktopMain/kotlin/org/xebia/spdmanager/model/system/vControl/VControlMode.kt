package org.xebia.spdmanager.model.system.vControl

enum class VControlMode (val value: Int) {
    MVC(0),
    V_LINK(1);

    companion object {
        fun fromValue(value: Int): VControlMode {
            return entries.find { it.value == value } ?: MVC
        }
    }
}