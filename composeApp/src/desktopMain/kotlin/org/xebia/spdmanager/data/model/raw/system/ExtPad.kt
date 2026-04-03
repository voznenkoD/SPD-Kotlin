package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class ExtPad(
    @param:JacksonXmlProperty(localName = "InputMode") @get:JacksonXmlProperty(localName = "InputMode") val inputMode: Int,
    @param:JacksonXmlProperty(localName = "PadType") @get:JacksonXmlProperty(localName = "PadType") val padType: Int,
    @param:JacksonXmlProperty(localName = "Sens") @get:JacksonXmlProperty(localName = "Sens") val sens: Int,
    @param:JacksonXmlProperty(localName = "Threshold") @get:JacksonXmlProperty(localName = "Threshold") val threshold: Int,
    @param:JacksonXmlProperty(localName = "Curve") @get:JacksonXmlProperty(localName = "Curve") val curve: Int,
    @param:JacksonXmlProperty(localName = "ScanTime") @get:JacksonXmlProperty(localName = "ScanTime") val scanTime: Int,
    @param:JacksonXmlProperty(localName = "RetrigCxl") @get:JacksonXmlProperty(localName = "RetrigCxl") val retrigCxl: Int,
    @param:JacksonXmlProperty(localName = "MaskTime") @get:JacksonXmlProperty(localName = "MaskTime") val maskTime: Int,
    @param:JacksonXmlProperty(localName = "XtalkCxl") @get:JacksonXmlProperty(localName = "XtalkCxl") val xtalkCxl: Int,
    @param:JacksonXmlProperty(localName = "RimAdjust") @get:JacksonXmlProperty(localName = "RimAdjust") val rimAdjust: Int,
    @param:JacksonXmlProperty(localName = "RimGain") @get:JacksonXmlProperty(localName = "RimGain") val rimGain: Int,
    @param:JacksonXmlProperty(localName = "NoiseCxl") @get:JacksonXmlProperty(localName = "NoiseCxl") val noiseCxl: Int,

)

