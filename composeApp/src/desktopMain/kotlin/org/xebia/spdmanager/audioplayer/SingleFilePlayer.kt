package org.xebia.spdmanager.audioplayer

import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent

object SingleFilePlayer {

    private val playing: MutableSet<Clip> = ConcurrentHashMap.newKeySet()
    private var currentClip: Clip? = null

    fun play(file: File) {
        stop()
        val clip = createClip(file)
        currentClip = clip
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
                if (clip == currentClip) {
                    currentClip = null
                }
            }
        }
        return clip
    }

    fun stop() {
        playing.forEach { clip -> clip.stop() }
        playing.clear()
        currentClip = null
    }

    fun getPlayingClip(): Clip? {
        return currentClip?.takeIf { it.isRunning }
    }
}
