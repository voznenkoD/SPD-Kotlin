package org.xebia.spdmanager.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListItemView(item: String) {
    // Wrap the row content inside a Box to manage border only at the bottom
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp) // Padding to give space for the bottom border
            .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(4.dp)) // Apply border only at the bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically // Align items vertically centered
        ) {
            // Dot (circle) before the item
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color.Magenta, shape = CircleShape)
                    .padding(4.dp) // Optional: To add some spacing around the dot
            )

            Spacer(modifier = Modifier.width(8.dp)) // Spacer between dot and text

            // Item Text
            Text(text = item, fontSize = 18.sp)
        }
    }
}