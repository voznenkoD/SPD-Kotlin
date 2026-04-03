package org.xebia.spdmanager.data.model.raw.kit
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.io.Serializable

@JacksonXmlRootElement(localName = "KitPrm")
data class KitPrm(
    @param:JacksonXmlProperty(localName = "Level") @get:JacksonXmlProperty(localName = "Level") val Level: Int,
    @param:JacksonXmlProperty(localName = "Tempo") @get:JacksonXmlProperty(localName = "Tempo") val Tempo: Int,
    @param:JacksonXmlProperty(localName = "Nm0") @get:JacksonXmlProperty(localName = "Nm0") val Nm0: Int,
    @param:JacksonXmlProperty(localName = "Nm1") @get:JacksonXmlProperty(localName = "Nm1") val Nm1: Int,
    @param:JacksonXmlProperty(localName = "Nm2") @get:JacksonXmlProperty(localName = "Nm2") val Nm2: Int,
    @param:JacksonXmlProperty(localName = "Nm3") @get:JacksonXmlProperty(localName = "Nm3") val Nm3: Int,
    @param:JacksonXmlProperty(localName = "Nm4") @get:JacksonXmlProperty(localName = "Nm4") val Nm4: Int,
    @param:JacksonXmlProperty(localName = "Nm5") @get:JacksonXmlProperty(localName = "Nm5") val Nm5: Int,
    @param:JacksonXmlProperty(localName = "Nm6") @get:JacksonXmlProperty(localName = "Nm6") val Nm6: Int,
    @param:JacksonXmlProperty(localName = "Nm7") @get:JacksonXmlProperty(localName = "Nm7") val Nm7: Int,
    @param:JacksonXmlProperty(localName = "SubNm0") @get:JacksonXmlProperty(localName = "SubNm0") val SubNm0: Int,
    @param:JacksonXmlProperty(localName = "SubNm1") @get:JacksonXmlProperty(localName = "SubNm1") val SubNm1: Int,
    @param:JacksonXmlProperty(localName = "SubNm2") @get:JacksonXmlProperty(localName = "SubNm2") val SubNm2: Int,
    @param:JacksonXmlProperty(localName = "SubNm3") @get:JacksonXmlProperty(localName = "SubNm3") val SubNm3: Int,
    @param:JacksonXmlProperty(localName = "SubNm4") @get:JacksonXmlProperty(localName = "SubNm4") val SubNm4: Int,
    @param:JacksonXmlProperty(localName = "SubNm5") @get:JacksonXmlProperty(localName = "SubNm5") val SubNm5: Int,
    @param:JacksonXmlProperty(localName = "SubNm6") @get:JacksonXmlProperty(localName = "SubNm6") val SubNm6: Int,
    @param:JacksonXmlProperty(localName = "SubNm7") @get:JacksonXmlProperty(localName = "SubNm7") val SubNm7: Int,
    @param:JacksonXmlProperty(localName = "SubNm8") @get:JacksonXmlProperty(localName = "SubNm8") val SubNm8: Int,
    @param:JacksonXmlProperty(localName = "SubNm9") @get:JacksonXmlProperty(localName = "SubNm9") val SubNm9: Int,
    @param:JacksonXmlProperty(localName = "SubNm10") @get:JacksonXmlProperty(localName = "SubNm10") val SubNm10: Int,
    @param:JacksonXmlProperty(localName = "SubNm11") @get:JacksonXmlProperty(localName = "SubNm11") val SubNm11: Int,
    @param:JacksonXmlProperty(localName = "SubNm12") @get:JacksonXmlProperty(localName = "SubNm12") val SubNm12: Int,
    @param:JacksonXmlProperty(localName = "SubNm13") @get:JacksonXmlProperty(localName = "SubNm13") val SubNm13: Int,
    @param:JacksonXmlProperty(localName = "SubNm14") @get:JacksonXmlProperty(localName = "SubNm14") val SubNm14: Int,
    @param:JacksonXmlProperty(localName = "SubNm15") @get:JacksonXmlProperty(localName = "SubNm15") val SubNm15: Int,
    @param:JacksonXmlProperty(localName = "Fx2Asgn") @get:JacksonXmlProperty(localName = "Fx2Asgn") val Fx2Asgn: Int,
    @param:JacksonXmlProperty(localName = "LinkPad0") @get:JacksonXmlProperty(localName = "LinkPad0") val LinkPad0: Int,
    @param:JacksonXmlProperty(localName = "LinkPad1") @get:JacksonXmlProperty(localName = "LinkPad1") val LinkPad1: Int,
    @param:JacksonXmlProperty(localName = "Fx1Sw") @get:JacksonXmlProperty(localName = "Fx1Sw") val Fx1Sw: Int,
    @param:JacksonXmlProperty(localName = "Fx1Type") @get:JacksonXmlProperty(localName = "Fx1Type") val Fx1Type: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm0") @get:JacksonXmlProperty(localName = "Fx1Prm0") val Fx1Prm0: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm1") @get:JacksonXmlProperty(localName = "Fx1Prm1") val Fx1Prm1: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm2") @get:JacksonXmlProperty(localName = "Fx1Prm2") val Fx1Prm2: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm3") @get:JacksonXmlProperty(localName = "Fx1Prm3") val Fx1Prm3: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm4") @get:JacksonXmlProperty(localName = "Fx1Prm4") val Fx1Prm4: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm5") @get:JacksonXmlProperty(localName = "Fx1Prm5") val Fx1Prm5: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm6") @get:JacksonXmlProperty(localName = "Fx1Prm6") val Fx1Prm6: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm7") @get:JacksonXmlProperty(localName = "Fx1Prm7") val Fx1Prm7: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm8") @get:JacksonXmlProperty(localName = "Fx1Prm8") val Fx1Prm8: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm9") @get:JacksonXmlProperty(localName = "Fx1Prm9") val Fx1Prm9: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm10") @get:JacksonXmlProperty(localName = "Fx1Prm10") val Fx1Prm10: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm11") @get:JacksonXmlProperty(localName = "Fx1Prm11") val Fx1Prm11: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm12") @get:JacksonXmlProperty(localName = "Fx1Prm12") val Fx1Prm12: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm13") @get:JacksonXmlProperty(localName = "Fx1Prm13") val Fx1Prm13: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm14") @get:JacksonXmlProperty(localName = "Fx1Prm14") val Fx1Prm14: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm15") @get:JacksonXmlProperty(localName = "Fx1Prm15") val Fx1Prm15: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm16") @get:JacksonXmlProperty(localName = "Fx1Prm16") val Fx1Prm16: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm17") @get:JacksonXmlProperty(localName = "Fx1Prm17") val Fx1Prm17: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm18") @get:JacksonXmlProperty(localName = "Fx1Prm18") val Fx1Prm18: Int,
    @param:JacksonXmlProperty(localName = "Fx1Prm19") @get:JacksonXmlProperty(localName = "Fx1Prm19") val Fx1Prm19: Int,
    @param:JacksonXmlProperty(localName = "Fx2Sw") @get:JacksonXmlProperty(localName = "Fx2Sw") val Fx2Sw: Int,
    @param:JacksonXmlProperty(localName = "Fx2Type") @get:JacksonXmlProperty(localName = "Fx2Type") val Fx2Type: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm0") @get:JacksonXmlProperty(localName = "Fx2Prm0") val Fx2Prm0: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm1") @get:JacksonXmlProperty(localName = "Fx2Prm1") val Fx2Prm1: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm2") @get:JacksonXmlProperty(localName = "Fx2Prm2") val Fx2Prm2: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm3") @get:JacksonXmlProperty(localName = "Fx2Prm3") val Fx2Prm3: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm4") @get:JacksonXmlProperty(localName = "Fx2Prm4") val Fx2Prm4: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm5") @get:JacksonXmlProperty(localName = "Fx2Prm5") val Fx2Prm5: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm6") @get:JacksonXmlProperty(localName = "Fx2Prm6") val Fx2Prm6: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm7") @get:JacksonXmlProperty(localName = "Fx2Prm7") val Fx2Prm7: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm8") @get:JacksonXmlProperty(localName = "Fx2Prm8") val Fx2Prm8: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm9") @get:JacksonXmlProperty(localName = "Fx2Prm9") val Fx2Prm9: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm10") @get:JacksonXmlProperty(localName = "Fx2Prm10") val Fx2Prm10: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm11") @get:JacksonXmlProperty(localName = "Fx2Prm11") val Fx2Prm11: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm12") @get:JacksonXmlProperty(localName = "Fx2Prm12") val Fx2Prm12: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm13") @get:JacksonXmlProperty(localName = "Fx2Prm13") val Fx2Prm13: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm14") @get:JacksonXmlProperty(localName = "Fx2Prm14") val Fx2Prm14: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm15") @get:JacksonXmlProperty(localName = "Fx2Prm15") val Fx2Prm15: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm16") @get:JacksonXmlProperty(localName = "Fx2Prm16") val Fx2Prm16: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm17") @get:JacksonXmlProperty(localName = "Fx2Prm17") val Fx2Prm17: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm18") @get:JacksonXmlProperty(localName = "Fx2Prm18") val Fx2Prm18: Int,
    @param:JacksonXmlProperty(localName = "Fx2Prm19") @get:JacksonXmlProperty(localName = "Fx2Prm19") val Fx2Prm19: Int,
    @JacksonXmlElementWrapper(useWrapping = false)
    @param:JacksonXmlProperty(localName = "PadPrm") @get:JacksonXmlProperty(localName = "PadPrm")
    val PadPrm: List<PadPrm>
) : Serializable {

    @JsonIgnore
    fun kitName(): IntArray {
        return intArrayOf(Nm0, Nm1, Nm2, Nm3, Nm4, Nm5, Nm6, Nm7)
    }

    @JsonIgnore
    fun kitSubName(): IntArray {
        return intArrayOf(
            SubNm0, SubNm1, SubNm2, SubNm3, SubNm4, SubNm5, SubNm6, SubNm7, SubNm8, SubNm9, SubNm10,
            SubNm11, SubNm12, SubNm13, SubNm14, SubNm15
        )
    }

    @JsonIgnore
    fun fx1Prm(): List<Int> {
        return listOf(
            Fx1Prm0, Fx1Prm1, Fx1Prm2, Fx1Prm3, Fx1Prm4,
            Fx1Prm5, Fx1Prm6, Fx1Prm7, Fx1Prm8, Fx1Prm9,
            Fx1Prm10, Fx1Prm11, Fx1Prm12, Fx1Prm13, Fx1Prm14,
            Fx1Prm15, Fx1Prm16, Fx1Prm17, Fx1Prm18, Fx1Prm19
        )
    }

    @JsonIgnore
    fun fx2Prm(): List<Int> {
        return listOf(
            Fx2Prm0, Fx2Prm1, Fx2Prm2, Fx2Prm3, Fx2Prm4,
            Fx2Prm5, Fx2Prm6, Fx2Prm7, Fx2Prm8, Fx2Prm9,
            Fx2Prm10, Fx2Prm11, Fx2Prm12, Fx2Prm13, Fx2Prm14,
            Fx2Prm15, Fx2Prm16, Fx2Prm17, Fx2Prm18, Fx2Prm19
        )
    }
}
