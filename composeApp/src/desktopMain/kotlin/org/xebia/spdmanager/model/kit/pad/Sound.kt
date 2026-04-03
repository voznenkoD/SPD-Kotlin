package org.xebia.spdmanager.model.kit.pad

data class Sound(
    val wave: Int,
    val volume: Int,
    val pan: PadPan
) {
    fun toRawWave(): Int = wave - 1
    fun toRawPan(): Int = pan.toRaw()

    companion object {
        fun fromValues(wave: Int, volume: Int, pan:Int): Sound {
            return Sound(
                wave = wave + 1,
                volume = volume,
                pan = PadPan.fromValue(pan)
            )
        }
    }
}
