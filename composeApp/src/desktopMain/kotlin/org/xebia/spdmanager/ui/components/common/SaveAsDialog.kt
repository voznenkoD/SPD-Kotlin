package org.xebia.spdmanager.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.xebia.spdmanager.service.DeviceManager
import org.xebia.spdmanager.ui.theme.*
import java.io.File

/**
 * Confirmation + progress dialog for "Save As". Shows the editable target name, the resulting path,
 * a progress bar during the copy, and any error. On success it dismisses; the device is rebased to
 * the new location by [DeviceManager.saveDeviceAs].
 */
@Composable
fun SaveAsDialog(
    parentPath: String,
    defaultName: String,
    deviceManager: DeviceManager,
    onDismiss: () -> Unit,
    onCopyingChange: (Boolean) -> Unit = {}
) {
    var name by remember { mutableStateOf(defaultName) }
    var copying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val trimmedName = name.trim()
    // Reject path separators and the dot folders so the copy can't escape the chosen parent.
    val nameHasSeparators = trimmedName.contains('/') || trimmedName.contains('\\')
    val nameInvalid = trimmedName.isEmpty() || nameHasSeparators || trimmedName == "." || trimmedName == ".."
    val targetPath = File(parentPath, trimmedName).path

    AlertDialog(
        onDismissRequest = { if (!copying) onDismiss() },
        containerColor = ColorSurface,
        titleContentColor = ColorTextPrimary,
        textContentColor = ColorTextPrimary,
        title = { Text("Save Device As") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    enabled = !copying,
                    label = { Text("Device folder name") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = ColorSurface,
                        focusedContainerColor = ColorBackground,
                        focusedIndicatorColor = ColorAccentOrange,
                        unfocusedIndicatorColor = ColorDivider,
                        cursorColor = ColorAccentOrange
                    )
                )
                Spacer(Modifier.height(Spacing.l))
                Text(text = "Save to:", style = Typography.caption, color = ColorTextSecondary)
                Text(text = targetPath, style = Typography.body, color = ColorTextPrimary)

                if (nameHasSeparators) {
                    Spacer(Modifier.height(Spacing.m))
                    Text(
                        text = "Name cannot contain / or \\",
                        style = Typography.caption,
                        color = ColorAccentOrange
                    )
                }

                if (copying) {
                    Spacer(Modifier.height(Spacing.xl))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth(),
                        color = ColorAccentOrange,
                        trackColor = ColorDivider
                    )
                    Spacer(Modifier.height(Spacing.m))
                    Text(
                        text = "Copying… ${(progress * 100).toInt()}%",
                        style = Typography.caption,
                        color = ColorTextSecondary
                    )
                }

                error?.let {
                    Spacer(Modifier.height(Spacing.xl))
                    Text(text = it, style = Typography.caption, color = ColorAccentOrange)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    copying = true
                    onCopyingChange(true)
                    error = null
                    progress = 0f
                    scope.launch {
                        val result = deviceManager.saveDeviceAs(targetPath) { p -> progress = p }
                        copying = false
                        onCopyingChange(false)
                        when (result) {
                            is DeviceManager.SaveAsResult.Success -> onDismiss()
                            is DeviceManager.SaveAsResult.Error -> error = result.message
                        }
                    }
                },
                enabled = !copying && !nameInvalid
            ) { Text("Save", color = ColorAccentOrange) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !copying) {
                Text("Cancel", color = ColorTextSecondary)
            }
        }
    )
}
