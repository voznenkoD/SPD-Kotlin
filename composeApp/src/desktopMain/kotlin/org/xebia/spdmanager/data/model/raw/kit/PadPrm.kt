package org.xebia.spdmanager.data.model.raw.kit

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.io.Serializable

@JacksonXmlRootElement(localName = "PadPrm")
data class PadPrm(
    @param:JacksonXmlProperty(localName = "Wv") @get:JacksonXmlProperty(localName = "Wv") val Wv: Int,
    @param:JacksonXmlProperty(localName = "WvLevel") @get:JacksonXmlProperty(localName = "WvLevel") val WvLevel: Int,
    @param:JacksonXmlProperty(localName = "WvPan") @get:JacksonXmlProperty(localName = "WvPan") val WvPan: Int,
    @param:JacksonXmlProperty(localName = "PlayMode") @get:JacksonXmlProperty(localName = "PlayMode") val PlayMode: Int,
    @param:JacksonXmlProperty(localName = "OutAsgn") @get:JacksonXmlProperty(localName = "OutAsgn") val OutAsgn: Int,
    @param:JacksonXmlProperty(localName = "MuteGrp") @get:JacksonXmlProperty(localName = "MuteGrp") val MuteGrp: Int,
    @param:JacksonXmlProperty(localName = "TempoSync") @get:JacksonXmlProperty(localName = "TempoSync") val TempoSync: Int,
    @param:JacksonXmlProperty(localName = "PadMidiCh") @get:JacksonXmlProperty(localName = "PadMidiCh") val PadMidiCh: Int,
    @param:JacksonXmlProperty(localName = "NoteNum") @get:JacksonXmlProperty(localName = "NoteNum") val NoteNum: Int,
    @param:JacksonXmlProperty(localName = "MidiCtrl") @get:JacksonXmlProperty(localName = "MidiCtrl") val MidiCtrl: Int,
    @param:JacksonXmlProperty(localName = "Loop") @get:JacksonXmlProperty(localName = "Loop") val Loop: Int,
    @param:JacksonXmlProperty(localName = "TrigType") @get:JacksonXmlProperty(localName = "TrigType") val TrigType: Int,
    @param:JacksonXmlProperty(localName = "GateTime") @get:JacksonXmlProperty(localName = "GateTime") val GateTime: Int,
    @param:JacksonXmlProperty(localName = "Dynamics") @get:JacksonXmlProperty(localName = "Dynamics") val Dynamics: Int,
    @param:JacksonXmlProperty(localName = "VoiceAsgn") @get:JacksonXmlProperty(localName = "VoiceAsgn") val VoiceAsgn: Int,
    @param:JacksonXmlProperty(localName = "Reverse") @get:JacksonXmlProperty(localName = "Reverse") val Reverse: Int,
    @param:JacksonXmlProperty(localName = "SubWv") @get:JacksonXmlProperty(localName = "SubWv") val SubWv: Int,
    @param:JacksonXmlProperty(localName = "SubWvLevel") @get:JacksonXmlProperty(localName = "SubWvLevel") val SubWvLevel: Int,
    @param:JacksonXmlProperty(localName = "SubWvPan") @get:JacksonXmlProperty(localName = "SubWvPan") val SubWvPan: Int
) : Serializable
