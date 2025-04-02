package org.xebia.spdmanager.ui.components.pad

import androidx.compose.runtime.*
import org.xebia.spdmanager.model.kit.pad.MuteGroup
import org.xebia.spdmanager.ui.components.common.DropdownSelector

@Composable
fun MuteGroupSelector(
    selectedMuteGroup: MuteGroup,
    onMuteGroupSelected: (MuteGroup) -> Unit
) {
    val muteGroups = listOf(MuteGroup.Off) + (0..9).map { MuteGroup.Group(it) }
    DropdownSelector(
        label = "Mute Group",
        selectedItem = selectedMuteGroup,
        onItemSelected = onMuteGroupSelected,
        items = muteGroups
    )
}
