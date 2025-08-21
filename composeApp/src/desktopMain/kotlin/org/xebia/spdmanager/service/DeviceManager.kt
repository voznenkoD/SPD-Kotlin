package org.xebia.spdmanager.service

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.xebia.spdmanager.data.Coordinate
import org.xebia.spdmanager.data.decodeName
import org.xebia.spdmanager.data.model.raw.kit.KitPrm
import org.xebia.spdmanager.data.model.raw.system.*
import org.xebia.spdmanager.data.model.raw.wave.WvPrm
import org.xebia.spdmanager.model.*
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.pad.Pad
import org.xebia.spdmanager.model.kit.pad.PadNumber
import org.xebia.spdmanager.model.list.WaveListsHolder
import org.xebia.spdmanager.model.setup.SetupConfig
import org.xebia.spdmanager.model.setup.fromRaw
import org.xebia.spdmanager.model.system.SystemConfig

class DeviceManager {
    val xmlParser = XmlParser()
    var device by mutableStateOf<Device?>(null)

    val classTypeToFilename = mapOf(
        Config::class.java to "sysparam.spd",
        TagList::class.java to "tag_list.spd",
        WvListSortbyName::class.java to "wavelist_name.spd",
        WvListSortbyNameTag::class.java to "wavelist_tagname.spd",
        WvListSortbyNumTag::class.java to "wavelist_tagnum.spd"
    )

    fun readDevice(rootPath: String) {
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

        device = Device(setupConfig, systemConfig, toKits(rawKits), toWaves(rawWaves), waveListsHolder, rootPath)
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

    fun updateKit(kitIndex: Int, transform: (Kit) -> Kit) {
        device?.let { currentDevice ->
            val updatedKits = currentDevice.kits.toMutableList()
            if (kitIndex in updatedKits.indices) {
                updatedKits[kitIndex] = transform(updatedKits[kitIndex])
                device = currentDevice.copy(kits = updatedKits)
                saveDevice()
            }
        }
    }

    fun updatePad(kitIndex: Int, padNumber: PadNumber, transform: (Pad) -> Pad) {
        device?.let { currentDevice ->
            val updatedKits = currentDevice.kits.toMutableList()
            if (kitIndex in updatedKits.indices) {
                val kit = updatedKits[kitIndex]
                val updatedPads = kit.pads.toMutableMap()
                updatedPads[padNumber]?.let { pad ->
                    updatedPads[padNumber] = transform(pad)
                }
                updatedKits[kitIndex] = kit.copy(pads = updatedPads)
                device = currentDevice.copy(kits = updatedKits)
                saveDevice()
            }
        }
    }

    fun updateWave(waveNumber: Int, transform: (Wave) -> Wave) {
        device?.let { currentDevice ->
            val updatedWaves = currentDevice.waves.map { wave ->
                if (wave.number == waveNumber) transform(wave) else wave
            }
            device = currentDevice.copy(waves = updatedWaves)
            saveDevice()
        }
    }

    /**
     * Updates the system configuration
     */
    fun updateSystemConfig(newSystemConfig: SystemConfig) {
        device?.let { currentDevice ->
            device = currentDevice.copy(systemConfig = newSystemConfig)
            saveDevice()
        }
    }

    /**
     * Updates a specific kit
     */
    fun updateKit(kitIndex: Int, updatedKit: Kit) {
        device?.let { currentDevice ->
            val updatedKits = currentDevice.kits.toMutableList()
            if (kitIndex in updatedKits.indices) {
                updatedKits[kitIndex] = updatedKit
                device = currentDevice.copy(kits = updatedKits)
                saveDevice()
            }
        }
    }

    /**
     * Updates a specific pad within a kit
     */
    fun updatePad(kitIndex: Int, padNumber: PadNumber, updatedPad: Pad) {
        device?.let { currentDevice ->
            val kit = currentDevice.kits.getOrNull(kitIndex) ?: return

            val updatedPads = kit.pads.toMutableMap()
            updatedPads[padNumber] = updatedPad

            val updatedKit = kit.copy(pads = updatedPads)

            updateKit(kitIndex, updatedKit)
        }
    }

    /**
     * Updates the wave list
     */
    fun updateWaves(waves: List<Wave>) {
        device?.let { currentDevice ->
            device = currentDevice.copy(waves = waves)
            saveDevice()
        }
    }

    /**
     * Updates a specific wave
     */
    fun updateWave(waveIndex: Int, updatedWave: Wave) {
        device?.let { currentDevice ->
            val updatedWaves = currentDevice.waves.toMutableList()
            if (waveIndex in updatedWaves.indices) {
                updatedWaves[waveIndex] = updatedWave
                device = currentDevice.copy(waves = updatedWaves)
                saveDevice()
            }
        }
    }

    /**
     * Updates wave lists holder
     */
    fun updateWaveLists(waveListsHolder: WaveListsHolder) {
        device?.let { currentDevice ->
            device = currentDevice.copy(waveLists = waveListsHolder)
            saveDevice()
        }
    }

    private fun saveDevice() {
        // TODO: Implement saving logic if needed
        // This would write the changes back to the XML files
    }
}
