package org.xebia.spdmanager.model.system

enum class Output(val value: Int) {
    MASTER(0),
    SUB(1),
    PHONES(2);

    companion object {
        fun fromValue(value: Int): Output {
            return entries.find { it.value == value } ?: MASTER  // Default to MASTER if value is invalid
        }
    }
}