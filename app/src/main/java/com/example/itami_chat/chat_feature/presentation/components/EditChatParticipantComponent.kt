package com.example.itami_chat.chat_feature.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant
import com.example.itami_chat.chat_feature.domain.model.ParticipantRole
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme


@Composable
fun EditChatParticipantComponent(
    participant: ChatParticipant,
    me: ChatParticipant,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    onMoreOptionsClicked: () -> Unit,
) {
    val context = LocalContext.current

    val roleText = remember(participant) {
        when (participant.role) {
            ParticipantRole.MEMBER -> {
                context.getString(R.string.text_member)
            }

            ParticipantRole.ADMIN -> {
                context.getString(R.string.text_admin)
            }

            ParticipantRole.CREATOR -> {
                context.getString(R.string.text_owner)
            }
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f, false)
        ) {
            AsyncImage(
                model = participant.user.profilePictureUrl,
                contentDescription = stringResource(id = R.string.desc_profile_picture),
                imageLoader = imageLoader,
                error = painterResource(id = R.drawable.sniper_mask),
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(30.dp))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = participant.user.fullName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Text(
                    text = roleText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        }
        when (me.role) {
            ParticipantRole.MEMBER -> {
                Unit
            }

            ParticipantRole.ADMIN -> {
                if (participant.role == ParticipantRole.MEMBER && participant.user.id != me.user.id) {
                    IconButton(
                        onClick = {
                            onMoreOptionsClicked()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_more_vert),
                            contentDescription = stringResource(id = R.string.more_vert_ic_desc)
                        )
                    }
                }
            }

            ParticipantRole.CREATOR -> {
                if (participant.user.id != me.user.id) {
                    IconButton(
                        onClick = {
                            onMoreOptionsClicked()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_more_vert),
                            contentDescription = stringResource(id = R.string.more_vert_ic_desc)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun EditChatParticipantComponentPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = false) {
        EditChatParticipantComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.5.dp, top = 6.5.dp)
                .clickable { }
                .padding(start = 10.dp, end = 10.dp),
            participant = ChatParticipant(
                user = SimpleUser(
                    0,
                    "Itami",
                    username = null,
                    profilePictureUrl = null,
                    isOnline = false,
                    lastActivity = 0L,
                ),
                role = ParticipantRole.CREATOR
            ),
            me = ChatParticipant(
                user = SimpleUser(
                    0,
                    "Itami",
                    username = null,
                    profilePictureUrl = null,
                    isOnline = false,
                    lastActivity = 0L,
                ),
                role = ParticipantRole.CREATOR
            ),
            onMoreOptionsClicked = {},
            imageLoader = ImageLoader(LocalContext.current)
        )
    }
}