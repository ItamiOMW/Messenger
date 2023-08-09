package com.example.itami_chat.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme

@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
    leadingIcon: Painter? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    contentPadding: PaddingValues = PaddingValues(vertical = 14.dp),
    enabled: () -> Boolean = { true },
    shape: Shape = MaterialTheme.shapes.small,
    onButtonClick: () -> Unit,
) {

    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = shape,
        enabled = enabled(),
        contentPadding = contentPadding,
        onClick = {
            onButtonClick()
        },
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    painter = leadingIcon,
                    contentDescription = text,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(9.dp))
            }
            Text(
                text = text,
                style = textStyle
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun ButtonComponentPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = true) {
        ButtonComponent(
            text = "Continue",
            enabled = { true },
            leadingIcon = null,
            onButtonClick = {}
        )
    }
}