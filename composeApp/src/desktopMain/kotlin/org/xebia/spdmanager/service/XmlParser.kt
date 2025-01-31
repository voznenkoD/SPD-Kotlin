package org.xebia.spdmanager.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xebia.spdmanager.data.model.raw.kit.KitPrm
import org.xebia.spdmanager.data.model.raw.system.Config
import org.xebia.spdmanager.data.model.raw.system.TagList
import org.xebia.spdmanager.data.model.raw.system.WvListSortbyName
import org.xebia.spdmanager.data.model.raw.system.WvListSortbyNameTag
import org.xebia.spdmanager.data.model.raw.system.WvListSortbyNumTag
import org.xebia.spdmanager.data.model.raw.wave.WvPrm
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

class XmlParser {
    private val xmlMapper = XmlMapper().apply {
        registerKotlinModule()
    }

    private fun readFilesInFolder(folderPath: String): List<File> {
        val folder = File(folderPath)

        if (!folder.exists() || !folder.isDirectory) {
            throw IllegalArgumentException("Invalid folder path: $folderPath")
        }
        return folder.listFiles()?.filter { it.isFile } ?: emptyList()
    }

    private inline fun <reified T> parseFile(file:File) =  xmlMapper.readValue<T>(file)

    private fun parseKits(kitFiles: List<File>): List<KitPrm> = kitFiles.map(::parseFile)

    private inline fun <reified T> parseSystemFile(filename: String, systemFiles: List<File>) =
        systemFiles.firstOrNull{ file ->
            file.name == filename
        }?.let { file ->
            parseFile<T>(file)
        }
    
    private fun parseSystemConfig(systemFiles:List<File>) =
        systemFiles.firstOrNull{ file ->
            file.name == "sysparam.spd"
        }?.let { file ->
            val xmlContent = String(Files.readAllBytes(Path(file.path)))
            val wrappedXml = "<Root>$xmlContent</Root>"
            xmlMapper.readValue(wrappedXml, Config::class.java)
        }

    /*
      -Folder
      --Folder
      ---File
      --Folder
      ---File
     */
    private fun parseWaves(waveFilesDir: String): Sequence<Pair<Coordinate, WvPrm>> {
        val folder = File(waveFilesDir)
        if (!folder.exists()) {
            throw IllegalArgumentException("Invalid folder path: $waveFilesDir")
        }
        return folder.walk().mapNotNull { file ->
            if (file.isDirectory){
                val parentName = file.name
                file.listFiles()?.filter { it.isFile }?.map { subFile ->
                    Coordinate(parentName, subFile.name) to parseFile<WvPrm>(subFile)
                }
            } else null
        }.flatten()
    }

    fun parseAllFiles(rootPath: String) {
        val classTypeToFilename = mapOf(
            Config::class.java to "sysparam.spd",
            TagList::class.java to "tag_list.spd",
            WvListSortbyName::class.java to "wavelist_name.spd",
            WvListSortbyNameTag::class.java to "wavelist_tagname.spd",
            WvListSortbyNumTag::class.java to "wavelist_tagnum.spd"
        )

        val systemFiles = readFilesInFolder("$rootPath/SYSTEM")
        val kitFiles = readFilesInFolder("$rootPath/KIT")

        val rawKits = parseKits(kitFiles)
        val rawSystemConfig = parseSystemConfig(systemFiles)
        val rawTagList = parseSystemFile<TagList>(classTypeToFilename[TagList::class.java]!!, systemFiles)
        val rawWvListSortByName = parseSystemFile<WvListSortbyName>(classTypeToFilename[WvListSortbyName::class.java]!!, systemFiles)
        val rawWvListSortByNameTag = parseSystemFile<WvListSortbyNameTag>(classTypeToFilename[WvListSortbyNameTag::class.java]!!, systemFiles)
        val rawWvListSortByNumTag = parseSystemFile<WvListSortbyNumTag>(classTypeToFilename[WvListSortbyNumTag::class.java]!!, systemFiles)
        val rawWaves = parseWaves("$rootPath/WAVE/PRM")

        println("hehe")

    }

    data class Coordinate(val folderNumber: String, val fileNumber: String)

}
