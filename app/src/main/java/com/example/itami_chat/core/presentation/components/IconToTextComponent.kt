package com.example.itami_chat.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun IconToTextComponent(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter,
    color: Color,
    textStyle: TextStyle,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            modifier = Modifier.padding(start = 16.dp),
            painter = icon,
            contentDescription = text,
            tint = color
        )
        Text(
            text = text,
            style = textStyle,
            color = color,
        )
    }
}