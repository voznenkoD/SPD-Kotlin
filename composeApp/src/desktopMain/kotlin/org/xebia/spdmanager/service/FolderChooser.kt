package org.xebia.spdmanager.service

import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView

fun openFolderDialog(onFolderSelected: (String) -> Unit) {
    val fileChooser = JFileChooser(FileSystemView.getFileSystemView()).apply {
        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        isAcceptAllFileFilterUsed = false
    }

    val result = fileChooser.showOpenDialog(null)
    if (result == JFileChooser.APPROVE_OPTION) {
        // Pass folder path back to the composable context
        onFolderSelected(fileChooser.selectedFile.absolutePath)
    }
}
