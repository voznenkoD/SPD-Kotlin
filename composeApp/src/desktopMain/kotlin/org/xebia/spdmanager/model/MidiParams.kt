package org.xebia.spdmanager.model

data class MidiParams(val midiChannel: Int, val midiNote: Int, val externalControl: Boolean, val gate: Int)
