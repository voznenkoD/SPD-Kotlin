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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    fun renameCategory(oldName: String, newName: String) {
        device?.let { currentDevice ->
            val updatedWaveLists = currentDevice.waveLists.renameCategory(oldName, newName)
            device = currentDevice.copy(waveLists = updatedWaveLists)
        }
    }

    /** Writes the four wave-list index files in SYSTEM/ from the given holder. */
    private fun writeWaveListIndexFiles(rootPath: String, waveLists: WaveListsHolder) {
        val systemDir = File("$rootPath/SYSTEM")
        val rawWaveLists = waveLists.toRaw()
        xmlParser.writeSystemFile(rawWaveLists.tagList, "tag_list.spd", systemDir)
        xmlParser.writeSystemFile(rawWaveLists.wvListSortbyName, "wavelist_name.spd", systemDir)
        xmlParser.writeSystemFile(rawWaveLists.wvListSortbyNameTag, "wavelist_tagname.spd", systemDir)
        xmlParser.writeSystemFile(rawWaveLists.wvListSortbyNumTag, "wavelist_tagnum.spd", systemDir)
    }

    /** The PRM (.spd) parameter file for a wave [number] at its number-derived location. */
    private fun wavePrmFile(rootPath: String, number: Int): File {
        val folderStr = "%02d".format((number - 1) / 100)
        val fileStr = "%02d".format((number - 1) % 100)
        return File("$rootPath/WAVE/PRM/$folderStr/$fileStr.spd")
    }

    /** Writes the single PRM (.spd) parameter file for [wave] at its number-derived location. */
    private fun writeWavePrmFile(rootPath: String, wave: Wave) {
        val prmFile = wavePrmFile(rootPath, wave.number)
        prmFile.parentFile?.mkdirs()
        xmlParser.writeWaveFile(wave.toRaw(), prmFile)
    }

    sealed class WaveOpResult {
        data object Success : WaveOpResult()
        data class Error(val message: String) : WaveOpResult()
    }

    /**
     * Renames wave [waveNumber] to [newName]. The supplied name is sanitized and trimmed to the
     * device's 12-char limit, then checked for uniqueness against the other waves. On success the
     * on-disk .wav audio file is renamed in place (path field updated), and the wave PRM .spd plus
     * the four wave-list index files are rewritten — mirroring importWave/deleteWave persistence.
     */
    fun renameWave(waveNumber: Int, newName: String): WaveOpResult {
        val currentDevice = device ?: return WaveOpResult.Error("No device loaded")
        if (currentDevice.rootPath.isBlank()) return WaveOpResult.Error("No device loaded")

        val wave = currentDevice.waves.find { it.number == waveNumber }
            ?: return WaveOpResult.Error("Wave #$waveNumber not found")

        val sanitized = sanitizeWaveName(newName).take(12)
        if (sanitized.isBlank()) return WaveOpResult.Error("Unable to derive a valid wave name")
        if (sanitized == wave.name) return WaveOpResult.Success
        if (currentDevice.waves.any { it.number != waveNumber && it.name == sanitized }) {
            return WaveOpResult.Error(
                "A wave named '$sanitized' already exists (names are limited to 12 characters)."
            )
        }

        // Rename the .wav audio in its existing DATA folder; keep the folder, swap the filename.
        val dir = wave.path.substringBeforeLast('/', "")
        val newFileName = "$sanitized.wav"
        val newRelativePath = if (dir.isBlank()) newFileName else "$dir/$newFileName"
        val oldDataFile = File("${currentDevice.rootPath}/WAVE/DATA/${wave.path}")
        val newDataFile = File("${currentDevice.rootPath}/WAVE/DATA/$newRelativePath")
        // Compare canonically so a case-only rename on a case-insensitive filesystem (e.g. macOS) is
        // treated as the same file rather than a spurious "already exists" conflict.
        val sameUnderlyingFile = runCatching { oldDataFile.canonicalFile == newDataFile.canonicalFile }.getOrDefault(false)
        val mustRename = oldDataFile.exists() && !sameUnderlyingFile
        if (mustRename && newDataFile.exists()) {
            return WaveOpResult.Error("A file named '$newFileName' already exists in WAVE/DATA/$dir.")
        }

        val updatedWave = wave.copy(name = sanitized, path = newRelativePath)
        val updatedWaves = currentDevice.waves.map { if (it.number == waveNumber) updatedWave else it }
        val updatedWaveLists = currentDevice.waveLists.withRenamedWave(waveNumber, sanitized)

        // Disk first: rename the audio, then write metadata. Roll the audio rename back if the
        // metadata write fails, and only commit the in-memory state once every write succeeds — so a
        // failure never leaves the UI showing a rename that did not fully persist.
        var renamed = false
        return try {
            if (mustRename) {
                if (!oldDataFile.renameTo(newDataFile)) {
                    oldDataFile.copyTo(newDataFile, overwrite = false)
                    oldDataFile.delete()
                }
                renamed = true
            }
            writeWavePrmFile(currentDevice.rootPath, updatedWave)
            writeWaveListIndexFiles(currentDevice.rootPath, updatedWaveLists)
            device = currentDevice.copy(waves = updatedWaves, waveLists = updatedWaveLists)
            WaveOpResult.Success
        } catch (e: Exception) {
            if (renamed) runCatching { newDataFile.renameTo(oldDataFile) }
            WaveOpResult.Error("Failed to rename wave: ${e.message}")
        }
    }

    /**
     * Moves wave [waveNumber] into the category named [categoryName] by updating its tagRef to the
     * category's order. Rewrites the wave PRM .spd (tag changed) and the four index files.
     */
    fun moveWaveToCategory(waveNumber: Int, categoryName: String): WaveOpResult {
        val currentDevice = device ?: return WaveOpResult.Error("No device loaded")
        if (currentDevice.rootPath.isBlank()) return WaveOpResult.Error("No device loaded")

        val wave = currentDevice.waves.find { it.number == waveNumber }
            ?: return WaveOpResult.Error("Wave #$waveNumber not found")
        val target = currentDevice.waveLists.wavesByNamePerCategory.keys.firstOrNull { it.name == categoryName }
            ?: return WaveOpResult.Error("Category '$categoryName' not found")
        if (wave.tagRef == target.order) return WaveOpResult.Success

        val updatedWave = wave.copy(tagRef = target.order)
        val updatedWaves = currentDevice.waves.map { if (it.number == waveNumber) updatedWave else it }
        val updatedWaveLists = currentDevice.waveLists.withMovedWaveToCategory(waveNumber, categoryName)

        // Disk first; commit the in-memory state only once the writes succeed.
        return try {
            writeWavePrmFile(currentDevice.rootPath, updatedWave)
            writeWaveListIndexFiles(currentDevice.rootPath, updatedWaveLists)
            device = currentDevice.copy(waves = updatedWaves, waveLists = updatedWaveLists)
            WaveOpResult.Success
        } catch (e: Exception) {
            WaveOpResult.Error("Failed to move wave: ${e.message}")
        }
    }

    sealed class ImportResult {
        data class Success(val wave: Wave) : ImportResult()
        data class Error(val message: String) : ImportResult()
    }

    fun importWave(sourceFile: File, categoryName: String): ImportResult {
        val currentDevice = device ?: return ImportResult.Error("No device loaded")
        if (currentDevice.rootPath.isBlank()) return ImportResult.Error("No device loaded")

        when (val v = WavValidator.validate(sourceFile)) {
            is WavValidationResult.Invalid -> return ImportResult.Error(v.message)
            WavValidationResult.Valid -> Unit
        }

        // Trim to the device's 12-char limit FIRST, then run the duplicate check against the trimmed
        // name so two differently-named sources that collapse to the same 12-char name are rejected.
        val sanitizedName = sanitizeWaveName(sourceFile.nameWithoutExtension).take(12)
        if (sanitizedName.isBlank()) {
            return ImportResult.Error("Unable to derive a valid wave name from filename")
        }
        if (currentDevice.waves.any { it.name == sanitizedName }) {
            return ImportResult.Error(
                "A wave named '$sanitizedName' already exists (names are limited to 12 characters). " +
                    "Please rename the source file and try again."
            )
        }

        val nextNumber = (currentDevice.waves.maxOfOrNull { it.number } ?: 0) + 1
        if (nextNumber > 1000) {
            return ImportResult.Error("Wave library is full (max 1000 waves).")
        }
        val folderStr = "%02d".format((nextNumber - 1) / 100)
        val fileStr = "%02d".format((nextNumber - 1) % 100)

        val tag = currentDevice.waveLists.wavesByNamePerCategory.keys
            .firstOrNull { it.name == categoryName }?.order ?: 0

        // Use the already-sanitized, 12-char-trimmed name (the device limit) for the on-disk file too,
        // so the audio filename and the metadata name match and both respect the 12-char limit.
        val sanitizedFilename = "$sanitizedName.wav"
        val relativePath = "$folderStr/$sanitizedFilename"

        return try {
            val dataFolder = File("${currentDevice.rootPath}/WAVE/DATA/$folderStr")
            dataFolder.mkdirs()
            val dataFile = File(dataFolder, sanitizedFilename)
            if (dataFile.exists()) {
                return ImportResult.Error("A file named '$sanitizedFilename' already exists in WAVE/DATA/$folderStr.")
            }
            sourceFile.copyTo(dataFile, overwrite = false)

            val prmFolder = File("${currentDevice.rootPath}/WAVE/PRM/$folderStr")
            prmFolder.mkdirs()
            val prmFile = File(prmFolder, "$fileStr.spd")

            val newWave = Wave(
                number = nextNumber,
                name = sanitizedName,
                path = relativePath,
                tagRef = tag,
                tempo = 0, beat = 0, measure = 0, start = 0, end = 0
            )
            xmlParser.writeWaveFile(newWave.toRaw(), prmFile)

            val updatedWaves = currentDevice.waves + newWave
            val updatedWaveLists = currentDevice.waveLists.withAddedWave(newWave, categoryName)
            device = currentDevice.copy(waves = updatedWaves, waveLists = updatedWaveLists)

            writeWaveListIndexFiles(currentDevice.rootPath, updatedWaveLists)

            ImportResult.Success(newWave)
        } catch (e: Exception) {
            ImportResult.Error("Failed to write wave files: ${e.message}")
        }
    }

    private fun sanitizeWaveName(input: String): String =
        input.replace(Regex("[^A-Za-z0-9 _-]"), "_").trim()

    sealed class DeleteResult {
        data object Success : DeleteResult()
        data class Error(val message: String) : DeleteResult()
        data class InUse(val kitNames: List<String>) : DeleteResult()
    }

    fun deleteWave(waveNumber: Int): DeleteResult {
        val currentDevice = device ?: return DeleteResult.Error("No device loaded")
        if (currentDevice.rootPath.isBlank()) return DeleteResult.Error("No device loaded")

        val wave = currentDevice.waves.find { it.number == waveNumber }
            ?: return DeleteResult.Error("Wave #$waveNumber not found")

        val usage = buildWaveUsageMap(currentDevice.kits)[waveNumber].orEmpty()
        if (usage.isNotEmpty()) {
            return DeleteResult.InUse(usage)
        }

        val folderStr = "%02d".format((wave.number - 1) / 100)
        val fileStr = "%02d".format((wave.number - 1) % 100)
        val prmFile = File("${currentDevice.rootPath}/WAVE/PRM/$folderStr/$fileStr.spd")
        val dataFile = File("${currentDevice.rootPath}/WAVE/DATA/${wave.path}")

        runCatching { if (prmFile.exists()) prmFile.delete() }
        runCatching { if (dataFile.exists()) dataFile.delete() }

        val updatedWaves = currentDevice.waves.filterNot { it.number == waveNumber }
        val updatedWaveLists = currentDevice.waveLists.withRemovedWave(waveNumber)
        device = currentDevice.copy(waves = updatedWaves, waveLists = updatedWaveLists)

        return try {
            writeWaveListIndexFiles(currentDevice.rootPath, updatedWaveLists)
            DeleteResult.Success
        } catch (e: Exception) {
            DeleteResult.Error("Failed to update wave list index files: ${e.message}")
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

    /**
     * Appends a copy of the kit at [sourceIndex] (renamed to [newName]) to the end of the kit list
     * and returns the new kit's index, or null if [sourceIndex] is invalid or the list already holds
     * [MAX_KITS] kits. Append-only insertion keeps existing indices stable, so KitChain references
     * stay valid.
     */
    fun duplicateKit(sourceIndex: Int, newName: String): Int? {
        val currentDevice = device ?: return null
        val source = currentDevice.kits.getOrNull(sourceIndex) ?: return null
        if (currentDevice.kits.size >= MAX_KITS) return null
        val kits = currentDevice.kits.toMutableList().apply { add(source.copy(name = newName)) }
        device = currentDevice.copy(kits = kits)
        return kits.lastIndex
    }

    companion object {
        /** Maximum number of kits the device can hold (kit numbers 1..99). */
        const val MAX_KITS = 99

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
        if (dev.rootPath.isBlank()) return
        saveDeviceTo(dev, dev.rootPath)
    }

    /** Writes the given device's in-memory metadata (.spd/system files) into [rootPath]. */
    private fun saveDeviceTo(dev: Device, rootPath: String) {
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
        writeWaveListIndexFiles(rootPath, dev.waveLists)

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

    sealed class SaveAsResult {
        object Success : SaveAsResult()
        data class Error(val message: String) : SaveAsResult()
    }

    /**
     * Copies the full current device (all .wav audio + every metadata file) to [targetRootPath],
     * writes the current in-memory metadata into the copy (capturing unsaved edits), then rebases
     * the current device to the new location. The original location is left untouched.
     *
     * Refuses if the target already exists and is non-empty. [onProgress] reports 0f..1f by bytes.
     */
    suspend fun saveDeviceAs(targetRootPath: String, onProgress: (Float) -> Unit): SaveAsResult {
        val currentDevice = device ?: return SaveAsResult.Error("No device loaded")
        val sourceRoot = currentDevice.rootPath
        if (sourceRoot.isBlank()) return SaveAsResult.Error("No device loaded")

        // canonicalFile can throw IOException (e.g. unmounted/broken path); resolve safely and return
        // an Error rather than letting it escape this suspend fun (which would leave the UI stuck).
        val source = runCatching { File(sourceRoot).canonicalFile }.getOrNull()
            ?: return SaveAsResult.Error("Cannot resolve the current device folder.")
        val target = runCatching { File(targetRootPath).canonicalFile }.getOrNull()
            ?: return SaveAsResult.Error("Cannot resolve the target folder:\n$targetRootPath")

        // Never copy into the source itself or a folder nested under it — that would alter the
        // original device (violating "original untouched") and make walkTopDown duplicate endlessly.
        if (target == source || target.path.startsWith(source.path + File.separator)) {
            return SaveAsResult.Error("Target must be outside the current device folder.")
        }
        // Refuse a target that already exists as a file or as a non-empty directory.
        if (target.exists() && (target.isFile || target.listFiles()?.isNotEmpty() == true)) {
            return SaveAsResult.Error("Target already exists and is not empty:\n${target.path}")
        }

        // Only roll back (delete) a target folder we actually created, never pre-existing user data.
        val createdTarget = !target.exists()

        return try {
            withContext(Dispatchers.IO) {
                val files = source.walkTopDown().filter { it.isFile }.toList()
                val totalBytes = files.sumOf { it.length() }.coerceAtLeast(1L)
                var copiedBytes = 0L
                onProgress(0f)
                for (file in files) {
                    val relative = file.relativeTo(source).path
                    val destFile = File(target, relative)
                    destFile.parentFile?.mkdirs()
                    file.copyTo(destFile, overwrite = true)
                    copiedBytes += file.length()
                    onProgress((copiedBytes.toFloat() / totalBytes).coerceIn(0f, 1f))
                }
                // Overwrite the copied metadata with the captured device model (captures unsaved edits).
                saveDeviceTo(currentDevice, target.path)
            }
            withContext(Dispatchers.Main) {
                device = currentDevice.copy(rootPath = target.path)
            }
            onProgress(1f)
            SaveAsResult.Success
        } catch (e: Exception) {
            // Don't leave a half-copied, corrupt device behind.
            if (createdTarget) runCatching { target.deleteRecursively() }
            SaveAsResult.Error("Save As failed: ${e.message}")
        }
    }
}
