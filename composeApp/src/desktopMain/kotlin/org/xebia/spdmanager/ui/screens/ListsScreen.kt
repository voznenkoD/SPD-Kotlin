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
import org.xebia.spdmanager.model.list.ListedWave
import org.xebia.spdmanager.model.list.WaveListsHolder
import org.xebia.spdmanager.ui.tabs.KitListTab
import org.xebia.spdmanager.ui.tabs.WaveListTab
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView

@Composable
fun ListsScreen(
    kits: List<Kit>,
    waveListsHolder: WaveListsHolder,
    onKitSelected: (Kit) -> Unit,
    onWaveSelected: (ListedWave) -> Unit,
    onOpenClicked: (String, Device) -> Unit
) {
    Column {
        Row(Modifier.weight(1f)) {
            ListHeaderTabs(kits, waveListsHolder, onKitSelected, onWaveSelected)
        }
        Row(Modifier.height(100.dp)) {
            SelectFolderSection(onOpenClicked)
        }
    }
}

@Composable
fun ListHeaderTabs(
    kits: List<Kit>,
    waveListsHolder: WaveListsHolder,
    onKitSelected: (Kit) -> Unit,
    onWaveSelected: (ListedWave) -> Unit
) {
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
            0 -> KitListTab(kits, onKitSelected) { kit -> Text(text = kit.name, fontSize = 18.sp) }
            1 -> WaveListTab(waveListsHolder, onWaveSelected)
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
