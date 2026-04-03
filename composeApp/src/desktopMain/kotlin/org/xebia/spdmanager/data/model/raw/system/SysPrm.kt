package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class SysPrm(
    @param:JacksonXmlProperty(localName = "ClickSndGrp") @get:JacksonXmlProperty(localName = "ClickSndGrp") val clickSoundGroup: Int,
    @param:JacksonXmlProperty(localName = "ClickSnd") @get:JacksonXmlProperty(localName = "ClickSnd") val clickSound: Int,
    @param:JacksonXmlProperty(localName = "ClickWv") @get:JacksonXmlProperty(localName = "ClickWv") val clickWave: Int,
    @param:JacksonXmlProperty(localName = "ClickInterval") @get:JacksonXmlProperty(localName = "ClickInterval") val clickInterval: Int,
    @param:JacksonXmlProperty(localName = "ClickPan") @get:JacksonXmlProperty(localName = "ClickPan") val clickPan: Int,
    @param:JacksonXmlProperty(localName = "ClickAsgn") @get:JacksonXmlProperty(localName = "ClickAsgn") val clickAssign: Int,
    @param:JacksonXmlProperty(localName = "ClickLevel") @get:JacksonXmlProperty(localName = "ClickLevel") val clickLevel: Int,
    @param:JacksonXmlProperty(localName = "AudInLevel") @get:JacksonXmlProperty(localName = "AudInLevel") val audioInputLevel: Int,
    @param:JacksonXmlProperty(localName = "AudInAsgn") @get:JacksonXmlProperty(localName = "AudInAsgn") val audioInputAssign: Int,
    @param:JacksonXmlProperty(localName = "Fx2Asgn") @get:JacksonXmlProperty(localName = "Fx2Asgn") val fx2Assign: Int,
    @param:JacksonXmlProperty(localName = "SystemGain") @get:JacksonXmlProperty(localName = "SystemGain") val systemGain: Int,
    @param:JacksonXmlProperty(localName = "SubOutLevel") @get:JacksonXmlProperty(localName = "SubOutLevel") val subOutLevel: Int,
    @param:JacksonXmlProperty(localName = "USBDAudInLevel") @get:JacksonXmlProperty(localName = "USBDAudInLevel") val usbDAudioInputLevel: Int,
    @param:JacksonXmlProperty(localName = "KitChainSw") @get:JacksonXmlProperty(localName = "KitChainSw") val kitChainSwitch: Int,
    @param:JacksonXmlProperty(localName = "KitChainBank") @get:JacksonXmlProperty(localName = "KitChainBank") val kitChainBank: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlPad1") @get:JacksonXmlProperty(localName = "PadCtrlPad1") val padCtrlPad1: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlPad2") @get:JacksonXmlProperty(localName = "PadCtrlPad2") val padCtrlPad2: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlPad3") @get:JacksonXmlProperty(localName = "PadCtrlPad3") val padCtrlPad3: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlPad4") @get:JacksonXmlProperty(localName = "PadCtrlPad4") val padCtrlPad4: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlPad5") @get:JacksonXmlProperty(localName = "PadCtrlPad5") val padCtrlPad5: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlPad6") @get:JacksonXmlProperty(localName = "PadCtrlPad6") val padCtrlPad6: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlPad7") @get:JacksonXmlProperty(localName = "PadCtrlPad7") val padCtrlPad7: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlPad8") @get:JacksonXmlProperty(localName = "PadCtrlPad8") val padCtrlPad8: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlPad9") @get:JacksonXmlProperty(localName = "PadCtrlPad9") val padCtrlPad9: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlExt1") @get:JacksonXmlProperty(localName = "PadCtrlExt1") val padCtrlExt1: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlExt2") @get:JacksonXmlProperty(localName = "PadCtrlExt2") val padCtrlExt2: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlExt3") @get:JacksonXmlProperty(localName = "PadCtrlExt3") val padCtrlExt3: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlExt4") @get:JacksonXmlProperty(localName = "PadCtrlExt4") val padCtrlExt4: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlFS1") @get:JacksonXmlProperty(localName = "PadCtrlFS1") val padCtrlFS1: Int,
    @param:JacksonXmlProperty(localName = "PadCtrlFS2") @get:JacksonXmlProperty(localName = "PadCtrlFS2") val padCtrlFS2: Int,
    @param:JacksonXmlProperty(localName = "VLinkMode") @get:JacksonXmlProperty(localName = "VLinkMode") val vLinkMode: Int,
    @param:JacksonXmlProperty(localName = "VLinkBank") @get:JacksonXmlProperty(localName = "VLinkBank") val vLinkBank: Int,
    @param:JacksonXmlProperty(localName = "VLinkCh") @get:JacksonXmlProperty(localName = "VLinkCh") val vLinkChannel: Int,
    @param:JacksonXmlProperty(localName = "VLinkKnob1CC") @get:JacksonXmlProperty(localName = "VLinkKnob1CC") val vLinkKnob1CC: Int,
    @param:JacksonXmlProperty(localName = "VLinkKnob2CC") @get:JacksonXmlProperty(localName = "VLinkKnob2CC") val vLinkKnob2CC: Int,
    @param:JacksonXmlProperty(localName = "VLinkCtrlOnly") @get:JacksonXmlProperty(localName = "VLinkCtrlOnly") val vLinkControlOnly: Int
) {
    @JsonIgnore
    fun padFsControls(): IntArray {
        return intArrayOf(
            padCtrlPad1,
            padCtrlPad2,
            padCtrlPad3,
            padCtrlPad4,
            padCtrlPad5,
            padCtrlPad6,
            padCtrlPad7,
            padCtrlPad8,
            padCtrlPad9,
            padCtrlExt1,
            padCtrlExt2,
            padCtrlExt3,
            padCtrlExt4,
            padCtrlFS1,
            padCtrlFS2
        )
    }
}

