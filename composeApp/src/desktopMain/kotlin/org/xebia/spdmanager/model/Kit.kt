package org.xebia.spdmanager.model

data class Kit(
    val name: String,
    val subName: String,
    val tempo: Double,
    val volume: Int,
    val records: Pair<String, String>,
    val pads: Map<String, Pad>
)
