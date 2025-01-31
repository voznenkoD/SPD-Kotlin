package org.xebia.spdmanager.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.components.DropdownMenuExample

@Composable
fun KitScreen() {
    var textFieldValue1 by remember { mutableStateOf(TextFieldValue("")) }
    var textFieldValue2 by remember { mutableStateOf(TextFieldValue("")) }
    var sliderValue1 by remember { mutableStateOf(0f) }
    var sliderValue2 by remember { mutableStateOf(0f) }
    var switchState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Static Text 1: ", fontSize = 14.sp, modifier = Modifier.weight(1f))
            TextField(
                value = textFieldValue1,
                onValueChange = { textFieldValue1 = it },
                modifier = Modifier.weight(2f)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Static Text 2: ", fontSize = 14.sp, modifier = Modifier.weight(1f))
            TextField(
                value = textFieldValue2,
                onValueChange = { textFieldValue2 = it },
                modifier = Modifier.weight(2f)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Static Text 3: ", fontSize = 14.sp, modifier = Modifier.weight(1f))
            Slider(
                value = sliderValue1,
                onValueChange = { sliderValue1 = it },
                valueRange = 0f..100f,
                modifier = Modifier.weight(2f)
            )
            Text("${sliderValue1.toInt()}", fontSize = 18.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Static Text 4: ", fontSize = 14.sp, modifier = Modifier.weight(1f))
            Slider(
                value = sliderValue2,
                onValueChange = { sliderValue2 = it },
                valueRange = 0f..100f,
                modifier = Modifier.weight(2f)
            )
            TextField(
                value = sliderValue2.toInt().toString(),
                onValueChange = { newValue ->
                    sliderValue2 = newValue.toFloatOrNull() ?: 0f
                },
                modifier = Modifier.width(60.dp),
            )
        }

        Row(verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            DropdownMenuExample()
        }

        Row(verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            DropdownMenuExample()
        }

        Spacer(modifier = Modifier.fillMaxSize())
    }
}