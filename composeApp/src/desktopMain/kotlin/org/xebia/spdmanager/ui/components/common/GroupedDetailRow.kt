package org.xebia.spdmanager.ui.components.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GroupedDetailRow(label1: String, value1: String, label2: String, value2: String, label3: String, value3: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label1, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Text(text = value1, fontSize = 16.sp, modifier = Modifier.weight(2f))

        Text(text = label2, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Text(text = value2, fontSize = 16.sp, modifier = Modifier.weight(2f))

        if (label3.isNotEmpty()) {
            Text(text = label3, fontSize = 16.sp, modifier = Modifier.weight(1f))
            Text(text = value3, fontSize = 16.sp, modifier = Modifier.weight(2f))
        }
    }
}