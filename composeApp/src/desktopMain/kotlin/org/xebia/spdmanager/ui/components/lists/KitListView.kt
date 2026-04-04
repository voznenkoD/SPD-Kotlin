package org.xebia.spdmanager.ui.components.lists

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.kit.Kit

@Composable
fun KitListView(
    kits: List<Kit>,
    onKitSelected: (Kit) -> Unit,
    onCopyKit: (Kit) -> Unit,
    onPasteKit: (Kit) -> Unit,
    hasCopiedKit: Boolean
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(kits) { kit ->
            ContextMenuArea(
                items = {
                    buildList {
                        add(ContextMenuItem("Copy") { onCopyKit(kit) })
                        if (hasCopiedKit) {
                            add(ContextMenuItem("Paste") { onPasteKit(kit) })
                        }
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                        .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(4.dp))
                        .clickable { onKitSelected(kit) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = kit.name, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}
