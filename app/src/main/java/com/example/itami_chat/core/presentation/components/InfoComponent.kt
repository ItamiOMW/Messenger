package com.example.itami_chat.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.example.itami_chat.core.presentation.ui.theme.spacing


@Composable
fun InfoComponent(
    text: String,
    label: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    hintColor: Color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = label,
            tint = textColor,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Top)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = hintColor
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                maxLines = 2,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}