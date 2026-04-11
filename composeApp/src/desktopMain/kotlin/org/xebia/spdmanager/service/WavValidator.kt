package org.xebia.spdmanager.service

import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.UnsupportedAudioFileException

sealed class WavValidationResult {
    data object Valid : WavValidationResult()
    data class Invalid(val message: String) : WavValidationResult()
}

object WavValidator {
    private const val REQUIRED_SAMPLE_RATE = 44100
    private const val REQUIRED_BITS_PER_SAMPLE = 16

    fun validate(file: File): WavValidationResult {
        if (!file.exists() || !file.isFile) {
            return WavValidationResult.Invalid("File does not exist: ${file.name}")
        }
        if (!file.name.lowercase().endsWith(".wav")) {
            return WavValidationResult.Invalid("Only .wav files are supported")
        }
        return try {
            val format = AudioSystem.getAudioFileFormat(file).format
            val encoding = format.encoding.toString()
            val sampleRate = format.sampleRate.toInt()
            val bitsPerSample = format.sampleSizeInBits
            val channels = format.channels

            when {
                !encoding.contains("PCM", ignoreCase = true) ->
                    WavValidationResult.Invalid("Only PCM-encoded WAV files are supported (got $encoding)")
                bitsPerSample != REQUIRED_BITS_PER_SAMPLE ->
                    WavValidationResult.Invalid("Wave must be 16-bit (got ${bitsPerSample}-bit)")
                sampleRate != REQUIRED_SAMPLE_RATE ->
                    WavValidationResult.Invalid("Wave must be 44.1 kHz (got $sampleRate Hz)")
                channels !in 1..2 ->
                    WavValidationResult.Invalid("Wave must be mono or stereo (got $channels channels)")
                else -> WavValidationResult.Valid
            }
        } catch (e: UnsupportedAudioFileException) {
            WavValidationResult.Invalid("Not a recognized WAV file: ${e.message}")
        } catch (e: Exception) {
            WavValidationResult.Invalid("Failed to read WAV file: ${e.message}")
        }
    }
}