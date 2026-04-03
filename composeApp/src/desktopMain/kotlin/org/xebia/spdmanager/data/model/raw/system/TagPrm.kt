package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class TagPrm(
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
    @param:JacksonXmlProperty(localName = "Nm10") @get:JacksonXmlProperty(localName = "Nm10") val nm10: Int,
    @param:JacksonXmlProperty(localName = "Nm11") @get:JacksonXmlProperty(localName = "Nm11") val nm11: Int,
    @param:JacksonXmlProperty(localName = "Order") @get:JacksonXmlProperty(localName = "Order") val order: Int
) {
    @JsonIgnore
    fun tagName(): IntArray {
        return intArrayOf(nm0, nm1, nm2, nm3, nm4, nm5, nm6, nm7, nm8, nm9, nm10, nm11)
    }
}


