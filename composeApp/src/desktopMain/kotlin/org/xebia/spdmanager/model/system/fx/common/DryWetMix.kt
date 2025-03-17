package org.xebia.spdmanager.model.system.fx.common

data class DryWetMix(val dry: Int, val wet: Int) {
    override fun toString(): String {
        return "D$dry:${wet}W"
    }

    companion object {
        fun fromInt(value: Int): DryWetMix {
            val clampedValue = value.coerceIn(0, 200) // Ensure it's within range
            val dry = 100 - (clampedValue / 2)
            val wet = clampedValue / 2
            return DryWetMix(dry, wet)
        }
    }
}
