package org.xebia.spdmanager.data.model.raw.system
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class KitChain(
    @JacksonXmlProperty(localName = "Nm0") val nm0: Int,
    @JacksonXmlProperty(localName = "Nm1") val nm1: Int,
    @JacksonXmlProperty(localName = "Nm2") val nm2: Int,
    @JacksonXmlProperty(localName = "Nm3") val nm3: Int,
    @JacksonXmlProperty(localName = "Nm4") val nm4: Int,
    @JacksonXmlProperty(localName = "Nm5") val nm5: Int,
    @JacksonXmlProperty(localName = "Nm6") val nm6: Int,
    @JacksonXmlProperty(localName = "Nm7") val nm7: Int,
    @JacksonXmlProperty(localName = "Nm8") val nm8: Int,
    @JacksonXmlProperty(localName = "Nm9") val nm9: Int,
    @JacksonXmlProperty(localName = "Stp0") val stp0: Int,
    @JacksonXmlProperty(localName = "Stp1") val stp1: Int,
    @JacksonXmlProperty(localName = "Stp2") val stp2: Int,
    @JacksonXmlProperty(localName = "Stp3") val stp3: Int,
    @JacksonXmlProperty(localName = "Stp4") val stp4: Int,
    @JacksonXmlProperty(localName = "Stp5") val stp5: Int,
    @JacksonXmlProperty(localName = "Stp6") val stp6: Int,
    @JacksonXmlProperty(localName = "Stp7") val stp7: Int,
    @JacksonXmlProperty(localName = "Stp8") val stp8: Int,
    @JacksonXmlProperty(localName = "Stp9") val stp9: Int,
    @JacksonXmlProperty(localName = "Stp10") val stp10: Int,
    @JacksonXmlProperty(localName = "Stp11") val stp11: Int,
    @JacksonXmlProperty(localName = "Stp12") val stp12: Int,
    @JacksonXmlProperty(localName = "Stp13") val stp13: Int,
    @JacksonXmlProperty(localName = "Stp14") val stp14: Int,
    @JacksonXmlProperty(localName = "Stp15") val stp15: Int,
    @JacksonXmlProperty(localName = "Stp16") val stp16: Int,
    @JacksonXmlProperty(localName = "Stp17") val stp17: Int,
    @JacksonXmlProperty(localName = "Stp18") val stp18: Int,
    @JacksonXmlProperty(localName = "Stp19") val stp19: Int
) {
    fun kitChainName(): IntArray {
        return intArrayOf(nm0, nm1, nm2, nm3, nm4, nm5, nm6, nm7, nm8, nm9)
    }

    fun getKits(): List<Int> {
        return listOf(stp0, stp1, stp2, stp3, stp4, stp5, stp6, stp7, stp8, stp9, stp10, stp11,
            stp12, stp13, stp14, stp15, stp16, stp17, stp18, stp19)
    }

}



