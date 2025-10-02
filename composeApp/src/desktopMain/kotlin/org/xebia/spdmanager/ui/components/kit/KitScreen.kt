package org.xebia.spdmanager.ui.components.kit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.fx.KitFX
import org.xebia.spdmanager.model.kit.pad.Pad
import org.xebia.spdmanager.model.kit.pad.PadNumber
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.service.DeviceManager
import org.xebia.spdmanager.ui.components.common.IntStepSliderWithLabel
import org.xebia.spdmanager.ui.components.common.SliderWithLabel
import org.xebia.spdmanager.ui.components.pad.PadDetailsScreen
import org.xebia.spdmanager.viewmodel.KitViewModel

@Composable
fun KitScreen(
    kitIndex: Int,
    deviceManager: DeviceManager
) {
    val viewModel = remember(kitIndex) {
        KitViewModel(kitIndex, deviceManager)
    }

    val kit = viewModel.kit

    if (kit == null) {
        Text("Kit not found", modifier = Modifier.padding(16.dp))
        return
    }

    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("Name: ", fontSize = 14.sp, modifier = Modifier.weight(0.5f))
            TextField(
                value = kit.name,
                onValueChange = viewModel::updateName,
                modifier = Modifier.weight(1f)
            )
            Text("Sub Name: ", fontSize = 14.sp, modifier = Modifier.weight(0.5f))
            TextField(
                value = kit.subName,
                onValueChange = viewModel::updateSubName,
                modifier = Modifier.weight(1f)
            )
        }

        SliderWithLabel(
            label = "Tempo",
            value = kit.tempo.toFloat(),
            onValueChange = { viewModel.updateTempo(it.toDouble()) },
            valueRange = 20f..260f
        )

        IntStepSliderWithLabel(
            label = "Volume",
            value = kit.volume,
            onValueChange = viewModel::updateVolume,
            range = 0..100
        )

        PadLinkSelector(
            padLink1 = kit.padLink?.first ?: PadNumber.PAD_1,
            onPadLink1Selected = { pad1 ->
                viewModel.updatePadLink(
                    pad1,
                    kit.padLink?.second ?: PadNumber.PAD_2
                )
            },
            padLink2 = kit.padLink?.second ?: PadNumber.PAD_2,
            onPadLink2Selected = { pad2 ->
                viewModel.updatePadLink(
                    kit.padLink?.first ?: PadNumber.PAD_1,
                    pad2
                )
            }
        )

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }) {
                Text("FX1", modifier = Modifier.padding(8.dp))
            }
            Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }) {
                Text("FX2", modifier = Modifier.padding(8.dp))
            }
        }

        when (selectedTabIndex) {
            0 -> KitFXView(
                kitFX = kit.fx1 ?: KitFX(SyncSwitch.OFF, FxEffect.fromValues(0, listOf(0))),
                onFxChange = viewModel::updateFx1
            )
            1 -> KitFXView(
                kitFX = kit.fx2 ?: KitFX(SyncSwitch.OFF, FxEffect.fromValues(0, listOf(0))),
                onFxChange = viewModel::updateFx2
            )
        }
    }
}

@Composable
fun DetailsTabs(
    kitIndex: Int?,
    kit: Kit?,
    pad: Pad?,
    padNumber: PadNumber?,
    deviceManager: DeviceManager,
    onWaveSelected: (Int?) -> Unit  // Add this parameter
) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("Kit", modifier = Modifier.padding(8.dp))
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("Pad", modifier = Modifier.padding(8.dp))
            }
        }

        when (selectedTab) {
            0 -> kitIndex?.let {
                KitScreen(
                    kitIndex = it,
                    deviceManager = deviceManager
                )
            } ?: Text("No kit selected", modifier = Modifier.padding(16.dp))

            1 -> {
                if (kitIndex != null && padNumber != null && pad != null) {
                    PadDetailsScreen(
                        padNumber = padNumber,
                        kitIndex = kitIndex,
                        deviceManager = deviceManager,
                        onWaveSelected = onWaveSelected  // Pass the callback
                    )
                } else {
                    Text("No pad selected", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}