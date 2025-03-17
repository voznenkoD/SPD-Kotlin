package org.xebia.spdmanager.service

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.xebia.spdmanager.data.Coordinate
import org.xebia.spdmanager.data.model.raw.kit.KitPrm
import org.xebia.spdmanager.data.model.raw.system.Config
import org.xebia.spdmanager.data.model.raw.wave.WvPrm
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

class XmlParser {
    val xmlMapper = XmlMapper().apply {
        registerKotlinModule()
    }

    fun readFilesInFolder(folderPath: String): List<File> {
        val folder = File(folderPath)

        if (!folder.exists() || !folder.isDirectory) {
            throw IllegalArgumentException("Invalid folder path: $folderPath")
        }
        return folder.listFiles()?.filter { it.isFile } ?: emptyList()
    }

    inline fun <reified T> parseFile(file:File) =  xmlMapper.readValue<T>(file)

    fun parseKits(kitFiles: List<File>): List<KitPrm> = kitFiles.map(::parseFile)

    inline fun <reified T> parseSystemFile(filename: String, systemFiles: List<File>) =
        systemFiles.firstOrNull{ file ->
            file.name == filename
        }?.let { file ->
            parseFile<T>(file)
        }
    
    fun parseSystemConfig(systemFiles:List<File>) =
        systemFiles.firstOrNull{ file ->
            file.name == "sysparam.spd"
        }?.let { file ->
            val xmlContent = String(Files.readAllBytes(Path(file.path)))
            val wrappedXml = "<Root>$xmlContent</Root>"
            xmlMapper.readValue(wrappedXml, Config::class.java)
        }

    fun parseWaves(waveFilesDir: String): Map<Coordinate, WvPrm> {
        val folder = File(waveFilesDir)
        if (!folder.exists()) {
            throw IllegalArgumentException("Invalid folder path: $waveFilesDir")
        }

        return folder.walk()
            .filter { it.isDirectory }
            .flatMap { dir ->
                val parent = dir.name
                dir.listFiles()
                    ?.filter { it.isFile }
                    ?.map { file -> Coordinate(parent, file.name) to parseFile<WvPrm>(file) }
                    ?: emptyList()
            }
            .sortedWith(compareBy(
                { it.first.folderNumber.toIntOrNull() ?: Int.MAX_VALUE },
                { it.first.fileNumber.toIntOrNull() ?: Int.MAX_VALUE }
            ))
            .toMap()
    }
}
