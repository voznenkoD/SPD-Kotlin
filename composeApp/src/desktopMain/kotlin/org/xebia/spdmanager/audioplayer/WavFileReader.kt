package org.xebia.spdmanager.audioplayer

import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.UnsupportedAudioFileException
import java.io.IOException

fun readWavFile(filePath: String): List<Float> {
    val file = File(filePath)
    val audioInputStream: AudioInputStream = try {
        AudioSystem.getAudioInputStream(file)
    } catch (e: UnsupportedAudioFileException) {
        e.printStackTrace()
        return emptyList()
    } catch (e: IOException) {
        e.printStackTrace()
        return emptyList()
    }

    val frameSize = 2  // 16-bit PCM means each sample is 2 bytes
    val frameRate = 44100  // 44.1 kHz sample rate
    val byteArray = audioInputStream.readAllBytes()

    val samples = mutableListOf<Float>()

    for (i in byteArray.indices step frameSize) {
        val sample = (byteArray[i + 1].toInt() shl 8) or (byteArray[i].toInt() and 0xFF)
        val normalizedSample = sample / 32768.0f  // 32768 is the max value for a 16-bit signed integer
        samples.add(normalizedSample)
    }

    return samples
}
