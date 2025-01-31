package org.xebia.spdmanager.audioplayer

import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent

object MultiFilePlayer {

    private val playing: MutableSet<Clip> = ConcurrentHashMap.newKeySet()

    fun play(file: File) {
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

    fun pauseAll() {
        playing.filter { clip -> clip.isRunning }.forEach(Clip::stop)
    }

    fun resumeAll() {
        playing.filter { clip -> !clip.isRunning }.forEach(Clip::start)
    }

    fun stopAll() {
        playing.forEach(Clip::stop)
        playing.clear()
    }
}
