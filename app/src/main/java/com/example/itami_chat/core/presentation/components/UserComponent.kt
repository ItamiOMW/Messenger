package com.example.itami_chat.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun UserComponent(
    simpleUser: SimpleUser,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    val context = LocalContext.current

    val lastSeen = remember(simpleUser.lastActivity) {
        DateTimeUtil.formatUtcDateTimeForLastSeen(
            timestamp = simpleUser.lastActivity,
            context = context
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box {
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
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clip(RoundedCornerShape(30.dp))
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_done),
                        contentDescription = stringResource(R.string.desc_selected),
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
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
}

@Preview
@Composable
fun SimpleUserComponentPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = false) {
        UserComponent(
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
            imageLoader = ImageLoader(LocalContext.current)
        )
    }
}