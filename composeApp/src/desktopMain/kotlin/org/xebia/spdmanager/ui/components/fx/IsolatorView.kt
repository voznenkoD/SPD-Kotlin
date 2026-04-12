package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.Isolator
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IsolatorView(
    isolator: Isolator,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Isolator Settings", fontSize = 18.sp)

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SliderWithLabel(
                label = "Mid",
                value = isolator.mid,
                onValueChange = { newMid ->
                    onFxChange(isolator.copy(mid = newMid))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Level",
                value = isolator.level.toFloat(),
                onValueChange = { newLevel ->
                    onFxChange(isolator.copy(level = newLevel.toInt()))
                },
                valueRange = 0f..100f
            )
        }
    }
}