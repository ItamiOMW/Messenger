package com.example.itami_chat.settings_feature.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.example.itami_chat.core.presentation.ui.theme.spacing
import com.example.itami_chat.core.utils.DateTimeUtil


@Composable
fun BlockedUserComponent(
    simpleUser: SimpleUser,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    onUnblockUser: () -> Unit,
) {
    val context = LocalContext.current

    val lastSeen = remember(simpleUser.lastActivity) {
        DateTimeUtil.formatUtcDateTimeForLastSeen(
            timestamp = simpleUser.lastActivity,
            context = context
        )
    }

    val showMoreOptionsMenu = rememberSaveable {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, false)
        ) {
            AsyncImage(
                model = simpleUser.profilePictureUrl,
                contentDescription = stringResource(id = R.string.desc_profile_picture),
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.sniper_mask),
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(30.dp))
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Column(
                modifier = Modifier,
            ) {
                Text(
                    text = simpleUser.fullName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Text(
                    text = if (simpleUser.isOnline) stringResource(R.string.text_online) else lastSeen,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (simpleUser.isOnline) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground.copy(0.6f),
                    maxLines = 1,
                )
            }
        }
        Box(
            modifier = Modifier.weight(0.1f),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    showMoreOptionsMenu.value = true
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vert),
                    contentDescription = stringResource(id = R.string.more_vert_ic_desc),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            DropdownMenu(
                expanded = showMoreOptionsMenu.value,
                onDismissRequest = {
                    showMoreOptionsMenu.value = false
                }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.text_unblock_user),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_block),
                            contentDescription = stringResource(R.string.desc_block_icon),
                            modifier = Modifier.size(24.dp),
                        )
                    },
                    onClick = {
                        showMoreOptionsMenu.value = false
                        onUnblockUser()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun SimpleUserComponentPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = false) {
        BlockedUserComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.5.dp, top = 6.5.dp)
                .clickable { }
                .padding(start = 10.dp, end = 10.dp),
            simpleUser = SimpleUser(
                0,
                "Itami",
                username = null,
                profilePictureUrl = null,
                isOnline = false,
                lastActivity = 0L,
            ),
            onUnblockUser = {},
            imageLoader = ImageLoader(LocalContext.current)
        )
    }
}