package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class ExtPad(
    @JacksonXmlProperty(localName = "InputMode") val inputMode: Int,
    @JacksonXmlProperty(localName = "PadType") val padType: Int,
    @JacksonXmlProperty(localName = "Sens") val sens: Int,
    @JacksonXmlProperty(localName = "Threshold") val threshold: Int,
    @JacksonXmlProperty(localName = "Curve") val curve: Int,
    @JacksonXmlProperty(localName = "ScanTime") val scanTime: Int,
    @JacksonXmlProperty(localName = "RetrigCxl") val retrigCxl: Int,
    @JacksonXmlProperty(localName = "MaskTime") val maskTime: Int,
    @JacksonXmlProperty(localName = "XtalkCxl") val xtalkCxl: Int,
    @JacksonXmlProperty(localName = "RimAdjust") val rimAdjust: Int,
    @JacksonXmlProperty(localName = "RimGain") val rimGain: Int,
    @JacksonXmlProperty(localName = "NoiseCxl") val noiseCxl: Int,

)

