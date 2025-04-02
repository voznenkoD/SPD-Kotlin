package org.xebia.spdmanager.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch

@Composable
fun SwitchWithLabel(label: String, syncSwitch: SyncSwitch, onValueChange: (Boolean) -> Unit) {
    Column(modifier = Modifier.width(150.dp)) {
        Text("$label: $syncSwitch", fontSize = 12.sp)
        Switch(
            checked = syncSwitch == SyncSwitch.ON,
            onCheckedChange = onValueChange,
            modifier = Modifier.padding(all = 4.dp)
        )
    }
}