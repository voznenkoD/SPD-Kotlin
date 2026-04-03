package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class SetupPrm(
    @param:JacksonXmlProperty(localName = "LCDContrast") @get:JacksonXmlProperty(localName = "LCDContrast") val lcdContrast: Int,
    @param:JacksonXmlProperty(localName = "LCDBright") @get:JacksonXmlProperty(localName = "LCDBright") val lcdBright: Int,
    @param:JacksonXmlProperty(localName = "PadIllumi") @get:JacksonXmlProperty(localName = "PadIllumi") val padIllumi: Int,
    @param:JacksonXmlProperty(localName = "TempoIndi") @get:JacksonXmlProperty(localName = "TempoIndi") val tempoIndi: Int,
    @param:JacksonXmlProperty(localName = "FS1Porality") @get:JacksonXmlProperty(localName = "FS1Porality") val fs1Porality: Int,
    @param:JacksonXmlProperty(localName = "FS2Porality") @get:JacksonXmlProperty(localName = "FS2Porality") val fs2Porality: Int,
    @param:JacksonXmlProperty(localName = "MIDICh") @get:JacksonXmlProperty(localName = "MIDICh") val midiCh: Int,
    @param:JacksonXmlProperty(localName = "MIDISync") @get:JacksonXmlProperty(localName = "MIDISync") val midiSync: Int,
    @param:JacksonXmlProperty(localName = "LocalCtrl") @get:JacksonXmlProperty(localName = "LocalCtrl") val localCtrl: Int,
    @param:JacksonXmlProperty(localName = "SoftThru") @get:JacksonXmlProperty(localName = "SoftThru") val softThru: Int,
    @param:JacksonXmlProperty(localName = "MIDIPCCtrl") @get:JacksonXmlProperty(localName = "MIDIPCCtrl") val midiPCCtrl: Int,
    @param:JacksonXmlProperty(localName = "MIDICCCtrl") @get:JacksonXmlProperty(localName = "MIDICCCtrl") val midiCCCtrl: Int,
    @param:JacksonXmlProperty(localName = "MEfctCCSel") @get:JacksonXmlProperty(localName = "MEfctCCSel") val mefctCCSel: Int,
    @param:JacksonXmlProperty(localName = "MEfctCCKnob1") @get:JacksonXmlProperty(localName = "MEfctCCKnob1") val mefctCCKnob1: Int,
    @param:JacksonXmlProperty(localName = "MEfctCCKnob2") @get:JacksonXmlProperty(localName = "MEfctCCKnob2") val mefctCCKnob2: Int,
    @param:JacksonXmlProperty(localName = "USBMIDIThru") @get:JacksonXmlProperty(localName = "USBMIDIThru") val usbMIDIThru: Int,
    @param:JacksonXmlProperty(localName = "PadLock") @get:JacksonXmlProperty(localName = "PadLock") val padLock: Int,
    @param:JacksonXmlProperty(localName = "AutoPowerOff") @get:JacksonXmlProperty(localName = "AutoPowerOff") val autoPowerOff: Int,
    @param:JacksonXmlProperty(localName = "DispMode") @get:JacksonXmlProperty(localName = "DispMode") val dispMode: Int,
    @param:JacksonXmlProperty(localName = "MultiView") @get:JacksonXmlProperty(localName = "MultiView") val multiView: Int,
    @param:JacksonXmlProperty(localName = "USBDevMode") @get:JacksonXmlProperty(localName = "USBDevMode") val usbDevMode: Int,
    @param:JacksonXmlProperty(localName = "StartupKit") @get:JacksonXmlProperty(localName = "StartupKit") val startupKit: Int,
    @JacksonXmlElementWrapper(useWrapping = false)
    @field:JacksonXmlProperty(localName = "IntPad")
    val intPads: Array<IntPad>,
    @JacksonXmlElementWrapper(useWrapping = false)
    @field:JacksonXmlProperty(localName = "ExtPad")
    val extPads: Array<ExtPad>
)
