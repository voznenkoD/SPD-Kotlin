package org.xebia.spdmanager.data

data class Coordinate(val folderNumber: String, val fileNumber: String) {
    val waveNumber: Int
        get() = folderNumber.toInt() * 100 + fileNumber.filter { it.isDigit() }.toInt() + 1
}