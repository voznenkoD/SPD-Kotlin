package org.xebia.spdmanager.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*

/**
 * Alert shown when loading a device aborts because a required SYSTEM file could not be parsed.
 * Names the offending file so the user knows which file to fix.
 */
@Composable
fun LoadErrorDialog(
    fileName: String,
    reason: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ColorSurface,
        titleContentColor = ColorTextPrimary,
        textContentColor = ColorTextPrimary,
        title = { Text("Failed to load device") },
        text = {
            Column {
                Text(
                    text = "Could not parse the system file \"$fileName\".",
                    style = Typography.body,
                    color = ColorTextPrimary
                )
                Spacer(Modifier.height(Spacing.m))
                Text(text = reason, style = Typography.caption, color = ColorTextSecondary)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK", color = ColorAccentOrange)
            }
        }
    )
}