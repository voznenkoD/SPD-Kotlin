package org.xebia.spdmanager.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch

@Composable
fun SwitchWithLabel(label: String, syncSwitch: SyncSwitch, onValueChange: (Boolean) -> Unit) {
    ToggleSwitchWithLabel(
        label = label,
        selectedItem = syncSwitch,
        offItem = SyncSwitch.OFF,
        onItem = SyncSwitch.ON,
        onItemSelected = { item -> onValueChange(item == SyncSwitch.ON) },
        colored = true
    )
}

@Composable
fun <T : Enum<T>> ToggleSwitchWithLabel(
    label: String,
    selectedItem: T,
    offItem: T,
    onItem: T,
    onItemSelected: (T) -> Unit,
    offLabel: String = offItem.name,
    onLabel: String = onItem.name,
    colored: Boolean = false
) {
    val isOn = selectedItem == onItem
    val defaults = SwitchDefaults.colors()
    val switchColors = if (colored) defaults else {
        SwitchDefaults.colors(
            checkedTrackColor = defaults.uncheckedTrackColor,
            checkedThumbColor = defaults.uncheckedThumbColor,
            checkedBorderColor = defaults.uncheckedBorderColor
        )
    }
    Column(modifier = Modifier.width(180.dp).padding(vertical = 4.dp)) {
        Text(
            text = "$label: ${selectedItem.name}",
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = offLabel,
                fontSize = 12.sp,
                fontWeight = if (!isOn) FontWeight.Bold else FontWeight.Normal,
                color = if (!isOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Switch(
                checked = isOn,
                onCheckedChange = { checked ->
                    onItemSelected(if (checked) onItem else offItem)
                },
                colors = switchColors,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = onLabel,
                fontSize = 12.sp,
                fontWeight = if (isOn) FontWeight.Bold else FontWeight.Normal,
                color = if (isOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}