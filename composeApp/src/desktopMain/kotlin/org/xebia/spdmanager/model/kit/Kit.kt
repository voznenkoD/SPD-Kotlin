package org.xebia.spdmanager.model.kit

import org.xebia.spdmanager.data.decodeName
import org.xebia.spdmanager.data.decodeTempo
import org.xebia.spdmanager.data.model.raw.kit.KitPrm
import org.xebia.spdmanager.model.kit.fx.KitFX
import org.xebia.spdmanager.model.kit.pad.*

data class Kit(
    val name: String,
    val subName: String,
    val tempo: Double,
    val volume: Int,
    val padLink: Pair<PadNumber, PadNumber>,
    val fx1: KitFX,
    val fx2: KitFX,
    val pads: Map<PadNumber, Pad>
){
    companion object {
        fun fromValues(rawKit: KitPrm): Kit {
            val name = decodeName(rawKit.kitName())
            val subName = decodeName(rawKit.kitSubName())
            val tempo = decodeTempo(rawKit.Tempo)
            val padLink = Pair(PadNumber.fromValue(rawKit.LinkPad0), PadNumber.fromValue(rawKit.LinkPad1))

            val fx1 = KitFX.fromValues(rawKit.Fx1Sw, rawKit.Fx1Type, rawKit.fx1Prm())
            val fx2 = KitFX.fromValues(rawKit.Fx2Sw, rawKit.Fx2Type, rawKit.fx2Prm())

            val pads = mutableMapOf<PadNumber, Pad>()

            for ((index, padPrm) in rawKit.PadPrm.withIndex()) {
                pads[PadNumber.fromValue(index)] = Pad.fromValues(padPrm)
            }

            return Kit(name, subName, tempo, rawKit.Level, padLink, fx1, fx2, pads)
        }

        fun formatKitNumber(kitNumber: Int): String {
            return "%03d".format(kitNumber)
        }
    }
}