package org.xebia.spdmanager.audioplayer

import java.io.File
import javax.sound.sampled.AudioSystem

data class WaveformData(
    val samples: List<Float>,
    val sampleRate: Int
)

fun readWavFile(filePath: String): WaveformData {
    val file = File(filePath)
    val audioInputStream = try {
        AudioSystem.getAudioInputStream(file)
    } catch (e: Exception) {
        e.printStackTrace()
        return WaveformData(emptyList(), 44100)
    }

    val format = audioInputStream.format
    val sampleRate = format.sampleRate.toInt()
    val frameSize = format.frameSize
    val byteArray = audioInputStream.readAllBytes()

    val samples = mutableListOf<Float>()
    for (i in byteArray.indices step frameSize) {
        val sample = (byteArray[i + 1].toInt() shl 8) or (byteArray[i].toInt() and 0xFF)
        val normalized = sample / 32768.0f
        samples.add(normalized)
    }

    return WaveformData(samples, sampleRate)
}
