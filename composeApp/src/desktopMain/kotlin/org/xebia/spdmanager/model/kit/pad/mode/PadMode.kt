package org.xebia.spdmanager.model.kit.pad.mode

import org.xebia.spdmanager.model.system.fx.common.SyncSwitch

data class PadMode(
    val template: PadTemplate,
    val loop: PadLoop,
    val trigType: TrigType,
    val dynamics: SyncSwitch,
    val polyMono: PolyMono
) {
    /**
     * Returns this mode switched to [template], applying the preset values of Loop / TrigType /
     * Dynamics / Poly-Mono that go with each pad type. [PadTemplate.NONE] only changes the template
     * and leaves the other parameters untouched.
     *
     * Re-selecting the current template is a no-op (returns `this`): the preset is applied only on an
     * actual template change, so manually-tweaked params — and loop multipliers like x2/x4/x8 — are
     * preserved when the same type is selected again.
     */
    fun withTemplate(template: PadTemplate): PadMode {
        if (template == this.template) return this
        return presetFor(template) ?: copy(template = template)
    }

    /**
     * Snaps the template to [PadTemplate.NONE] when the current params no longer match the pad
     * type's preset. Call after changing Loop / TrigType / Dynamics / Poly-Mono so a manual tweak
     * away from a type's defaults clears the template. [PadTemplate.NONE] has no preset, so it is
     * left untouched.
     */
    fun normalized(): PadMode =
        if (matchesPreset()) this else copy(template = PadTemplate.NONE)

    /**
     * Whether the current params match the template's preset. For the LOOP type any loop multiplier
     * (x2/x4/x8) still counts as matching, since those are legitimate loop-on variants.
     */
    private fun matchesPreset(): Boolean {
        val preset = presetFor(template) ?: return true
        val loopMatches = loop == preset.loop ||
            (preset.loop == PadLoop.ON && loop != PadLoop.OFF)
        return loopMatches &&
            trigType == preset.trigType &&
            dynamics == preset.dynamics &&
            polyMono == preset.polyMono
    }

    /** The preset mode for [template], or `null` for [PadTemplate.NONE] which has no preset. */
    private fun presetFor(template: PadTemplate): PadMode? = when (template) {
        PadTemplate.SINGLE -> copy(
            template = template,
            loop = PadLoop.OFF,
            trigType = TrigType.SHOT,
            dynamics = SyncSwitch.ON,
            polyMono = PolyMono.POLY
        )
        PadTemplate.PHRASE -> copy(
            template = template,
            loop = PadLoop.OFF,
            trigType = TrigType.ALT,
            dynamics = SyncSwitch.OFF,
            polyMono = PolyMono.MONO
        )
        PadTemplate.LOOP -> copy(
            template = template,
            loop = PadLoop.ON,
            trigType = TrigType.ALT,
            dynamics = SyncSwitch.OFF,
            polyMono = PolyMono.MONO
        )
        PadTemplate.NONE -> null
    }

    companion object {
        fun fromValues(
            template: Int,
            loop: Int,
            trigType: Int,
            dynamics: Int,
            polyMono: Int
        ): PadMode {
            return PadMode(
                PadTemplate.fromValue(template),
                PadLoop.fromValue(loop),
                TrigType.fromValue(trigType),
                SyncSwitch.fromValue(dynamics),
                PolyMono.fromValue(polyMono)
            )
        }
    }
}
