package org.xebia.spdmanager.ui.components.kit

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.kit.fx.KitFX
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch

@Composable
fun KitFXView(kitFX: KitFX) {
    if (kitFX.sw == SyncSwitch.OFF) {
        Text("FX Disabled", fontSize = 16.sp, modifier = Modifier.padding(16.dp))
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("FX Type: ${kitFX.fx.fxType}", fontSize = 16.sp)
            kitFX.fx.renderParameters()
        }
    }
}