package org.xebia.spdmanager.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.ui.screens.openFolderDialog

@Composable
fun SelectFolderButton(onFolderSelected: (String) -> Unit) {
    Button(
        content = { Text("Select Folder", fontSize = 12.sp) },
        onClick = {
            openFolderDialog { folder ->
                onFolderSelected(folder)
            }
        }
    )
}
