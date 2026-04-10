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
import org.xebia.spdmanager.model.setup.toRaw
import org.xebia.spdmanager.model.system.SystemConfig
import java.io.File

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
            }
        }
    }

    fun updateWave(waveNumber: Int, transform: (Wave) -> Wave) {
        device?.let { currentDevice ->
            val updatedWaves = currentDevice.waves.map { wave ->
                if (wave.number == waveNumber) transform(wave) else wave
            }
            device = currentDevice.copy(waves = updatedWaves)
        }
    }

    /**
     * Updates the system configuration
     */
    fun updateSystemConfig(newSystemConfig: SystemConfig) {
        device?.let { currentDevice ->
            device = currentDevice.copy(systemConfig = newSystemConfig)
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
            }
        }
    }

    /**
     * Updates wave lists holder
     */
    fun updateWaveLists(waveListsHolder: WaveListsHolder) {
        device?.let { currentDevice ->
            device = currentDevice.copy(waveLists = waveListsHolder)
        }
    }

    fun renameCategory(oldName: String, newName: String) {
        device?.let { currentDevice ->
            val updatedWaveLists = currentDevice.waveLists.renameCategory(oldName, newName)
            device = currentDevice.copy(waveLists = updatedWaveLists)
        }
    }

    fun moveKit(fromIndex: Int, toIndex: Int) {
        device?.let { currentDevice ->
            val kits = currentDevice.kits.toMutableList()
            if (fromIndex in kits.indices && toIndex in kits.indices && fromIndex != toIndex) {
                val kit = kits.removeAt(fromIndex)
                kits.add(toIndex, kit)
                device = currentDevice.copy(kits = kits)
            }
        }
    }

    companion object {
        fun buildWaveUsageMap(kits: List<Kit>): Map<Int, List<String>> {
            val usage = mutableMapOf<Int, MutableList<String>>()
            for (kit in kits) {
                for (pad in kit.pads.values) {
                    if (pad.main.wave > 0) {
                        usage.getOrPut(pad.main.wave) { mutableListOf() }.let { list ->
                            if (kit.name !in list) list.add(kit.name)
                        }
                    }
                    if (pad.sub.wave > 0) {
                        usage.getOrPut(pad.sub.wave) { mutableListOf() }.let { list ->
                            if (kit.name !in list) list.add(kit.name)
                        }
                    }
                }
            }
            return usage
        }
    }

    fun saveDevice() {
        val dev = device ?: return
        val rootPath = dev.rootPath
        if (rootPath.isBlank()) return

        // Convert domain → raw
        val setupPrm = dev.setupConfig.toRaw()
        val sysPrm = dev.systemConfig.toRawSysPrm()
        val kitChainPrm = dev.systemConfig.toRawKitChainPrm()
        val mEfctPrm = dev.systemConfig.toRawMEfctPrm()
        val config = Config(setupPrm, sysPrm, kitChainPrm, mEfctPrm)

        // Write system config (sysparam.spd with Root wrapper stripped)
        val systemDir = File("$rootPath/SYSTEM")
        xmlParser.writeSystemConfig(config, systemDir)

        // Write wave list files
        val rawWaveLists = dev.waveLists.toRaw()
        xmlParser.writeSystemFile(rawWaveLists.tagList, "tag_list.spd", systemDir)
        xmlParser.writeSystemFile(rawWaveLists.wvListSortbyName, "wavelist_name.spd", systemDir)
        xmlParser.writeSystemFile(rawWaveLists.wvListSortbyNameTag, "wavelist_tagname.spd", systemDir)
        xmlParser.writeSystemFile(rawWaveLists.wvListSortbyNumTag, "wavelist_tagnum.spd", systemDir)

        // Write kit files
        dev.kits.forEachIndexed { index, kit ->
            val kitFile = File("$rootPath/KIT/KIT_${"%03d".format(index)}.spd")
            xmlParser.writeKitFile(kit.toRaw(), kitFile)
        }

        // Write wave files
        dev.waves.forEach { wave ->
            val folder = (wave.number - 1) / 100
            val file = (wave.number - 1) % 100
            val waveFile = File("$rootPath/WAVE/PRM/%02d/%02d.spd".format(folder, file))
            xmlParser.writeWaveFile(wave.toRaw(), waveFile)
        }
    }
}
