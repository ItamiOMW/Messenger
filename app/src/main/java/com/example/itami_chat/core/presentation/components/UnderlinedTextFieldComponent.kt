package com.example.itami_chat.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.example.itami_chat.core.presentation.ui.theme.spacing


@Composable
fun UnderlinedTextFieldComponent(
    modifier: Modifier = Modifier,
    textValue: () -> String,
    onValueChange: (String) -> Unit,
    error: () -> String?,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Ascii,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    errorColor: Color = MaterialTheme.colorScheme.error,
    enabled: () -> Boolean = { true },
    trailingContent: (@Composable () -> Unit)? = null,
) {

    val textFieldInteractionSource = remember { MutableInteractionSource() }

    val isFocused = textFieldInteractionSource.collectIsFocusedAsState()

    Column(
        modifier = modifier
    ) {
        AnimatedVisibility(visible = error() != null) {
            error()?.let { error ->
                Text(
                    text = error,
                    color = errorColor,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.padding(start = MaterialTheme.spacing.extraSmall)
                )
            }
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textValue(),
            onValueChange = onValueChange,
            enabled = enabled(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            singleLine = true,
            interactionSource = textFieldInteractionSource,
            colors = TextFieldDefaults.colors(
                cursorColor = textColor,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                disabledIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                disabledContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(0.6f),
            ),
            label = {
                Text(
                    text = label,
                    style = if (isFocused.value || textValue().isNotBlank()) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyLarge
                )
            },
            trailingIcon = {
                trailingContent?.invoke()
            },
        )
    }
}

@Preview
@Composable
fun UnderlinedTextFieldPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = false) {
        UnderlinedTextFieldComponent(
            textValue = { "" },
            onValueChange = {},
            error = { null },
            label = "Email",
            trailingContent = {
                Text(
                    text = "60"
                )
            }
        )
    }
}