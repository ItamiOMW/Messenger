package com.example.itami_chat.chat_feature.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.Message
import com.example.itami_chat.chat_feature.domain.model.MessageType
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.utils.DateTimeUtil


@Composable
fun MyMessageComponent(
    message: Message,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    onImageClicked: (imageUrl: String) -> Unit = { },
    onEditMessage: () -> Unit,
    onDeleteMessage: () -> Unit,
) {

    val time = remember(message) {
        DateTimeUtil.formatUtcDateTimeForMessage(message.createdAt)
    }

    val showMoreMessageOptionsMenu = rememberSaveable {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier
            .clickable {
                showMoreMessageOptionsMenu.value = true
            },
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
        )
    ) {
        Box {
            Column(
                modifier = Modifier.padding(
                    start = MaterialTheme.padding.small,
                    end = MaterialTheme.padding.small,
                    bottom = MaterialTheme.padding.extraSmall,
                    top = MaterialTheme.padding.extraSmall
                )
            ) {
                if (message.text != null) {
                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor
                    )
                }
                if (!message.pictureUrls.isNullOrEmpty()) {
                    ImagesComponent(
                        imageUrls = message.pictureUrls,
                        imageLoader = imageLoader,
                        onImageClicked = onImageClicked
                    )
                }
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (message.updatedAt != null) {
                        Text(
                            text = stringResource(R.string.text_edited),
                            style = MaterialTheme.typography.bodySmall,
                            color = contentColor.copy(0.6f),
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = time,
                        style = MaterialTheme.typography.bodySmall,
                        color = contentColor.copy(0.6f)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        painter = painterResource(
                            id = if (message.isRead) R.drawable.ic_done_all
                            else R.drawable.ic_done
                        ),
                        contentDescription = stringResource(id = R.string.desc_is_message_read),
                        modifier = Modifier.size(16.dp),
                        tint = contentColor.copy(0.6f)
                    )
                }
            }
            DropdownMenu(
                expanded = showMoreMessageOptionsMenu.value,
                onDismissRequest = { showMoreMessageOptionsMenu.value = false },
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.text_edit_message),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = stringResource(R.string.desc_edit_message),
                            modifier = Modifier.size(24.dp),
                        )
                    },
                    onClick = {
                        showMoreMessageOptionsMenu.value = false
                        onEditMessage()
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.text_delete_message),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = stringResource(R.string.desc_delete_message),
                            modifier = Modifier.size(24.dp),
                        )
                    },
                    onClick = {
                        showMoreMessageOptionsMenu.value = false
                        onDeleteMessage()
                    }
                )
            }
        }
    }
}

@Composable
@Preview
fun MyMessageComponentPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = false) {
        DialogMessageComponent(
            message = Message(
                0,
                0,
                authorId = 0,
                authorFullName = "fullname",
                authorProfilePictureUrl = null,
                MessageType.MESSAGE,
                text = "Just some random text",
                pictureUrls = null,
                isRead = false,
                usersSeenMessage = emptyList(),
                createdAt = 0L,
                updatedAt = null
            ),
            imageLoader = ImageLoader(LocalContext.current)
        )
    }
}