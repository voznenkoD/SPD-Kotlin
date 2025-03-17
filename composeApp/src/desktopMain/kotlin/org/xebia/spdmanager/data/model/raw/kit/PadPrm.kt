package org.xebia.spdmanager.data.model.raw.kit

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.io.Serializable

@JacksonXmlRootElement(localName = "PadPrm")
data class PadPrm(
    val Wv: Int,        // Wave ref
    val WvLevel: Int,   // Volume
    val WvPan: Int,     // Pan: SmallPan (L15..R15)
    val PlayMode: Int,  // MODE Template
    val OutAsgn: Int,
    val MuteGrp: Int,   // MAIN MuteGroup 1..9
    val TempoSync: Int,   // MAIN
    val PadMidiCh: Int,  // MIDI
    val NoteNum: Int,    // MIDI
    val MidiCtrl: Int,   // MIDI
    val Loop: Int,       //MODE
    val TrigType: Int,   //MODE
    val GateTime: Int,   //MIDI
    val Dynamics: Int,   //MODE
    val VoiceAsgn: Int, //MODE MONO/POLY
    val Reverse: Int,
    val SubWv: Int,      //Wave ref
    val SubWvLevel: Int, //Volume
    val SubWvPan: Int    //Pan: Small Pan L15..R15
) : Serializable
