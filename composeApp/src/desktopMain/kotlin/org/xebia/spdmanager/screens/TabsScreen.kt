package org.xebia.spdmanager.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.model.Kit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.service.XmlParser
import org.xebia.spdmanager.tabs.KitTab
import org.xebia.spdmanager.tabs.WaveTab
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView

@Composable
fun TabsScreen(kits: List<Kit>, onItemSelected: (Kit) -> Unit, onOpenClicked: (String) -> Unit) {
    Column {
        Row(Modifier.weight(1f)) {
            HeaderTabsSection(kits, onItemSelected)
        }
        Row(Modifier.height(200.dp)) {
            BottomTabsSection(onOpenClicked)
        }
    }
}

@Composable
fun HeaderTabsSection(kits: List<Kit>, onItemSelected: (Kit) -> Unit) {
    var selectedTab by remember { mutableStateOf(0) } // Keep track of selected tab

    val tabs = listOf("Kit", "Wave")

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
fun BottomTabsSection(onOpenClicked: (String) -> Unit) {
    var folderPath by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Selected Folder:", fontSize = 18.sp, modifier = Modifier.weight(0.3f))
            TextField(
                value = folderPath,
                onValueChange = { folderPath = it },
                modifier = Modifier.weight(0.7f)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Button(
                onClick = {
                    openFolderDialog { folder -> folderPath = folder }
                    val parser = XmlParser()
                    parser.parseAllFiles(folderPath)
                    onOpenClicked(folderPath)
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Select Folder", fontSize = 16.sp)
            }
        }
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
