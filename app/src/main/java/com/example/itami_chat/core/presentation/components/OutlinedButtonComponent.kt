package com.example.itami_chat.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
fun OutlinedButtonComponent(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
    leadingIcon: Painter? = null,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    contentPadding: PaddingValues = PaddingValues(vertical = 14.dp),
    enabled: () -> Boolean,
    shape: Shape = MaterialTheme.shapes.small,
    onButtonClick: () -> Unit,
) {

    OutlinedButton(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            contentColor = contentColor,
            containerColor = Color.Transparent,
        ),
        shape = shape,
        enabled = enabled(),
        contentPadding = contentPadding,
        border = BorderStroke(1.5.dp, contentColor),
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
fun OutlinedButtonComponentPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = true) {
        OutlinedButtonComponent(
            text = "Continue",
            enabled = { true },
            leadingIcon = null,
            onButtonClick = {}
        )
    }
}