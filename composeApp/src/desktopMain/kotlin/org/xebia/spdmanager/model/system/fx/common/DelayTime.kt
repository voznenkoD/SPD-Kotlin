package org.xebia.spdmanager.model.system.fx.common

sealed class DelayTime {
    data class EnumTime(val delayTimeEnum: DelayTimeEnum) : DelayTime()
    data class IntTime(val intTime: Int) : DelayTime()
}