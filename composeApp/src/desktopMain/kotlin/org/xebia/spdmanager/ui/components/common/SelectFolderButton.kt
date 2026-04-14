package org.xebia.spdmanager.ui.components.common

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.LocalDeviceManager
import org.xebia.spdmanager.service.openFolderDialog
import org.xebia.spdmanager.ui.theme.*

@Composable
fun SelectFolderButton() {
    val deviceManager = LocalDeviceManager.current

    Button(
        onClick = {
            openFolderDialog { folderPath ->
                deviceManager.readDevice(folderPath)
            }
        },
        shape = ShapeDefault,
        colors = ButtonDefaults.buttonColors(
            containerColor = ColorAccentOrange,
            contentColor = ColorTextOnAccent
        ),
        modifier = Modifier.height(Heights.button)
    ) {
        Text("Select Folder", style = Typography.label)
    }
}
