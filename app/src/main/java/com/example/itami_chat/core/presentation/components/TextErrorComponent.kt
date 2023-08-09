package com.example.itami_chat.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun TextErrorComponent(
    modifier: Modifier = Modifier,
    text: () -> String?,
    textColor: Color = MaterialTheme.colorScheme.error,
) {

    AnimatedVisibility(
        visible = text() != null,
        modifier = modifier,
    ) {
        text()?.let { error ->
            Text(
                text = error,
                color = textColor,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }

}