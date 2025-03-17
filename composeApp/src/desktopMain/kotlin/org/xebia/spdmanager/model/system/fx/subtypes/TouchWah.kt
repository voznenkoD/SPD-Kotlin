package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.Polarity
import org.xebia.spdmanager.model.system.fx.common.WahMode

data class TouchWah(
    override val fxType: FXType = FXType.TOUCH_WAH,
    val mode: WahMode,
    val polarity: Polarity,
    val peak: Int,
    val effectLevel: Int,
    val directLevel: Int,
): FxEffect(){
    companion object {
        fun fromValues(params: List<Int>): TouchWah {
            return TouchWah(
                mode = WahMode.fromValue(params[0]),
                polarity = Polarity.fromValue(params[1]),
                peak = params[2],
                effectLevel = params[3],
                directLevel = params[4],
            )
        }
    }
}
