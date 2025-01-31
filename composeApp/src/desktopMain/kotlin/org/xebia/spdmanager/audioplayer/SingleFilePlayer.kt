package org.xebia.spdmanager.audioplayer

import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent

object SingleFilePlayer {

    private val playing: MutableSet<Clip> = ConcurrentHashMap.newKeySet()

    fun play(file: File) {
        stop()
        val clip = createClip(file)
        clip.start()
        playing.add(clip)
    }

    private fun createClip(file: File): Clip {
        val audioInputStream = AudioSystem.getAudioInputStream(file)
        val clip: Clip = AudioSystem.getClip()
        clip.open(audioInputStream)
        clip.addLineListener { event ->
            if (event.type == LineEvent.Type.STOP && clip.microsecondPosition >= clip.microsecondLength - 50) {
                playing.remove(clip)
            }
        }
        return clip
    }

    fun stop() {
        playing.forEach { clip -> clip.stop() }
        playing.clear()
    }
}