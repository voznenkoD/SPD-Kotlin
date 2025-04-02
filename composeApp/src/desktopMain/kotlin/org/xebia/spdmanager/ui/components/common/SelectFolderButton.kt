package org.xebia.spdmanager.ui.components.common

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.LocalDeviceManager
import org.xebia.spdmanager.service.openFolderDialog

@Composable
fun SelectFolderButton() {
    val deviceManager = LocalDeviceManager.current

    Button(
        onClick = {
            openFolderDialog { folderPath ->
                deviceManager.readDevice(folderPath)
            }
        },
        content = {
            Text("Select Folder", fontSize = 12.sp)
        }
    )
}
