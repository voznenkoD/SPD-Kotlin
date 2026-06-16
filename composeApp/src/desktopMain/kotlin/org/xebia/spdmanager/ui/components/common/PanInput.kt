package org.xebia.spdmanager.ui.components.common

/**
 * Parses a pan value typed in the same format the pan knobs display:
 * "C" (center) -> 0, "L20" -> -20, "R5" -> 5. Plain signed numbers ("-5", "5")
 * are also accepted. Returns null when the text isn't a recognizable pan value.
 *
 * The returned value is unclamped — [KnobControl] clamps it to the knob's range,
 * so out-of-range input like "L20" is accepted and pinned to the limit ("L15").
 */
fun parsePanInput(text: String): Float? {
    val t = text.trim().uppercase()
    if (t.isEmpty()) return null
    return when (t.first()) {
        'C' -> if (t == "C") 0f else null
        'L' -> t.drop(1).trim().toIntOrNull()?.let { -it.toFloat() }
        'R' -> t.drop(1).trim().toIntOrNull()?.let { it.toFloat() }
        else -> t.toFloatOrNull()
    }
}