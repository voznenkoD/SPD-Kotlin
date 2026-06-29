package org.xebia.spdmanager.model.kit

import org.xebia.spdmanager.data.decodeName
import org.xebia.spdmanager.data.decodeTempo
import org.xebia.spdmanager.data.encodeNamePadded
import org.xebia.spdmanager.data.encodeTempo
import org.xebia.spdmanager.data.model.raw.kit.KitPrm
import org.xebia.spdmanager.data.model.raw.kit.PadPrm
import org.xebia.spdmanager.model.kit.fx.KitFX
import org.xebia.spdmanager.model.kit.pad.*

data class Kit(
    val name: String,
    val subName: String,
    val tempo: Double,
    val volume: Int,
    val padLink: Pair<PadNumber?, PadNumber?>,
    val fx1: KitFX,
    val fx2: KitFX,
    val pads: Map<PadNumber, Pad>
){
    fun toRaw(): KitPrm {
        val nm = encodeNamePadded(name, NAME_MAX_LENGTH)
        val snm = encodeNamePadded(subName, 16)
        val fx1p = fx1.toRawParams()
        val fx2p = fx2.toRawParams()
        val padList = PadNumber.entries.map { padNum ->
            pads[padNum]?.toRaw() ?: PadPrm(0, 0, 15, 0, 0, -1, 0, -1, -1, 0, 0, 0, -1, 0, 0, 0, 0, 0, 15)
        }
        return KitPrm(
            Level = volume, Tempo = encodeTempo(tempo),
            Nm0 = nm[0], Nm1 = nm[1], Nm2 = nm[2], Nm3 = nm[3],
            Nm4 = nm[4], Nm5 = nm[5], Nm6 = nm[6], Nm7 = nm[7],
            SubNm0 = snm[0], SubNm1 = snm[1], SubNm2 = snm[2], SubNm3 = snm[3],
            SubNm4 = snm[4], SubNm5 = snm[5], SubNm6 = snm[6], SubNm7 = snm[7],
            SubNm8 = snm[8], SubNm9 = snm[9], SubNm10 = snm[10], SubNm11 = snm[11],
            SubNm12 = snm[12], SubNm13 = snm[13], SubNm14 = snm[14], SubNm15 = snm[15],
            Fx2Asgn = 0,
            LinkPad0 = padLink.first?.value ?: -1, LinkPad1 = padLink.second?.value ?: -1,
            Fx1Sw = fx1.toRawSw(), Fx1Type = fx1.toRawType(),
            Fx1Prm0 = fx1p[0], Fx1Prm1 = fx1p[1], Fx1Prm2 = fx1p[2], Fx1Prm3 = fx1p[3],
            Fx1Prm4 = fx1p[4], Fx1Prm5 = fx1p[5], Fx1Prm6 = fx1p[6], Fx1Prm7 = fx1p[7],
            Fx1Prm8 = fx1p[8], Fx1Prm9 = fx1p[9], Fx1Prm10 = fx1p[10], Fx1Prm11 = fx1p[11],
            Fx1Prm12 = fx1p[12], Fx1Prm13 = fx1p[13], Fx1Prm14 = fx1p[14], Fx1Prm15 = fx1p[15],
            Fx1Prm16 = fx1p[16], Fx1Prm17 = fx1p[17], Fx1Prm18 = fx1p[18], Fx1Prm19 = fx1p[19],
            Fx2Sw = fx2.toRawSw(), Fx2Type = fx2.toRawType(),
            Fx2Prm0 = fx2p[0], Fx2Prm1 = fx2p[1], Fx2Prm2 = fx2p[2], Fx2Prm3 = fx2p[3],
            Fx2Prm4 = fx2p[4], Fx2Prm5 = fx2p[5], Fx2Prm6 = fx2p[6], Fx2Prm7 = fx2p[7],
            Fx2Prm8 = fx2p[8], Fx2Prm9 = fx2p[9], Fx2Prm10 = fx2p[10], Fx2Prm11 = fx2p[11],
            Fx2Prm12 = fx2p[12], Fx2Prm13 = fx2p[13], Fx2Prm14 = fx2p[14], Fx2Prm15 = fx2p[15],
            Fx2Prm16 = fx2p[16], Fx2Prm17 = fx2p[17], Fx2Prm18 = fx2p[18], Fx2Prm19 = fx2p[19],
            PadPrm = padList
        )
    }

    companion object {
        /** Max length of a kit name, as encoded into the raw device format by [toRaw]. */
        const val NAME_MAX_LENGTH = 8

        fun fromValues(rawKit: KitPrm): Kit {
            val name = decodeName(rawKit.kitName())
            val subName = decodeName(rawKit.kitSubName())
            val tempo = decodeTempo(rawKit.Tempo)
            val padLink = Pair(
                if (rawKit.LinkPad0 < 0) null else PadNumber.fromValue(rawKit.LinkPad0),
                if (rawKit.LinkPad1 < 0) null else PadNumber.fromValue(rawKit.LinkPad1)
            )

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