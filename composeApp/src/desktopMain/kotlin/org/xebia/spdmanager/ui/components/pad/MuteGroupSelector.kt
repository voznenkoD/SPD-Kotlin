package org.xebia.spdmanager.ui.components.pad

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.kit.pad.MuteGroup

@Composable
fun MuteGroupSelector(selectedGroup: MuteGroup, onGroupSelected: (MuteGroup) -> Unit) {
    val options = listOf<MuteGroup>(MuteGroup.Off) + (0..9).map { MuteGroup.Group(it) }
    val selectedIndex = options.indexOfFirst { it == selectedGroup }

    Text("Mute group", fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        options.forEachIndexed { index, option ->
            Button(
                onClick = { onGroupSelected(option) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (index == selectedIndex) Color.Blue else Color.Gray
                ),
                modifier = Modifier.padding(4.dp)
            ) {
                Text(if (option is MuteGroup.Off) "Off" else option.toString())
            }
        }
    }
}
