package org.xebia.spdmanager.data.model.raw.system
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class KitChain(
    @param:JacksonXmlProperty(localName = "Nm0") @get:JacksonXmlProperty(localName = "Nm0") val nm0: Int,
    @param:JacksonXmlProperty(localName = "Nm1") @get:JacksonXmlProperty(localName = "Nm1") val nm1: Int,
    @param:JacksonXmlProperty(localName = "Nm2") @get:JacksonXmlProperty(localName = "Nm2") val nm2: Int,
    @param:JacksonXmlProperty(localName = "Nm3") @get:JacksonXmlProperty(localName = "Nm3") val nm3: Int,
    @param:JacksonXmlProperty(localName = "Nm4") @get:JacksonXmlProperty(localName = "Nm4") val nm4: Int,
    @param:JacksonXmlProperty(localName = "Nm5") @get:JacksonXmlProperty(localName = "Nm5") val nm5: Int,
    @param:JacksonXmlProperty(localName = "Nm6") @get:JacksonXmlProperty(localName = "Nm6") val nm6: Int,
    @param:JacksonXmlProperty(localName = "Nm7") @get:JacksonXmlProperty(localName = "Nm7") val nm7: Int,
    @param:JacksonXmlProperty(localName = "Nm8") @get:JacksonXmlProperty(localName = "Nm8") val nm8: Int,
    @param:JacksonXmlProperty(localName = "Nm9") @get:JacksonXmlProperty(localName = "Nm9") val nm9: Int,
    @param:JacksonXmlProperty(localName = "Stp0") @get:JacksonXmlProperty(localName = "Stp0") val stp0: Int,
    @param:JacksonXmlProperty(localName = "Stp1") @get:JacksonXmlProperty(localName = "Stp1") val stp1: Int,
    @param:JacksonXmlProperty(localName = "Stp2") @get:JacksonXmlProperty(localName = "Stp2") val stp2: Int,
    @param:JacksonXmlProperty(localName = "Stp3") @get:JacksonXmlProperty(localName = "Stp3") val stp3: Int,
    @param:JacksonXmlProperty(localName = "Stp4") @get:JacksonXmlProperty(localName = "Stp4") val stp4: Int,
    @param:JacksonXmlProperty(localName = "Stp5") @get:JacksonXmlProperty(localName = "Stp5") val stp5: Int,
    @param:JacksonXmlProperty(localName = "Stp6") @get:JacksonXmlProperty(localName = "Stp6") val stp6: Int,
    @param:JacksonXmlProperty(localName = "Stp7") @get:JacksonXmlProperty(localName = "Stp7") val stp7: Int,
    @param:JacksonXmlProperty(localName = "Stp8") @get:JacksonXmlProperty(localName = "Stp8") val stp8: Int,
    @param:JacksonXmlProperty(localName = "Stp9") @get:JacksonXmlProperty(localName = "Stp9") val stp9: Int,
    @param:JacksonXmlProperty(localName = "Stp10") @get:JacksonXmlProperty(localName = "Stp10") val stp10: Int,
    @param:JacksonXmlProperty(localName = "Stp11") @get:JacksonXmlProperty(localName = "Stp11") val stp11: Int,
    @param:JacksonXmlProperty(localName = "Stp12") @get:JacksonXmlProperty(localName = "Stp12") val stp12: Int,
    @param:JacksonXmlProperty(localName = "Stp13") @get:JacksonXmlProperty(localName = "Stp13") val stp13: Int,
    @param:JacksonXmlProperty(localName = "Stp14") @get:JacksonXmlProperty(localName = "Stp14") val stp14: Int,
    @param:JacksonXmlProperty(localName = "Stp15") @get:JacksonXmlProperty(localName = "Stp15") val stp15: Int,
    @param:JacksonXmlProperty(localName = "Stp16") @get:JacksonXmlProperty(localName = "Stp16") val stp16: Int,
    @param:JacksonXmlProperty(localName = "Stp17") @get:JacksonXmlProperty(localName = "Stp17") val stp17: Int,
    @param:JacksonXmlProperty(localName = "Stp18") @get:JacksonXmlProperty(localName = "Stp18") val stp18: Int,
    @param:JacksonXmlProperty(localName = "Stp19") @get:JacksonXmlProperty(localName = "Stp19") val stp19: Int
) {
    @JsonIgnore
    fun kitChainName(): IntArray {
        return intArrayOf(nm0, nm1, nm2, nm3, nm4, nm5, nm6, nm7, nm8, nm9)
    }

    @JsonIgnore
    fun getKits(): List<Int> {
        return listOf(stp0, stp1, stp2, stp3, stp4, stp5, stp6, stp7, stp8, stp9, stp10, stp11,
            stp12, stp13, stp14, stp15, stp16, stp17, stp18, stp19)
    }

}



