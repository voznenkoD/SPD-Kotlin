package org.xebia.spdmanager.model.system.fx

import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.mainTypes.*

/**
 * Preset ↔ parameter linking for the master effects (FILTER, DELAY, S.LOOP).
 *
 * Each preset (the device "TYPE") defines a set of **structural** switch values — and only those
 * (see documentation/master-fx-presets.md). Continuous parameters (cutoff, resonance, depths,
 * levels, …) are never part of a preset and are left untouched.
 *
 * Two operations per effect:
 *  - [applyPreset]: set the effect's structural values to the chosen preset's values (converting the
 *    note/numeric rate representation across a sync change), preserving every continuous parameter.
 *  - [reconcilePreset]: after a structural control is edited, if the effect no longer matches its
 *    currently-selected preset, snap the preset field back to index 0 (the first preset). It only
 *    relabels the preset; it never rewrites the user's just-edited parameters.
 *
 * FX is intentionally excluded — its TYPE is the effect itself and has no preset/params split.
 */

// ---------------------------------------------------------------------------
// FILTER — defining structural values: { type, rateSync }
// ---------------------------------------------------------------------------

/** The (filter type, rate-sync) pair a [FilterPreset] stands for. */
private fun FilterPreset.defining(): Pair<FilterType, SyncSwitch> = when (this) {
    FilterPreset.SIMPLE_LPF -> FilterType.LOW_PASS to SyncSwitch.OFF
    FilterPreset.SIMPLE_BPF -> FilterType.BAND_PASS to SyncSwitch.OFF
    FilterPreset.SIMPLE_HPF -> FilterType.HIGH_PASS to SyncSwitch.OFF
    FilterPreset.LPF_SYNC_MOD -> FilterType.LOW_PASS to SyncSwitch.ON
    FilterPreset.BPF_SYNC_MOD -> FilterType.BAND_PASS to SyncSwitch.ON
    FilterPreset.HPF_SYNC_MOD -> FilterType.HIGH_PASS to SyncSwitch.ON
}

private fun FilterPreset.matches(e: FilterEffect): Boolean = defining() == (e.type to e.rateSync)

/** Mod-rate representation for a target sync state, mirroring the sync-switch UI conversion. */
private fun FilterEffect.modRateForSync(target: SyncSwitch): ModRate = when {
    target == rateSync -> modRate
    target == SyncSwitch.ON -> ModRate.EnumRate(ModRateEnum.fromIndex(0))
    else -> ModRate.IntRate((modRate as? ModRate.EnumRate)?.modRateEnum?.ordinal ?: 0)
}

fun FilterEffect.applyPreset(preset: FilterPreset): FilterEffect {
    val (type, sync) = preset.defining()
    return copy(
        preset = preset,
        type = type,
        rateSync = sync,
        modRate = modRateForSync(sync)
    )
}

/** Snap the preset to index 0 if the structural values no longer match the selected preset. */
fun FilterEffect.reconcilePreset(): FilterEffect =
    if (preset.matches(this)) this else copy(preset = FilterPreset.entries.first())

// ---------------------------------------------------------------------------
// DELAY — defining structural values: { type, syncSW, delay-time note (PAN presets only) }
// ---------------------------------------------------------------------------

private fun DelayEffect.noteIs(note: DelayTimeEnum): Boolean =
    (delayTime as? DelayTime.EnumTime)?.delayTimeEnum == note

private fun DelayPreset.matches(e: DelayEffect): Boolean = when (this) {
    DelayPreset.STEREO_NORMAL -> e.type == DelayType.NORMAL && e.syncSW == SyncSwitch.OFF
    DelayPreset.STEREO_SYNC -> e.type == DelayType.NORMAL && e.syncSW == SyncSwitch.ON
    DelayPreset.PAN_QUARTER -> e.type == DelayType.PAN && e.syncSW == SyncSwitch.ON && e.noteIs(DelayTimeEnum.QUARTER)
    // PAN_DOUBLE shares PAN_DOTTED_EIGHT's modeled values (doubling is not a stored parameter).
    DelayPreset.PAN_DOTTED_EIGHT,
    DelayPreset.PAN_DOUBLE -> e.type == DelayType.PAN && e.syncSW == SyncSwitch.ON && e.noteIs(DelayTimeEnum.EIGHTH_DOTTED)
}

/** Preserve a numeric delay time across a sync change, mirroring the sync-switch UI conversion. */
private fun DelayEffect.intTimePreserving(): DelayTime.IntTime =
    DelayTime.IntTime((delayTime as? DelayTime.IntTime)?.intTime
        ?: (delayTime as? DelayTime.EnumTime)?.delayTimeEnum?.ordinal ?: 0)

/** Preserve/seed a note delay time when entering a synced preset that doesn't pin a note. */
private fun DelayEffect.enumTimePreserving(): DelayTime.EnumTime =
    DelayTime.EnumTime((delayTime as? DelayTime.EnumTime)?.delayTimeEnum ?: DelayTimeEnum.fromIndex(0))

fun DelayEffect.applyPreset(preset: DelayPreset): DelayEffect = when (preset) {
    DelayPreset.STEREO_NORMAL -> copy(preset = preset, type = DelayType.NORMAL, syncSW = SyncSwitch.OFF, delayTime = intTimePreserving())
    DelayPreset.STEREO_SYNC -> copy(preset = preset, type = DelayType.NORMAL, syncSW = SyncSwitch.ON, delayTime = enumTimePreserving())
    DelayPreset.PAN_QUARTER -> copy(preset = preset, type = DelayType.PAN, syncSW = SyncSwitch.ON, delayTime = DelayTime.EnumTime(DelayTimeEnum.QUARTER))
    DelayPreset.PAN_DOTTED_EIGHT,
    DelayPreset.PAN_DOUBLE -> copy(preset = preset, type = DelayType.PAN, syncSW = SyncSwitch.ON, delayTime = DelayTime.EnumTime(DelayTimeEnum.EIGHTH_DOTTED))
}

fun DelayEffect.reconcilePreset(): DelayEffect =
    if (preset.matches(this)) this else copy(preset = DelayPreset.entries.first())

// ---------------------------------------------------------------------------
// S.LOOP — defining structural values: { mode, rateSync, rate note (AUTO note presets only) }
// ---------------------------------------------------------------------------

private fun SLoopEffect.rateIs(rate: SLoopRateEnum): Boolean =
    (this.rate as? SLoopRate.EnumRate)?.rateEnum == rate

private fun SLoopPreset.matches(e: SLoopEffect): Boolean = when (this) {
    SLoopPreset.MANUAL -> e.mode == SLoopMode.MANUAL
    SLoopPreset.AUTO_QUARTER -> e.mode == SLoopMode.AUTO && e.rateSync == SyncSwitch.ON && e.rateIs(SLoopRateEnum.QUARTER)
    SLoopPreset.AUTO_EIGHT -> e.mode == SLoopMode.AUTO && e.rateSync == SyncSwitch.ON && e.rateIs(SLoopRateEnum.EIGHTH)
    SLoopPreset.AUTO_SIXTEEN -> e.mode == SLoopMode.AUTO && e.rateSync == SyncSwitch.ON && e.rateIs(SLoopRateEnum.SIXTEENTH)
    SLoopPreset.AUTO_FREERUN -> e.mode == SLoopMode.AUTO && e.rateSync == SyncSwitch.OFF
}

private fun SLoopEffect.intRatePreserving(): SLoopRate.IntRate =
    SLoopRate.IntRate((rate as? SLoopRate.IntRate)?.intRate
        ?: (rate as? SLoopRate.EnumRate)?.rateEnum?.ordinal ?: 0)

fun SLoopEffect.applyPreset(preset: SLoopPreset): SLoopEffect = when (preset) {
    // MANUAL: rate is knob-driven; only the mode is pinned, leave rate/rateSync as they are.
    SLoopPreset.MANUAL -> copy(preset = preset, mode = SLoopMode.MANUAL)
    SLoopPreset.AUTO_QUARTER -> copy(preset = preset, mode = SLoopMode.AUTO, rateSync = SyncSwitch.ON, rate = SLoopRate.EnumRate(SLoopRateEnum.QUARTER))
    SLoopPreset.AUTO_EIGHT -> copy(preset = preset, mode = SLoopMode.AUTO, rateSync = SyncSwitch.ON, rate = SLoopRate.EnumRate(SLoopRateEnum.EIGHTH))
    SLoopPreset.AUTO_SIXTEEN -> copy(preset = preset, mode = SLoopMode.AUTO, rateSync = SyncSwitch.ON, rate = SLoopRate.EnumRate(SLoopRateEnum.SIXTEENTH))
    SLoopPreset.AUTO_FREERUN -> copy(preset = preset, mode = SLoopMode.AUTO, rateSync = SyncSwitch.OFF, rate = intRatePreserving())
}

fun SLoopEffect.reconcilePreset(): SLoopEffect =
    if (preset.matches(this)) this else copy(preset = SLoopPreset.entries.first())
