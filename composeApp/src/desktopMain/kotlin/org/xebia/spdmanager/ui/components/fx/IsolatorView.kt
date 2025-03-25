package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.subtypes.Isolator
import org.xebia.spdmanager.ui.components.SliderWithLabel

@Composable
fun IsolatorView(isolator: Isolator) {
    var mid by remember { mutableStateOf(isolator.mid) }
    var level by remember { mutableStateOf(isolator.level) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Isolator Settings", fontSize = 18.sp)

        // Mid (Slider)
        SliderWithLabel(
            label = "Mid",
            value = mid,
            onValueChange = { mid = it },
            valueRange = 0f..100f
        )

        // Level (Slider)
        SliderWithLabel(
            label = "Level",
            value = level.toFloat(),
            onValueChange = { level = it.toInt() },
            valueRange = 0f..100f
        )
    }
}