package org.xebia.spdmanager.data.model.raw.wave

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class WvPrm(
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
    @param:JacksonXmlProperty(localName = "Path") @get:JacksonXmlProperty(localName = "Path") val path: String,
    @param:JacksonXmlProperty(localName = "Tag") @get:JacksonXmlProperty(localName = "Tag") val tag: Int,
    @param:JacksonXmlProperty(localName = "Tempo") @get:JacksonXmlProperty(localName = "Tempo") val tempo: Int,
    @param:JacksonXmlProperty(localName = "Beat") @get:JacksonXmlProperty(localName = "Beat") val beat: Int,
    @param:JacksonXmlProperty(localName = "Measure") @get:JacksonXmlProperty(localName = "Measure") val measure: Int,
    @param:JacksonXmlProperty(localName = "Start") @get:JacksonXmlProperty(localName = "Start") val start: Int,
    @param:JacksonXmlProperty(localName = "End") @get:JacksonXmlProperty(localName = "End") val end: Int
) {

    @JsonIgnore
    fun waveName(): IntArray {
        return intArrayOf(nm0, nm1, nm2, nm3, nm4, nm5, nm6, nm7, nm8, nm9)
    }
}

