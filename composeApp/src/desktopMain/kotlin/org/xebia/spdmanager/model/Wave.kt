package org.xebia.spdmanager.model

import org.xebia.spdmanager.data.encodeNamePadded
import org.xebia.spdmanager.data.model.raw.wave.WvPrm

data class Wave(val number: Int, val name: String, val path: String, val tagRef: Int, val tempo: Int, val beat: Int, val measure: Int, val start: Int, val end: Int) {
    fun toRaw(): WvPrm {
        val nm = encodeNamePadded(name, 12)
        return WvPrm(
            nm0 = nm[0], nm1 = nm[1], nm2 = nm[2], nm3 = nm[3], nm4 = nm[4],
            nm5 = nm[5], nm6 = nm[6], nm7 = nm[7], nm8 = nm[8], nm9 = nm[9],
            nm10 = nm[10], nm11 = nm[11],
            path = path, tag = tagRef, tempo = tempo, beat = beat,
            measure = measure, start = start, end = end
        )
    }
}
