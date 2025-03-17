package org.xebia.spdmanager.model.system

import org.xebia.spdmanager.model.system.fx.common.EqFreq

data class SystemEq(
    val lowGain: Float,
    val midFreq: EqFreq,
    val midGain: Float,
    val highGain: Float,
) {
    companion object {
        fun fromValues(lowGain: Int, midFreq: Int, midGain: Int, highGain: Int): SystemEq {
            return SystemEq(
                mapGain(lowGain),
                EqFreq.fromIndex(midFreq),
                mapGain(midGain),
                mapGain(highGain)
            )
        }

        private fun mapGain(value: Int): Float {
            return (value - 12).toFloat()
        }
    }
}
