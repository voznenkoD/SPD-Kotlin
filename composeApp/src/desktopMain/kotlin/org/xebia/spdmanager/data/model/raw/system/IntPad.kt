package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class IntPad(
    @param:JacksonXmlProperty(localName = "Sens") @get:JacksonXmlProperty(localName = "Sens") val sens: Int,
    @param:JacksonXmlProperty(localName = "Threshold") @get:JacksonXmlProperty(localName = "Threshold") val threshold: Int,
    @param:JacksonXmlProperty(localName = "Curve") @get:JacksonXmlProperty(localName = "Curve") val curve: Int
)

