package org.xebia.spdmanager.service

import org.xebia.spdmanager.data.Coordinate
import org.xebia.spdmanager.data.decodeName
import org.xebia.spdmanager.data.model.raw.kit.KitPrm
import org.xebia.spdmanager.data.model.raw.system.*
import org.xebia.spdmanager.data.model.raw.wave.WvPrm
import org.xebia.spdmanager.model.*
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.list.WaveListsHolder
import org.xebia.spdmanager.model.setup.SetupConfig
import org.xebia.spdmanager.model.setup.fromRaw
import org.xebia.spdmanager.model.system.SystemConfig

class DeviceManager {
    val xmlParser = XmlParser()

    val classTypeToFilename = mapOf(
        Config::class.java to "sysparam.spd",
        TagList::class.java to "tag_list.spd",
        WvListSortbyName::class.java to "wavelist_name.spd",
        WvListSortbyNameTag::class.java to "wavelist_tagname.spd",
        WvListSortbyNumTag::class.java to "wavelist_tagnum.spd"
    )

    fun readDevice(rootPath: String): Device {
        val systemFiles = xmlParser.readFilesInFolder("$rootPath/SYSTEM")
        val kitFiles = xmlParser.readFilesInFolder("$rootPath/KIT")

        val rawKits = xmlParser.parseKits(kitFiles)
        val rawSystemConfig = xmlParser.parseSystemConfig(systemFiles)

        val rawWaves = xmlParser.parseWaves("$rootPath/WAVE/PRM")

        val rawTagList = xmlParser.parseSystemFile<TagList>(classTypeToFilename[TagList::class.java]!!, systemFiles)
        val rawWvListSortByName = xmlParser.parseSystemFile<WvListSortbyName>(classTypeToFilename[WvListSortbyName::class.java]!!, systemFiles)
        val rawWvListSortByNameTag = xmlParser.parseSystemFile<WvListSortbyNameTag>(classTypeToFilename[WvListSortbyNameTag::class.java]!!, systemFiles)
        val rawWvListSortByNumTag = xmlParser.parseSystemFile<WvListSortbyNumTag>(classTypeToFilename[WvListSortbyNumTag::class.java]!!, systemFiles)


        val setupConfig = SetupConfig.fromRaw(rawSystemConfig!!.setupPrm)
        val systemConfig = SystemConfig.fromValue(rawSystemConfig.sysPrm, rawSystemConfig.kitChainPrm, rawSystemConfig.mEfctPrm)

        val waves = toWaves(rawWaves)
        val waveListsHolder = WaveListsHolder.fromValues(rawTagList!!, rawWvListSortByName!!, rawWvListSortByNameTag!!, rawWvListSortByNumTag!!, waves)

        val device = Device(setupConfig, systemConfig, toKits(rawKits), toWaves(rawWaves), waveListsHolder)

        return device
    }

    private fun toKits(rawKits: List<KitPrm>): List<Kit> {
        val kits = mutableListOf<Kit>()
        for(rawKit in rawKits) {
            kits.add(Kit.fromValues(rawKit))
        }
        return kits
    }

    private fun toWaves(rawWaves: Map<Coordinate, WvPrm>): List<Wave> {
        val waves = mutableListOf<Wave>()
        for ((coordinate, wvPrm) in rawWaves) {
            val number = coordinate.waveNumber
            val name = decodeName(wvPrm.waveName())
            waves.add(Wave(number, name, wvPrm.path, wvPrm.tag, wvPrm.tempo, wvPrm.beat, wvPrm.measure, wvPrm.start, wvPrm.end))
        }
        return waves
    }
}
