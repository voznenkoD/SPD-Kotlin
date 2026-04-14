package org.xebia.spdmanager.ui.components.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.*
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

enum class KnobMode { Unipolar, Bipolar }

private const val ARC_START = 135f
private const val ARC_SWEEP = 270f
private const val DRAG_PIXELS_FOR_FULL_RANGE = 250f

@Composable
fun KnobControl(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    mode: KnobMode = KnobMode.Unipolar,
    steps: Int? = null,
    valueDisplay: String? = null,
    modifier: Modifier = Modifier
) {
    val accentColor = ColorAccentOrange
    val trackColor = ColorDivider

    val fraction = if (valueRange.endInclusive == valueRange.start) 0f
    else (value - valueRange.start) / (valueRange.endInclusive - valueRange.start)

    val displayText = valueDisplay ?: value.toInt().toString()

    val currentValue by rememberUpdatedState(value)
    val currentOnValueChange by rememberUpdatedState(onValueChange)

    var isEditing by remember { mutableStateOf(false) }
    var editText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val scale = LocalScale.current
    val knobSize = (48 * scale).dp
    val columnWidth = (64 * scale).dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(columnWidth).padding(Spacing.m)
    ) {
        Text(
            text = label,
            style = Typography.caption,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = ColorTextSecondary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Spacing.s))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(knobSize)
                .pointerInput(valueRange, steps) {
                    detectVerticalDragGestures { _, dragAmount ->
                        val range = valueRange.endInclusive - valueRange.start
                        val delta = -(dragAmount / DRAG_PIXELS_FOR_FULL_RANGE) * range
                        var newValue = (currentValue + delta).coerceIn(valueRange.start, valueRange.endInclusive)
                        if (steps != null && steps > 0) {
                            val stepSize = range / steps
                            newValue = (((newValue - valueRange.start) / stepSize).toInt() * stepSize + valueRange.start)
                                .coerceIn(valueRange.start, valueRange.endInclusive)
                        }
                        currentOnValueChange(newValue)
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding((6 * scale).dp)) {
                val strokeWidth = (6 * scale).dp.toPx()
                val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

                drawArc(
                    color = trackColor,
                    startAngle = ARC_START,
                    sweepAngle = ARC_SWEEP,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                when (mode) {
                    KnobMode.Unipolar -> {
                        val fillSweep = ARC_SWEEP * fraction
                        if (fillSweep > 0.5f) {
                            drawArc(
                                color = accentColor,
                                startAngle = ARC_START,
                                sweepAngle = fillSweep,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }
                    }
                    KnobMode.Bipolar -> {
                        val centerFraction = if (valueRange.endInclusive == valueRange.start) 0.5f
                        else (0f - valueRange.start) / (valueRange.endInclusive - valueRange.start)
                        val centerAngle = ARC_START + ARC_SWEEP * centerFraction.coerceIn(0f, 1f)
                        val currentAngle = ARC_START + ARC_SWEEP * fraction
                        val sweep = currentAngle - centerAngle
                        if (abs(sweep) > 0.5f) {
                            drawArc(
                                color = accentColor,
                                startAngle = if (sweep >= 0) centerAngle else currentAngle,
                                sweepAngle = abs(sweep),
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }
                    }
                }

                val indicatorAngle = ARC_START + ARC_SWEEP * fraction
                val angleRad = indicatorAngle * (PI / 180f).toFloat()
                val cx = size.width / 2f
                val cy = size.height / 2f
                val radius = (arcSize.width / 2f)
                drawCircle(
                    color = accentColor,
                    radius = (4 * scale).dp.toPx(),
                    center = Offset(
                        cx + radius * cos(angleRad),
                        cy + radius * sin(angleRad)
                    )
                )
            }
        }

        Spacer(Modifier.height(Spacing.s))

        if (isEditing) {
            var hasBeenFocused by remember { mutableStateOf(false) }
            BasicTextField(
                value = editText,
                onValueChange = { editText = it },
                singleLine = true,
                textStyle = Typography.mono.copy(textAlign = TextAlign.Center),
                modifier = Modifier
                    .width((60 * scale).dp)
                    .border(1.dp, ColorDivider, ShapeDefault)
                    .padding(horizontal = Spacing.m, vertical = Spacing.m)
                    .focusRequester(focusRequester)
                    .onFocusChanged { state ->
                        if (state.isFocused) {
                            hasBeenFocused = true
                        } else if (hasBeenFocused && isEditing) {
                            commitEdit(editText, valueRange, steps, onValueChange)
                            isEditing = false
                        }
                    }
                    .onKeyEvent { event ->
                        if (event.type == KeyEventType.KeyDown) {
                            when (event.key) {
                                Key.Enter, Key.NumPadEnter -> {
                                    commitEdit(editText, valueRange, steps, onValueChange)
                                    isEditing = false
                                    focusManager.clearFocus()
                                    true
                                }
                                Key.Escape -> {
                                    isEditing = false
                                    focusManager.clearFocus()
                                    true
                                }
                                else -> false
                            }
                        } else false
                    }
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        } else {
            Text(
                text = displayText,
                style = Typography.mono,
                textAlign = TextAlign.Center,
                color = ColorTextPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        editText = displayText
                        isEditing = true
                    }
            )
        }
    }
}

private fun commitEdit(
    text: String,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int?,
    onValueChange: (Float) -> Unit
) {
    val parsed = text.toFloatOrNull() ?: return
    var clamped = parsed.coerceIn(valueRange.start, valueRange.endInclusive)
    if (steps != null && steps > 0) {
        val range = valueRange.endInclusive - valueRange.start
        val stepSize = range / steps
        clamped = (((clamped - valueRange.start) / stepSize).toInt() * stepSize + valueRange.start)
            .coerceIn(valueRange.start, valueRange.endInclusive)
    }
    onValueChange(clamped)
}
