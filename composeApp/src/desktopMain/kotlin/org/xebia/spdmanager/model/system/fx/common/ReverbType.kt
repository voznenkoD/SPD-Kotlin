package org.xebia.spdmanager.model.system.fx.common

enum class ReverbType(val value: Int) {
    AMBIENCE(0),
    ROOM(1),
    HALL_1(2),
    HALL_2(3),
    Plate(4);


    companion object {
        fun fromValue(value: Int): ReverbType {
            return entries.find { it.value == value } ?: AMBIENCE
        }
    }
}
