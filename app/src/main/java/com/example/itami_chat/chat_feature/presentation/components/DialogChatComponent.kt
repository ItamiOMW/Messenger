package com.example.itami_chat.chat_feature.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant
import com.example.itami_chat.chat_feature.domain.model.ChatType
import com.example.itami_chat.chat_feature.domain.model.Message
import com.example.itami_chat.chat_feature.domain.model.MessageType
import com.example.itami_chat.chat_feature.domain.model.ParticipantRole
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import com.example.itami_chat.core.utils.DateTimeUtil


@Composable
fun DialogChatComponent(
    chat: Chat,
    myUserId: Int,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current

    val formattedTime = remember(chat.lastMessage) {
        chat.lastMessage?.createdAt?.let { DateTimeUtil.formatUtcDateTimeForMessage(it) }
    }

    val dialogUser = remember(chat) {
        chat.participants.first { it.user.id != myUserId }
    }

    val lastMessageText = remember(chat.lastMessage) {
        when (chat.lastMessage?.type) {
            MessageType.MESSAGE -> {
                if (!chat.lastMessage.pictureUrls.isNullOrEmpty()) {
                    context.getString(
                        R.string.text_pictures,
                        chat.lastMessage.pictureUrls.count()
                    )
                } else {
                    chat.lastMessage.text
                }
            }

            else -> {
                chat.lastMessage?.text
            }
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = dialogUser.user.profilePictureUrl,
            contentDescription = stringResource(id = R.string.desc_profile_picture),
            imageLoader = imageLoader,
            error = painterResource(id = R.drawable.sniper_mask),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(start = MaterialTheme.padding.small)
                .size(60.dp)
                .clip(RoundedCornerShape(30.dp))
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dialogUser.user.fullName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f, false)
                )
                if (lastMessageText != null && chat.lastMessage != null) {
                    Row(
                        modifier = Modifier.padding(end = MaterialTheme.padding.small)
                    ) {
                        if (chat.lastMessage.type == MessageType.MESSAGE && chat.lastMessage.authorId == myUserId) {
                            Icon(
                                painter = painterResource(
                                    id = if (chat.lastMessage.isRead) R.drawable.ic_done_all
                                    else R.drawable.ic_done
                                ),
                                contentDescription = stringResource(R.string.desc_is_message_read),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(20.dp)
                                    .weight(0.1f, false)
                            )
                        }
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                        Text(
                            text = formattedTime ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                            maxLines = 1,
                            modifier = Modifier
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = MaterialTheme.padding.medium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (chat.lastMessage != null) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                                if (myUserId == chat.lastMessage.authorId) {
                                    append(
                                        stringResource(R.string.text_you_sent) + " ",
                                    )
                                }
                            }
                            append(lastMessageText)
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, false)
                    )
                    if (chat.unreadMessagesCount > 0) {
                        Box(
                            modifier = Modifier
                                .weight(0.2f, false)
                                .clip(RoundedCornerShape(size = 30.dp))
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = chat.unreadMessagesCount.toString(),
                                color = MaterialTheme.colorScheme.onPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(
                                    start = MaterialTheme.padding.extraSmall,
                                    end = MaterialTheme.padding.extraSmall,
                                )
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Preview
@Composable
fun DialogChatComponentPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = false) {
        DialogChatComponent(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = 8.dp, top = 10.dp),
            chat = Chat(
                1,
                "Family chat",
                ChatType.DIALOG,
                null,
                lastMessage = Message(
                    1,
                    1,
                    authorId = 1,
                    authorFullName = "fullname",
                    authorProfilePictureUrl = null,
                    MessageType.MESSAGE,
                    "Random text...",
                    null,
                    false,
                    createdAt = 0L,
                    usersSeenMessage = emptyList(),
                    updatedAt = null
                ),
                listOf(
                    ChatParticipant(
                        SimpleUser(
                            1,
                            "Itami",
                            null,
                            null,
                            false,
                            0L,
                        ),
                        ParticipantRole.CREATOR
                    ),
                    ChatParticipant(
                        SimpleUser(
                            2,
                            "Chad",
                            null,
                            null,
                            false,
                            0L,
                        ),
                        ParticipantRole.CREATOR
                    )
                ),
                1
            ),
            myUserId = 1,
            imageLoader = ImageLoader(LocalContext.current)
        )
    }
}