package com.example.itami_chat.contacts_feature.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.itami_chat.core.domain.model.ContactRequest
import com.example.itami_chat.core.domain.model.ContactRequestStatus
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.example.itami_chat.core.presentation.ui.theme.spacing


@Composable
fun MyContactRequestComponent(
    contactRequest: ContactRequest,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
) {
    val user = contactRequest.sender

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = user.profilePictureUrl,
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
                text = user.fullName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Text(
                text = stringResource(R.string.text_cancel),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        onCancel()
                    },
            )

        }
    }
}

@Preview
@Composable
fun MyContactRequestComponentPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = false) {
        MyContactRequestComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.5.dp, top = 6.5.dp)
                .clickable { }
                .padding(start = 10.dp, end = 10.dp),
            contactRequest = ContactRequest(
                sender = SimpleUser(
                    0,
                    "Itami",
                    username = null,
                    profilePictureUrl = null,
                    isOnline = false,
                    lastActivity = 0L,
                ),
                recipient = SimpleUser(
                    0,
                    "Itami",
                    username = null,
                    profilePictureUrl = null,
                    isOnline = false,
                    lastActivity = 0L,
                ),
                status = ContactRequestStatus.PENDING,
                createdAt = 0L
            ),
            onCancel = {},
            imageLoader = ImageLoader(LocalContext.current)
        )
    }
}