package org.xebia.spdmanager.model.list

import org.xebia.spdmanager.data.decodeName
import org.xebia.spdmanager.data.encodeNamePadded
import org.xebia.spdmanager.data.model.raw.system.*
import org.xebia.spdmanager.model.Wave

data class WaveListsHolder(val wavesByName: List<ListedWave>, val wavesByNamePerCategory: Map<Category, List<ListedWave>>, val wavesByNumPerCategory: Map<Category, List<ListedWave>>) {
    data class RawWaveLists(
        val tagList: TagList,
        val wvListSortbyName: WvListSortbyName,
        val wvListSortbyNameTag: WvListSortbyNameTag,
        val wvListSortbyNumTag: WvListSortbyNumTag
    )

    fun toRaw(): RawWaveLists {
        val categories = wavesByNamePerCategory.keys.sortedBy { it.order }
        val tagList = TagList(categories.map { cat ->
            val nm = encodeNamePadded(cat.name, 12)
            TagPrm(nm[0], nm[1], nm[2], nm[3], nm[4], nm[5], nm[6], nm[7], nm[8], nm[9], nm[10], nm[11], cat.order)
        })
        val wvListSortbyName = WvListSortbyName(wavesByName.map { it.number })
        val wvListSortbyNameTag = WvListSortbyNameTag(categories.map { cat ->
            WvList(wavesByNamePerCategory[cat]?.map { it.number } ?: emptyList())
        })
        val wvListSortbyNumTag = WvListSortbyNumTag(categories.map { cat ->
            WvList(wavesByNumPerCategory[cat]?.map { it.number } ?: emptyList())
        })
        return RawWaveLists(tagList, wvListSortbyName, wvListSortbyNameTag, wvListSortbyNumTag)
    }

 companion object {
     fun fromValues(rawTagList: TagList, rawWavesByName: WvListSortbyName, rawByNameTag: WvListSortbyNameTag, rawByNumTag: WvListSortbyNumTag, waves: List<Wave>): WaveListsHolder {
         val categories = rawTagList.tagList.map { tag ->
             Category(decodeName(tag.tagName()), tag.order)
         }

         val listedWaves = waves.associate { wave ->
             wave.number to ListedWave(wave.number, wave.name)
         }

         val wavesByName = rawWavesByName.wvList.mapNotNull {waveNumber -> listedWaves[waveNumber]}

         val wavesNameCategory = categories
             .mapIndexed { index, category -> category to rawByNameTag.wvList[index].wvList.mapNotNull { listedWaves[it] } }
             .toMap()

         val wavesNumCategory = categories
             .mapIndexed { index, category -> category to rawByNumTag.wvList[index].wvList.mapNotNull { listedWaves[it] } }
             .toMap()

         return WaveListsHolder(wavesByName, wavesNameCategory, wavesNumCategory)
     }
 }
}
