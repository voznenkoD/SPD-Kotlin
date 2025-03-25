package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.model.kit.Kit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.LocalDeviceManager
import org.xebia.spdmanager.model.Device
import org.xebia.spdmanager.ui.tabs.KitTab
import org.xebia.spdmanager.ui.tabs.WaveTab
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView

@Composable
fun ListsScreen(kits: List<Kit>, onItemSelected: (Kit) -> Unit, onOpenClicked: (String, Device) -> Unit) {
    Column {
        Row(Modifier.weight(1f)) {
            ListHeaderTabs(kits, onItemSelected)
        }
        Row(Modifier.height(100.dp)) {
            SelectFolderSection(onOpenClicked)
        }
    }
}

@Composable
fun ListHeaderTabs(kits: List<Kit>, onItemSelected: (Kit) -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Kits", "Waves")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTab
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> KitTab(kits, onItemSelected)
            1 -> WaveTab()
        }
    }
}

@Composable
fun SelectFolderSection(onOpenClicked: (String, Device) -> Unit) {
    var folderPath by remember { mutableStateOf("") }
    val deviceManager = LocalDeviceManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Selected Folder: $folderPath", fontSize = 12.sp)
        Button(
            content = { Text("Select Folder", fontSize = 12.sp)} ,
            onClick = {
                openFolderDialog { folder -> folderPath = folder }
                onOpenClicked(folderPath, deviceManager.readDevice(folderPath))
            }
        )
    }
}

fun openFolderDialog(onFolderSelected: (String) -> Unit) {
    val fileChooser = JFileChooser(FileSystemView.getFileSystemView()).apply {
        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY // Restrict selection to folders
        isAcceptAllFileFilterUsed = false
    }

    val result = fileChooser.showOpenDialog(null) // Open the dialog
    if (result == JFileChooser.APPROVE_OPTION) {
        val selectedFolder = fileChooser.selectedFile.absolutePath
        onFolderSelected(selectedFolder)
    }
}
