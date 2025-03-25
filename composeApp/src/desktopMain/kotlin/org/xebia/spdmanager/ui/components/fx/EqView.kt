package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.EQ
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun EqView(fx: EQ) {
    var selectedLowCut by remember { mutableStateOf(fx.lowCut) }
    var lowGain by remember { mutableStateOf(fx.lowGain) }
    var selectedPkg1Freq by remember { mutableStateOf(fx.pkg1Freq) }
    var selectedPkg1Q by remember { mutableStateOf(fx.pkg1Q) }
    var pkg1Gain by remember { mutableStateOf(fx.pkg1Gain) }
    var selectedPkg2Freq by remember { mutableStateOf(fx.pkg2Freq) }
    var selectedPkg2Q by remember { mutableStateOf(fx.pkg2Q) }
    var pkg2Gain by remember { mutableStateOf(fx.pkg2Gain) }
    var hiGain by remember { mutableStateOf(fx.hiGain) }
    var selectedHiCut by remember { mutableStateOf(fx.hiCut) }
    var level by remember { mutableStateOf(fx.level) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("EQ Settings", fontSize = 18.sp)

        // Low Cut (Dropdown)
        DropdownSelector(
            label = "Low Cut",
            selectedItem = selectedLowCut,
            items = LowCut.entries.toList(),
            onItemSelected = { selectedLowCut = it }
        )

        // Low Gain (-15 to +15 dB)
        SliderWithLabel(
            label = "Low Gain",
            value = lowGain,
            onValueChange = { lowGain = it },
            valueRange = -15f..15f
        )

        // Package 1 Frequency (Dropdown)
        DropdownSelector(
            label = "Pkg1 Frequency",
            selectedItem = selectedPkg1Freq,
            items = EqFreq.entries.toList(),
            onItemSelected = { selectedPkg1Freq = it }
        )

        // Package 1 Q (Dropdown)
        DropdownSelector(
            label = "Pkg1 Q",
            selectedItem = selectedPkg1Q,
            items = EqQ.entries.toList(),
            onItemSelected = { selectedPkg1Q = it }
        )

        // Package 1 Gain (-15 to +15 dB)
        SliderWithLabel(
            label = "Pkg1 Gain",
            value = pkg1Gain,
            onValueChange = { pkg1Gain = it },
            valueRange = -15f..15f
        )

        // Package 2 Frequency (Dropdown)
        DropdownSelector(
            label = "Pkg2 Frequency",
            selectedItem = selectedPkg2Freq,
            items = EqFreq.entries.toList(),
            onItemSelected = { selectedPkg2Freq = it }
        )

        // Package 2 Q (Dropdown)
        DropdownSelector(
            label = "Pkg2 Q",
            selectedItem = selectedPkg2Q,
            items = EqQ.entries.toList(),
            onItemSelected = { selectedPkg2Q = it }
        )

        // Package 2 Gain (-15 to +15 dB)
        SliderWithLabel(
            label = "Pkg2 Gain",
            value = pkg2Gain,
            onValueChange = { pkg2Gain = it },
            valueRange = -15f..15f
        )

        // High Gain (-15 to +15 dB)
        SliderWithLabel(
            label = "High Gain",
            value = hiGain,
            onValueChange = { hiGain = it },
            valueRange = -15f..15f
        )

        // High Cut (Dropdown)
        DropdownSelector(
            label = "High Cut",
            selectedItem = selectedHiCut,
            items = HighCut.entries.toList(),
            onItemSelected = { selectedHiCut = it }
        )

        // Level (0 to 100)
        SliderWithLabel(
            label = "Level",
            value = level,
            onValueChange = { level = it },
            valueRange = 0f..100f
        )
    }
}
