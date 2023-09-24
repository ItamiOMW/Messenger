package com.example.itami_chat.profile_feature.presentation.user_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.model.ContactRequestStatus
import com.example.itami_chat.core.presentation.components.InfoComponent
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import com.example.itami_chat.core.utils.DateTimeUtil
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    onShowSnackbar: (message: String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToDialogChat: (userId: Int) -> Unit,
    imageLoader: ImageLoader,
    state: UserProfileState,
    uiEvent: Flow<UserProfileUiEvent>,
    onEvent: (event: UserProfileEvent) -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is UserProfileUiEvent.OnNavigateBack -> {
                    onNavigateBack()
                }

                is UserProfileUiEvent.OnShowSnackbar -> {
                    onShowSnackbar(event.message)
                }
            }
        }
    }

    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val dialogUserLastSeen = remember(key1 = state.userProfile?.lastActivity) {
        state.userProfile?.lastActivity?.let { timestamp ->
            DateTimeUtil.formatUtcDateTimeForLastSeen(timestamp = timestamp, context = context)
        }
    }

    val isTopBarCollapsed = remember(scrollBehavior.state.collapsedFraction) {
        scrollBehavior.state.collapsedFraction > 0.5f
    }

    val showMoreOptionsMenu = rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            if (state.userProfile != null) {
                LargeTopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = state.userProfile.profilePictureUrl,
                                contentDescription = stringResource(R.string.desc_chat_picture),
                                imageLoader = imageLoader,
                                contentScale = ContentScale.Crop,
                                error = painterResource(id = R.drawable.sniper_mask),
                                modifier = Modifier
                                    .padding(start = MaterialTheme.padding.small)
                                    .size(if (isTopBarCollapsed) 50.dp else 60.dp)
                                    .clip(RoundedCornerShape(30.dp)),
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                            Column(
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = state.userProfile.fullName,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (state.userProfile.isOnline) stringResource(id = R.string.text_online)
                                    else dialogUserLastSeen ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onNavigateBack()
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_left),
                                contentDescription = stringResource(R.string.desc_navigate_back),
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        Box {
                            IconButton(
                                onClick = {
                                    showMoreOptionsMenu.value = true
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_more_vert),
                                    contentDescription = stringResource(R.string.more_vert_ic_desc),
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            DropdownMenu(
                                expanded = showMoreOptionsMenu.value,
                                onDismissRequest = { showMoreOptionsMenu.value = false },
                            ) {
                                if (state.userProfile.canSendMessage) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = stringResource(R.string.text_send_message),
                                                style = MaterialTheme.typography.bodyLarge,
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_write_message),
                                                contentDescription = stringResource(R.string.desc_write_icon),
                                                modifier = Modifier.size(24.dp),
                                            )
                                        },
                                        onClick = {
                                            showMoreOptionsMenu.value = false
                                            onNavigateToDialogChat(state.userProfile.userId)
                                        }
                                    )
                                }
                                if (state.userProfile.contactRequest != null &&
                                    state.userProfile.contactRequest.status == ContactRequestStatus.PENDING
                                ) {
                                    if (state.userProfile.contactRequest.sender.id == state.userProfile.userId) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = stringResource(R.string.text_accept_contact_request),
                                                    style = MaterialTheme.typography.bodyLarge,
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_done),
                                                    contentDescription = stringResource(R.string.desc_done_icon),
                                                    modifier = Modifier.size(24.dp),
                                                )
                                            },
                                            onClick = {
                                                onEvent(UserProfileEvent.OnAcceptContactRequest)
                                                showMoreOptionsMenu.value = false
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = stringResource(R.string.text_decline_contact_request),
                                                    style = MaterialTheme.typography.bodyLarge,
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_close),
                                                    contentDescription = stringResource(R.string.desc_done_icon),
                                                    modifier = Modifier.size(24.dp),
                                                )
                                            },
                                            onClick = {
                                                onEvent(UserProfileEvent.OnDeclineContactRequest)
                                                showMoreOptionsMenu.value = false
                                            }
                                        )
                                    } else {
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = stringResource(R.string.text_cancel_contact_request),
                                                    style = MaterialTheme.typography.bodyLarge,
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_close),
                                                    contentDescription = stringResource(R.string.desc_done_icon),
                                                    modifier = Modifier.size(24.dp),
                                                )
                                            },
                                            onClick = {
                                                onEvent(UserProfileEvent.OnCancelContactRequest)
                                                showMoreOptionsMenu.value = false
                                            }
                                        )
                                    }
                                }
                                if (!state.userProfile.isContact && state.userProfile.contactRequest == null) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = stringResource(R.string.text_add_contact),
                                                style = MaterialTheme.typography.bodyLarge,
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_add_contact),
                                                contentDescription = stringResource(R.string.desc_person_add_icon),
                                                modifier = Modifier.size(24.dp),
                                            )
                                        },
                                        onClick = {
                                            onEvent(UserProfileEvent.OnSendContactRequest)
                                            showMoreOptionsMenu.value = false
                                        }
                                    )
                                }
                                if (state.userProfile.isContact) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = stringResource(R.string.text_delete_contact),
                                                style = MaterialTheme.typography.bodyLarge,
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_delete),
                                                contentDescription = stringResource(R.string.desc_delete_icon),
                                                modifier = Modifier.size(24.dp),
                                            )
                                        },
                                        onClick = {
                                            onEvent(UserProfileEvent.OnDeleteContact)
                                            showMoreOptionsMenu.value = false
                                        }
                                    )
                                }
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = if (!state.userProfile.isBlockedByMe) stringResource(
                                                R.string.text_block_user
                                            )
                                            else stringResource(R.string.text_unblock_user),
                                            style = MaterialTheme.typography.bodyLarge,
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_block),
                                            contentDescription = stringResource(R.string.desc_block_icon)
                                        )
                                    },
                                    onClick = {
                                        if (!state.userProfile.isBlockedByMe) {
                                            onEvent(UserProfileEvent.OnBlockUser)
                                        } else {
                                            onEvent(UserProfileEvent.OnUnblockUser)
                                        }
                                        showMoreOptionsMenu.value = false
                                    }
                                )
                            }
                        }
                    }
                )
            }
        },

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (state.userProfile != null) {
                Column {
                    if (state.userProfile.username != null) {
                        InfoComponent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = MaterialTheme.padding.small, top = MaterialTheme.padding.small),
                            text = state.userProfile.username,
                            label = stringResource(id = R.string.title_username),
                            icon = painterResource(id = R.drawable.ic_alternate_email)
                        )
                    }
                    if (state.userProfile.bio != null) {
                        InfoComponent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = MaterialTheme.padding.small, top = MaterialTheme.padding.small),
                            text = state.userProfile.bio,
                            label = stringResource(id = R.string.title_about_me),
                            icon = painterResource(id = R.drawable.ic_info)
                        )
                    }
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

}