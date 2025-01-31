package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class IntPad(
    @JacksonXmlProperty(localName = "Sens") val sens: Int,
    @JacksonXmlProperty(localName = "Threshold") val threshold: Int,
    @JacksonXmlProperty(localName = "Curve") val curve: Int
)

