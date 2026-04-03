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
    val xmlMapper = XmlMapper.builder()
        .defaultUseWrapper(false)
        .build()
        .apply {
            registerKotlinModule()
        }

    fun readFilesInFolder(folderPath: String): List<File> {
        val folder = File(folderPath)

        if (!folder.exists() || !folder.isDirectory) {
            throw IllegalArgumentException("Invalid folder path: $folderPath")
        }
        return folder.listFiles()
            ?.filter { it.isFile && !it.name.startsWith(".") }
            ?.sortedBy { it.name }
            ?: emptyList()    }

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

    fun writeKitFile(kitPrm: KitPrm, file: File) {
        xmlMapper.writeValue(file, kitPrm)
    }

    fun writeSystemConfig(config: Config, systemDir: File) {
        val xml = xmlMapper.writeValueAsString(config)
        // Strip the <Root> wrapper — sysparam.spd stores raw elements without a root tag
        val inner = xml
            .replaceFirst(Regex("^<Root[^>]*>"), "")
            .replaceFirst(Regex("</Root>$"), "")
        File(systemDir, "sysparam.spd").writeText(inner)
    }

    fun <T> writeSystemFile(data: T, filename: String, systemDir: File) {
        xmlMapper.writeValue(File(systemDir, filename), data)
    }

    fun writeWaveFile(wvPrm: WvPrm, file: File) {
        xmlMapper.writeValue(file, wvPrm)
    }
}
