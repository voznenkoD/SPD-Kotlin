package org.xebia.spdmanager.data.model.raw.kit

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.io.Serializable

@JacksonXmlRootElement(localName = "PadPrm")
data class PadPrm(
    val Wv: Int,
    val WvLevel: Int,
    val WvPan: Int,
    val PlayMode: Int,
    val OutAsgn: Int,
    val MuteGrp: Int,
    val TempoSync: Int,
    val PadMidiCh: Int,
    val NoteNum: Int,
    val MidiCtrl: Int,
    val Loop: Int,
    val TrigType: Int,
    val GateTime: Int,
    val Dynamics: Int,
    val VoiceAsgn: Int,
    val Reverse: Int,
    val SubWv: Int,
    val SubWvLevel: Int,
    val SubWvPan: Int
) : Serializable
