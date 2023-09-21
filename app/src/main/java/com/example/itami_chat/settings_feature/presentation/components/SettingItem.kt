package com.example.itami_chat.settings_feature.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.itami_chat.core.presentation.ui.theme.spacing

@Composable
fun SettingItem(
    title: String,
    description: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    titleStyle: TextStyle = MaterialTheme.typography.titleSmall,
    descriptionColor: Color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
    descriptionStyle: TextStyle = MaterialTheme.typography.bodySmall,
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = title,
            tint = titleColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = titleStyle,
                color = titleColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Text(
                text = description,
                style = descriptionStyle,
                color = descriptionColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }

}