package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class TagPrm(
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
    @JacksonXmlProperty(localName = "Nm10") val nm10: Int,
    @JacksonXmlProperty(localName = "Nm11") val nm11: Int,
    @JacksonXmlProperty(localName = "Order") val order: Int
) {
    fun tagName(): IntArray {
        return intArrayOf(nm0, nm1, nm2, nm3, nm4, nm5, nm6, nm7, nm8, nm9, nm10, nm11)
    }
}


