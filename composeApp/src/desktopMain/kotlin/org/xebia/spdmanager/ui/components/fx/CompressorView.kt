package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.Compressor
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.ui.components.common.ButtonRowCompact
import org.xebia.spdmanager.ui.components.common.SliderWithLabel
import org.xebia.spdmanager.ui.components.common.ToggleSwitchWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompressorView(
    fx: Compressor,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(Spacing.m).fillMaxWidth()) {
        Text("Compressor Settings", fontSize = Typography.titleSize)

        ToggleSwitchWithLabel(
            label = "Knee",
            selectedItem = fx.knee,
            offItem = Knee.SOFT,
            onItem = Knee.HARD,
            onItemSelected = { newKnee ->
                onFxChange(fx.copy(knee = newKnee))
            },
            offLabel = "Soft",
            onLabel = "Hard"
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.m)) {
            SliderWithLabel(
                label = "Threshold",
                value = fx.threshold,
                onValueChange = { newThreshold ->
                    onFxChange(fx.copy(threshold = newThreshold))
                },
                valueRange = -80f..0f
            )

            SliderWithLabel(
                label = "Attack",
                value = fx.attack.toFloat(),
                onValueChange = { newAttack ->
                    onFxChange(fx.copy(attack = newAttack.toInt()))
                },
                valueRange = 1f..1000f
            )

            SliderWithLabel(
                label = "Release",
                value = fx.release.toFloat(),
                onValueChange = { newRelease ->
                    onFxChange(fx.copy(release = newRelease.toInt()))
                },
                valueRange = 1f..1000f
            )

            Box(modifier = Modifier.weight(1f)) {
                SliderWithLabel(
                    label = "Makeup Gain",
                    value = fx.makeup.toFloat(),
                    onValueChange = { newMakeup ->
                        onFxChange(fx.copy(makeup = newMakeup.toInt()))
                    },
                    valueRange = 0f..30f
                )
            }
        }

        ButtonRowCompact(
            label = "Ratio",
            selectedItem = fx.ratio,
            items = Ratio.entries.toTypedArray(),
            onItemSelected = { newRatio ->
                onFxChange(fx.copy(ratio = newRatio))
            }
        )
    }
}