package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.fx.KitFX
import org.xebia.spdmanager.model.kit.pad.PadNumber
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.ui.components.KitFXView
import org.xebia.spdmanager.ui.components.PadLinkSelector

@Composable
fun KitScreen(kit: Kit?) {
    var name by remember { mutableStateOf(kit?.name ?: "") }
    var subName by remember { mutableStateOf(kit?.subName ?: "") }
    var tempo by remember { mutableStateOf(kit?.tempo ?: 120.0) }
    var volume by remember { mutableStateOf(kit?.volume ?: 50) }
    var padLink1 by remember { mutableStateOf(kit?.padLink?.first ?: PadNumber.PAD_1) }
    var padLink2 by remember { mutableStateOf(kit?.padLink?.second ?: PadNumber.PAD_2) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    var fx1 by remember { mutableStateOf(kit?.fx1 ?: KitFX(SyncSwitch.OFF, FxEffect.fromValues(0, listOf(0)))) }
    var fx2 by remember { mutableStateOf(kit?.fx2 ?: KitFX(SyncSwitch.OFF, FxEffect.fromValues(0, listOf(0))))  }


    LaunchedEffect(kit) {
        name = kit?.name ?: ""
        subName = kit?.subName ?: ""
        tempo = kit?.tempo ?: 120.0
        volume = kit?.volume ?: 50
        padLink1 = kit?.padLink?.first ?: PadNumber.PAD_1
        padLink2 = kit?.padLink?.second ?: PadNumber.PAD_2
        fx1 = kit?.fx1 ?: KitFX(SyncSwitch.OFF, FxEffect.fromValues(0, listOf(0)))
        fx2 = kit?.fx2 ?: KitFX(SyncSwitch.OFF, FxEffect.fromValues(0, listOf(0)))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Name Input Field
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Name: ", fontSize = 14.sp, modifier = Modifier.weight(1f))
            TextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.weight(2f)
            )
        }

        // SubName Input Field
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Sub Name: ", fontSize = 14.sp, modifier = Modifier.weight(1f))
            TextField(
                value = subName,
                onValueChange = { subName = it },
                modifier = Modifier.weight(2f)
            )
        }

        // Tempo Slider (20.0 - 260.0)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Tempo: ", fontSize = 14.sp, modifier = Modifier.weight(1f))
            Slider(
                value = tempo.toFloat(),
                onValueChange = { tempo = it.toDouble() },
                valueRange = 20f..260f,
                modifier = Modifier.weight(2f)
            )
            Text("${tempo.toInt()}", fontSize = 18.sp)
        }

        // Volume Slider (0 - 100)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Volume: ", fontSize = 14.sp, modifier = Modifier.weight(1f))
            Slider(
                value = volume.toFloat(),
                onValueChange = { volume = it.toInt() },
                valueRange = 0f..100f,
                modifier = Modifier.weight(2f)
            )
            Text("${volume.toInt()}", fontSize = 18.sp)
        }

        PadLinkSelector(
            padLink1 = padLink1,
            onPadLink1Selected = { padLink1 = it },
            padLink2 = padLink2,
            onPadLink2Selected = { padLink2 = it }
        )

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }) {
                Text("FX1", modifier = Modifier.padding(8.dp))
            }
            Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }) {
                Text("FX2", modifier = Modifier.padding(8.dp))
            }
        }

        when (selectedTabIndex) {
            0 -> KitFXView(fx1)
            1 -> KitFXView(fx2)
        }

        Spacer(modifier = Modifier.fillMaxSize())
    }
}
