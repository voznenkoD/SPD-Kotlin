package org.xebia.spdmanager.data

class NameConverter {

    fun decode(asciis: IntArray): String {
        val result = StringBuilder()

        for (ascii in asciis) {
            if (ascii != 0) {
                result.append(ascii.toChar())
            }
        }

        return result.toString()
    }

    fun encode(string: String): IntArray {
        return string.map { it.code }.toIntArray()
    }
}
