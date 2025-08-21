package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.Compressor
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun CompressorView(
    fx: Compressor,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Compressor Settings", fontSize = 18.sp)

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

        ButtonRow(
            label = "Ratio",
            selectedItem = fx.ratio,
            items = Ratio.entries.toTypedArray(),
            onItemSelected = { newRatio ->
                onFxChange(fx.copy(ratio = newRatio))
            }
        )

        ButtonRow(
            label = "Knee",
            selectedItem = fx.knee,
            items = Knee.entries.toTypedArray(),
            onItemSelected = { newKnee ->
                onFxChange(fx.copy(knee = newKnee))
            }
        )

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