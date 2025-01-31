package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class SetupPrm(
    @JacksonXmlProperty(localName = "LCDContrast") val lcdContrast: Int,
    @JacksonXmlProperty(localName = "LCDBright") val lcdBright: Int,
    @JacksonXmlProperty(localName = "PadIllumi") val padIllumi: Int,
    @JacksonXmlProperty(localName = "TempoIndi") val tempoIndi: Int,
    @JacksonXmlProperty(localName = "FS1Porality") val fs1Porality: Int,
    @JacksonXmlProperty(localName = "FS2Porality") val fs2Porality: Int,
    @JacksonXmlProperty(localName = "MIDICh") val midiCh: Int,
    @JacksonXmlProperty(localName = "MIDISync") val midiSync: Int,
    @JacksonXmlProperty(localName = "LocalCtrl") val localCtrl: Int,
    @JacksonXmlProperty(localName = "SoftThru") val softThru: Int,
    @JacksonXmlProperty(localName = "MIDIPCCtrl") val midiPCCtrl: Int,
    @JacksonXmlProperty(localName = "MIDICCCtrl") val midiCCCtrl: Int,
    @JacksonXmlProperty(localName = "MEfctCCSel") val mefctCCSel: Int,
    @JacksonXmlProperty(localName = "MEfctCCKnob1") val mefctCCKnob1: Int,
    @JacksonXmlProperty(localName = "MEfctCCKnob2") val mefctCCKnob2: Int,
    @JacksonXmlProperty(localName = "USBMIDIThru") val usbMIDIThru: Int,
    @JacksonXmlProperty(localName = "PadLock") val padLock: Int,
    @JacksonXmlProperty(localName = "AutoPowerOff") val autoPowerOff: Int,
    @JacksonXmlProperty(localName = "DispMode") val dispMode: Int,
    @JacksonXmlProperty(localName = "MultiView") val multiView: Int,
    @JacksonXmlProperty(localName = "USBDevMode") val usbDevMode: Int,
    @JacksonXmlProperty(localName = "StartupKit") val startupKit: Int,
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "IntPad") val intPads: Array<IntPad>,
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "ExtPad") val extPads: Array<ExtPad>
)
