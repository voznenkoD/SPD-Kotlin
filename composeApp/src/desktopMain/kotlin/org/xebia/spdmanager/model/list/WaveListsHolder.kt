package org.xebia.spdmanager.model.list

import org.xebia.spdmanager.data.decodeName
import org.xebia.spdmanager.data.model.raw.system.TagList
import org.xebia.spdmanager.data.model.raw.system.WvListSortbyName
import org.xebia.spdmanager.data.model.raw.system.WvListSortbyNameTag
import org.xebia.spdmanager.data.model.raw.system.WvListSortbyNumTag
import org.xebia.spdmanager.model.Wave

data class WaveListsHolder(val wavesByName: List<ListedWave>, val wavesByNamePerCategory: Map<Category, List<ListedWave>>, val wavesByNumPerCategory: Map<Category, List<ListedWave>>) {
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
