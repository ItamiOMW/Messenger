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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.itami_chat.chat_feature.presentation.util.getText
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import com.example.itami_chat.core.utils.DateTimeUtil


@Composable
fun GroupChatComponent(
    chat: Chat,
    myUserId: Int,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current

    val formattedTime = remember(chat.lastMessage) {
        chat.lastMessage?.createdAt?.let { DateTimeUtil.formatUtcDateTimeForMessage(it) }
    }

    val chatName = remember(chat) {
        chat.name
    }

    val lastMessageText = remember(chat.lastMessage) {
        chat.lastMessage?.getText(context, myUserId)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (chat.chatPictureUrl != null) {
            AsyncImage(
                model = chat.chatPictureUrl,
                contentDescription = stringResource(R.string.desc_chat_picture),
                imageLoader = imageLoader,
                modifier = Modifier
                    .padding(start = MaterialTheme.padding.small)
                    .size(60.dp)
                    .clip(RoundedCornerShape(30.dp)),
            )
        } else {
            Box(
                modifier = Modifier
                    .padding(start = MaterialTheme.padding.small)
                    .size(60.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = chat.name.first().toString(),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
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
                    text = chatName,
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
                        if (chat.lastMessage.authorId == myUserId) {
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
                    if (chat.lastMessage.type == MessageType.MESSAGE) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                                    append(
                                        if (myUserId == chat.lastMessage.authorId) stringResource(R.string.text_you_sent)
                                        else stringResource(
                                            R.string.text_user_sent,
                                            chat.participants.find { it.user.id == chat.lastMessage.authorId }?.user?.fullName
                                                ?: ""
                                        )
                                    )
                                }
                                append(" $lastMessageText")
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, false)
                        )
                    } else {
                        Text(
                            text = lastMessageText ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, false)
                        )
                    }
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
fun GroupChatComponentPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = false) {
        GroupChatComponent(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = 8.dp, top = 10.dp),
            chat = Chat(
                1,
                "Family chat",
                ChatType.GROUP,
                null,
                lastMessage = Message(
                    1,
                    1,
                    1,
                    authorFullName = "fullname",
                    authorProfilePictureUrl = null,
                    MessageType.CHAT_CREATED,
                    "Chat created",
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