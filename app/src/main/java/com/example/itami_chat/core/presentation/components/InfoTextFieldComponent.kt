package com.example.itami_chat.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.itami_chat.core.presentation.ui.theme.spacing


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldInfoComponent(
    value: () -> String,
    onValueChange: (String) -> Unit,
    error: () -> String?,
    label: String,
    hint: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    hintColor: Color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
    errorColor: Color = MaterialTheme.colorScheme.error,
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = label,
                tint = textColor,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicTextField(
                    value = value(),
                    onValueChange = onValueChange,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    interactionSource = interactionSource,
                ) {
                    TextFieldDefaults.DecorationBox(
                        value = value(),
                        innerTextField = it,
                        enabled = true,
                        singleLine = false,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        contentPadding = PaddingValues(0.dp),
                        label = {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium,
                                color = hintColor
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            cursorColor = textColor,
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor,
                            focusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedLabelColor = Color.Transparent,
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodySmall,
                    color = hintColor,
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

}