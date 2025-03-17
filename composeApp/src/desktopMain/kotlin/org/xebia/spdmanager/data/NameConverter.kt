package org.xebia.spdmanager.data

fun decodeName(asciis: IntArray): String {
    val result = StringBuilder()

    for (ascii in asciis) {
        if (ascii != 0) {
            result.append(ascii.toChar())
        }
    }

    return result.toString()
}

fun encodeName(string: String): IntArray {
    return string.map { it.code }.toIntArray()
}

fun decodeTempo(tempo: Int): Double{
    return tempo / 10.0;
}

fun encodeTempo(tempo: Double): Int {
    return (tempo * 10).toInt()
}