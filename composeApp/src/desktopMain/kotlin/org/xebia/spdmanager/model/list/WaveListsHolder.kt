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

    fun withAddedWave(wave: Wave, categoryName: String): WaveListsHolder {
        val listed = ListedWave(wave.number, wave.name)

        val newByName = (wavesByName + listed).sortedBy { it.name.lowercase() }

        val targetByNameKey = wavesByNamePerCategory.keys.firstOrNull { it.name == categoryName }
            ?: wavesByNamePerCategory.keys.firstOrNull()
        val updatedByName = LinkedHashMap<Category, List<ListedWave>>()
        for ((category, waves) in wavesByNamePerCategory) {
            updatedByName[category] = if (category == targetByNameKey) {
                (waves + listed).sortedBy { it.name.lowercase() }
            } else waves
        }

        val targetByNumKey = wavesByNumPerCategory.keys.firstOrNull { it.name == categoryName }
            ?: wavesByNumPerCategory.keys.firstOrNull()
        val updatedByNum = LinkedHashMap<Category, List<ListedWave>>()
        for ((category, waves) in wavesByNumPerCategory) {
            updatedByNum[category] = if (category == targetByNumKey) waves + listed else waves
        }

        return copy(
            wavesByName = newByName,
            wavesByNamePerCategory = updatedByName,
            wavesByNumPerCategory = updatedByNum
        )
    }

    fun withRemovedWave(waveNumber: Int): WaveListsHolder {
        val newByName = wavesByName.filterNot { it.number == waveNumber }

        val updatedByName = LinkedHashMap<Category, List<ListedWave>>()
        for ((category, waves) in wavesByNamePerCategory) {
            updatedByName[category] = waves.filterNot { it.number == waveNumber }
        }

        val updatedByNum = LinkedHashMap<Category, List<ListedWave>>()
        for ((category, waves) in wavesByNumPerCategory) {
            updatedByNum[category] = waves.filterNot { it.number == waveNumber }
        }

        return copy(
            wavesByName = newByName,
            wavesByNamePerCategory = updatedByName,
            wavesByNumPerCategory = updatedByNum
        )
    }

    /**
     * Returns a copy with [waveNumber] renamed to [newName]. Names drive the ordering of the
     * by-name views, so those are re-sorted; the by-number view keeps its order (only the label
     * changes). Category membership is untouched.
     */
    fun withRenamedWave(waveNumber: Int, newName: String): WaveListsHolder {
        fun rename(w: ListedWave) = if (w.number == waveNumber) w.copy(name = newName) else w

        val newByName = wavesByName.map(::rename).sortedBy { it.name.lowercase() }

        val updatedByName = LinkedHashMap<Category, List<ListedWave>>()
        for ((category, waves) in wavesByNamePerCategory) {
            updatedByName[category] = waves.map(::rename).sortedBy { it.name.lowercase() }
        }

        val updatedByNum = LinkedHashMap<Category, List<ListedWave>>()
        for ((category, waves) in wavesByNumPerCategory) {
            updatedByNum[category] = waves.map(::rename)
        }

        return copy(
            wavesByName = newByName,
            wavesByNamePerCategory = updatedByName,
            wavesByNumPerCategory = updatedByNum
        )
    }

    /**
     * Returns a copy with [waveNumber] moved into the category named [targetCategoryName]. The wave
     * is removed from whatever category currently holds it and inserted into the target — sorted by
     * name in the by-name view and by number in the by-number view. The flat by-name list is global
     * and so is unaffected. No-op if the target category does not exist or the wave is unknown.
     */
    fun withMovedWaveToCategory(waveNumber: Int, targetCategoryName: String): WaveListsHolder {
        val targetByNameKey = wavesByNamePerCategory.keys.firstOrNull { it.name == targetCategoryName }
            ?: return this
        val listed = wavesByName.firstOrNull { it.number == waveNumber } ?: return this

        val updatedByName = LinkedHashMap<Category, List<ListedWave>>()
        for ((category, waves) in wavesByNamePerCategory) {
            val without = waves.filterNot { it.number == waveNumber }
            updatedByName[category] =
                if (category == targetByNameKey) (without + listed).sortedBy { it.name.lowercase() }
                else without
        }

        val targetByNumKey = wavesByNumPerCategory.keys.firstOrNull { it.name == targetCategoryName }
        val updatedByNum = LinkedHashMap<Category, List<ListedWave>>()
        for ((category, waves) in wavesByNumPerCategory) {
            val without = waves.filterNot { it.number == waveNumber }
            updatedByNum[category] =
                if (category == targetByNumKey) (without + listed).sortedBy { it.number }
                else without
        }

        return copy(
            wavesByNamePerCategory = updatedByName,
            wavesByNumPerCategory = updatedByNum
        )
    }

    fun renameCategory(oldName: String, newName: String): WaveListsHolder {
        if (oldName == newName) return this
        if (wavesByNamePerCategory.keys.any { it.name == newName }) return this

        val updatedByName = LinkedHashMap<Category, List<ListedWave>>()
        for ((category, waves) in wavesByNamePerCategory) {
            val key = if (category.name == oldName) Category(newName, category.order) else category
            updatedByName[key] = waves
        }

        val updatedByNum = LinkedHashMap<Category, List<ListedWave>>()
        for ((category, waves) in wavesByNumPerCategory) {
            val key = if (category.name == oldName) Category(newName, category.order) else category
            updatedByNum[key] = waves
        }

        return copy(
            wavesByNamePerCategory = updatedByName,
            wavesByNumPerCategory = updatedByNum
        )
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
