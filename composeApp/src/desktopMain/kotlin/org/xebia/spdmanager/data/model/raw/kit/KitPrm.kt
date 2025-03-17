package org.xebia.spdmanager.data.model.raw.kit
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.io.Serializable

@JacksonXmlRootElement(localName = "KitPrm")
data class KitPrm(
    val Level: Int,
    val Tempo: Int,
    val Nm0: Int,
    val Nm1: Int,
    val Nm2: Int,
    val Nm3: Int,
    val Nm4: Int,
    val Nm5: Int,
    val Nm6: Int,
    val Nm7: Int,
    val SubNm0: Int,
    val SubNm1: Int,
    val SubNm2: Int,
    val SubNm3: Int,
    val SubNm4: Int,
    val SubNm5: Int,
    val SubNm6: Int,
    val SubNm7: Int,
    val SubNm8: Int,
    val SubNm9: Int,
    val SubNm10: Int,
    val SubNm11: Int,
    val SubNm12: Int,
    val SubNm13: Int,
    val SubNm14: Int,
    val SubNm15: Int,
    val Fx2Asgn: Int,
    val LinkPad0: Int,
    val LinkPad1: Int,
    val Fx1Sw: Int,
    val Fx1Type: Int,
    val Fx1Prm0: Int,
    val Fx1Prm1: Int,
    val Fx1Prm2: Int,
    val Fx1Prm3: Int,
    val Fx1Prm4: Int,
    val Fx1Prm5: Int,
    val Fx1Prm6: Int,
    val Fx1Prm7: Int,
    val Fx1Prm8: Int,
    val Fx1Prm9: Int,
    val Fx1Prm10: Int,
    val Fx1Prm11: Int,
    val Fx1Prm12: Int,
    val Fx1Prm13: Int,
    val Fx1Prm14: Int,
    val Fx1Prm15: Int,
    val Fx1Prm16: Int,
    val Fx1Prm17: Int,
    val Fx1Prm18: Int,
    val Fx1Prm19: Int,
    val Fx2Sw: Int,
    val Fx2Type: Int,
    val Fx2Prm0: Int,
    val Fx2Prm1: Int,
    val Fx2Prm2: Int,
    val Fx2Prm3: Int,
    val Fx2Prm4: Int,
    val Fx2Prm5: Int,
    val Fx2Prm6: Int,
    val Fx2Prm7: Int,
    val Fx2Prm8: Int,
    val Fx2Prm9: Int,
    val Fx2Prm10: Int,
    val Fx2Prm11: Int,
    val Fx2Prm12: Int,
    val Fx2Prm13: Int,
    val Fx2Prm14: Int,
    val Fx2Prm15: Int,
    val Fx2Prm16: Int,
    val Fx2Prm17: Int,
    val Fx2Prm18: Int,
    val Fx2Prm19: Int,
    @JacksonXmlElementWrapper(localName = "PadPrm", useWrapping = false)
    val PadPrm: List<PadPrm>
) : Serializable {

    fun kitName(): IntArray {
        return intArrayOf(Nm0, Nm1, Nm2, Nm3, Nm4, Nm5, Nm6, Nm7)
    }

    fun kitSubName(): IntArray {
        return intArrayOf(
            SubNm0, SubNm1, SubNm3, SubNm4, SubNm5, SubNm6, SubNm7, SubNm8, SubNm9, SubNm10,
            SubNm11, SubNm12, SubNm13, SubNm14, SubNm15
        )
    }

    fun fx1Prm(): List<Int> {
        return listOf(
            Fx1Prm0,
            Fx1Prm1,
            Fx1Prm2,
            Fx1Prm3,
            Fx1Prm4,
            Fx1Prm5,
            Fx1Prm6,
            Fx1Prm7,
            Fx1Prm8,
            Fx1Prm9,
            Fx1Prm10,
            Fx1Prm11,
            Fx1Prm12,
            Fx1Prm13,
            Fx1Prm14,
            Fx1Prm15,
            Fx1Prm16,
            Fx1Prm17,
            Fx1Prm18,
            Fx1Prm19
        )
    }

    fun fx2Prm(): List<Int> {
        return listOf(
            Fx2Prm0,
            Fx2Prm1,
            Fx2Prm2,
            Fx2Prm3,
            Fx2Prm4,
            Fx2Prm5,
            Fx2Prm6,
            Fx2Prm7,
            Fx2Prm8,
            Fx2Prm9,
            Fx2Prm10,
            Fx2Prm11,
            Fx2Prm12,
            Fx2Prm13,
            Fx2Prm14,
            Fx2Prm15,
            Fx2Prm16,
            Fx2Prm17,
            Fx2Prm18,
            Fx2Prm19
        )
    }
}
