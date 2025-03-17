package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class SysPrm(
    @JacksonXmlProperty(localName = "ClickSndGrp") val clickSoundGroup: Int,
    @JacksonXmlProperty(localName = "ClickSnd") val clickSound: Int,
    @JacksonXmlProperty(localName = "ClickWv") val clickWave: Int,
    @JacksonXmlProperty(localName = "ClickInterval") val clickInterval: Int,
    @JacksonXmlProperty(localName = "ClickPan") val clickPan: Int,
    @JacksonXmlProperty(localName = "ClickAsgn") val clickAssign: Int,
    @JacksonXmlProperty(localName = "ClickLevel") val clickLevel: Int,
    @JacksonXmlProperty(localName = "AudInLevel") val audioInputLevel: Int,
    @JacksonXmlProperty(localName = "AudInAsgn") val audioInputAssign: Int,
    @JacksonXmlProperty(localName = "Fx2Asgn") val fx2Assign: Int,
    @JacksonXmlProperty(localName = "SystemGain") val systemGain: Int,
    @JacksonXmlProperty(localName = "SubOutLevel") val subOutLevel: Int,
    @JacksonXmlProperty(localName = "USBDAudInLevel") val usbDAudioInputLevel: Int,
    @JacksonXmlProperty(localName = "KitChainSw") val kitChainSwitch: Int,
    @JacksonXmlProperty(localName = "KitChainBank") val kitChainBank: Int,
    @JacksonXmlProperty(localName = "PadCtrlPad1") val padCtrlPad1: Int,
    @JacksonXmlProperty(localName = "PadCtrlPad2") val padCtrlPad2: Int,
    @JacksonXmlProperty(localName = "PadCtrlPad3") val padCtrlPad3: Int,
    @JacksonXmlProperty(localName = "PadCtrlPad4") val padCtrlPad4: Int,
    @JacksonXmlProperty(localName = "PadCtrlPad5") val padCtrlPad5: Int,
    @JacksonXmlProperty(localName = "PadCtrlPad6") val padCtrlPad6: Int,
    @JacksonXmlProperty(localName = "PadCtrlPad7") val padCtrlPad7: Int,
    @JacksonXmlProperty(localName = "PadCtrlPad8") val padCtrlPad8: Int,
    @JacksonXmlProperty(localName = "PadCtrlPad9") val padCtrlPad9: Int,
    @JacksonXmlProperty(localName = "PadCtrlExt1") val padCtrlExt1: Int,
    @JacksonXmlProperty(localName = "PadCtrlExt2") val padCtrlExt2: Int,
    @JacksonXmlProperty(localName = "PadCtrlExt3") val padCtrlExt3: Int,
    @JacksonXmlProperty(localName = "PadCtrlExt4") val padCtrlExt4: Int,
    @JacksonXmlProperty(localName = "PadCtrlFS1") val padCtrlFS1: Int,
    @JacksonXmlProperty(localName = "PadCtrlFS2") val padCtrlFS2: Int,
    @JacksonXmlProperty(localName = "VLinkMode") val vLinkMode: Int,
    @JacksonXmlProperty(localName = "VLinkBank") val vLinkBank: Int,
    @JacksonXmlProperty(localName = "VLinkCh") val vLinkChannel: Int,
    @JacksonXmlProperty(localName = "VLinkKnob1CC") val vLinkKnob1CC: Int,
    @JacksonXmlProperty(localName = "VLinkKnob2CC") val vLinkKnob2CC: Int,
    @JacksonXmlProperty(localName = "VLinkCtrlOnly") val vLinkControlOnly: Int
) {
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

