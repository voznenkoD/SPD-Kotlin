package org.xebia.spdmanager.ui.components.wave

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.ui.components.common.KnobControl
import org.xebia.spdmanager.ui.theme.Spacing
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Editable wave parameter knobs (Tempo, Beat, Measure, Start, End). Every change is emitted via
 * [onWaveChange] as a fully-updated [Wave]; persistence is the caller's responsibility (memory only
 * here — saving happens through the central explicit save).
 *
 * Tempo is stored as raw = BPM × 10, so the knob operates in BPM (20.0–250.0) and converts back.
 * Start/End are sample indices kept ordered (0 ≤ start ≤ end ≤ totalSamples).
 */
@Composable
fun WaveParamKnobs(
    wave: Wave,
    totalSamples: Int,
    onWaveChange: (Wave) -> Unit,
    modifier: Modifier = Modifier
) {
    // Upper bounds kept >= the lower bound so KnobControl ranges and the coerceIn() calls below can
    // never form an empty range (min > max), which would otherwise throw on edit when stored sample
    // indices exceed the decoded WAV length (e.g. a missing/short file → totalSamples = 0).
    val startMax = maxOf(0, wave.end)
    val endMax = maxOf(wave.start, totalSamples)
    val tempoBpm = (wave.tempo / 10f).coerceIn(20f, 250f)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing.m),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KnobControl(
            label = "Tempo",
            value = tempoBpm,
            onValueChange = { bpm -> onWaveChange(wave.copy(tempo = (bpm * 10f).roundToInt())) },
            valueRange = 20f..250f,
            valueDisplay = String.format(Locale.US, "%.1f", tempoBpm),
            parseInput = { it.replace(',', '.').toFloatOrNull() }
        )
        KnobControl(
            label = "Beat",
            value = wave.beat.toFloat().coerceIn(1f, 16f),
            onValueChange = { onWaveChange(wave.copy(beat = it.roundToInt().coerceIn(1, 16))) },
            valueRange = 1f..16f,
            steps = 15,
            valueDisplay = wave.beat.coerceIn(1, 16).toString()
        )
        KnobControl(
            label = "Measure",
            value = wave.measure.toFloat().coerceIn(1f, 10f),
            onValueChange = { onWaveChange(wave.copy(measure = it.roundToInt().coerceIn(1, 10))) },
            valueRange = 1f..10f,
            steps = 9,
            valueDisplay = wave.measure.coerceIn(1, 10).toString()
        )
        KnobControl(
            label = "Start",
            value = wave.start.toFloat().coerceIn(0f, startMax.toFloat()),
            onValueChange = { onWaveChange(wave.copy(start = it.roundToInt().coerceIn(0, startMax))) },
            valueRange = 0f..startMax.toFloat(),
            valueDisplay = wave.start.toString()
        )
        KnobControl(
            label = "End",
            value = wave.end.toFloat().coerceIn(wave.start.toFloat(), endMax.toFloat()),
            onValueChange = { onWaveChange(wave.copy(end = it.roundToInt().coerceIn(wave.start, endMax))) },
            valueRange = wave.start.toFloat()..endMax.toFloat(),
            valueDisplay = wave.end.toString()
        )
    }
}
