package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography
import org.xebia.spdmanager.model.system.fx.common.ChorusMode
import org.xebia.spdmanager.model.system.fx.common.HighCut
import org.xebia.spdmanager.model.system.fx.common.LowCut
import org.xebia.spdmanager.model.system.fx.subtypes.Chorus
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.ui.components.common.ButtonRowCompact
import org.xebia.spdmanager.ui.components.common.IntStepSliderWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChorusView(
    chorus: Chorus,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(Spacing.m).fillMaxWidth()) {
        Text("Chorus", fontSize = Typography.titleSize)

        ButtonRowCompact(
            label = "Mode",
            selectedItem = chorus.mode,
            items = ChorusMode.entries.toTypedArray(),
            onItemSelected = { newMode ->
                onFxChange(chorus.copy(mode = newMode))
            }
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
            IntStepSliderWithLabel(
                label = "Rate",
                value = chorus.rate,
                onValueChange = { newRate ->
                    onFxChange(chorus.copy(rate = newRate))
                },
                range = 0..100
            )

            IntStepSliderWithLabel(
                label = "Pre-Delay",
                value = chorus.preDelay,
                onValueChange = { newPreDelay ->
                    onFxChange(chorus.copy(preDelay = newPreDelay))
                },
                range = 0..100
            )

            IntStepSliderWithLabel(
                label = "Direct Level",
                value = chorus.directLevel,
                onValueChange = { newDirectLevel ->
                    onFxChange(chorus.copy(directLevel = newDirectLevel))
                },
                range = 0..100
            )
        }

        ButtonRowCompact(
            label = "Low Cut (Hz)",
            selectedItem = chorus.lowCut,
            items = LowCut.entries.toTypedArray(),
            onItemSelected = { newLowCut ->
                onFxChange(chorus.copy(lowCut = newLowCut))
            }
        )

        ButtonRowCompact(
            label = "High Cut (Hz)",
            selectedItem = chorus.highCut,
            items = HighCut.entries.toTypedArray(),
            onItemSelected = { newHighCut ->
                onFxChange(chorus.copy(highCut = newHighCut))
            }
        )
    }
}