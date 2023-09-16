package com.example.itami_chat.chat_feature.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.Message
import com.example.itami_chat.chat_feature.domain.model.MessageType
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import com.example.itami_chat.core.utils.DateTimeUtil


@Composable
fun GroupMessageComponent(
    message: Message,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onImageClicked: (imageUrl: String) -> Unit = { },
    onUserProfilePictureClicked: () -> Unit,
) {

    val time = remember(message) {
        DateTimeUtil.formatUtcDateTimeForMessage(message.createdAt)
    }

    Row(
        modifier = modifier,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(35.dp)
                .clip(RoundedCornerShape(30.dp))
                .align(Alignment.Bottom)
                .clickable {
                    onUserProfilePictureClicked()
                },
            model = message.authorProfilePictureUrl,
            contentDescription = stringResource(id = R.string.desc_profile_picture),
            imageLoader = imageLoader,
            error = painterResource(id = R.drawable.sniper_mask)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
        Card(
            modifier = Modifier,
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = containerColor,
                contentColor = contentColor
            )
        ) {
            Column(
                modifier = Modifier.padding(
                    start = MaterialTheme.padding.small,
                    end = MaterialTheme.padding.small,
                    bottom = MaterialTheme.padding.extraSmall,
                    top = MaterialTheme.padding.extraSmall
                )
            ) {
                Text(
                    text = message.authorFullName,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary,
                    overflow = TextOverflow.Ellipsis
                )
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
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (message.updatedAt != null) {
                        Text(
                            text = stringResource(R.string.text_edited),
                            style = MaterialTheme.typography.bodySmall,
                            color = contentColor.copy(0.6f),
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = time,
                        style = MaterialTheme.typography.bodySmall,
                        color = contentColor.copy(0.6f),
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun GroupMessageComponentPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = false) {
        DialogMessageComponent(
            message = Message(
                0,
                0,
                authorId = 0,
                authorFullName = "Random dude",
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