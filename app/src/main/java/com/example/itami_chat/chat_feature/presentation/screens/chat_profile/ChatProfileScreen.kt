package com.example.itami_chat.chat_feature.presentation.screens.chat_profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant
import com.example.itami_chat.chat_feature.domain.model.ParticipantRole
import com.example.itami_chat.chat_feature.presentation.components.ChatParticipantComponent
import com.example.itami_chat.core.presentation.components.DefaultDialogComponent
import com.example.itami_chat.core.presentation.components.IconToTextComponent
import com.example.itami_chat.core.presentation.navigation.NavResultCallback
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatProfileScreen(
    onShowSnackbar: (message: String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateBackToChats: () -> Unit,
    onNavigateToEditChat: (chatId: Int, result: NavResultCallback<Chat?>) -> Unit,
    onNavigateToAddChatParticipants: (chatId: Int, result: NavResultCallback<List<ChatParticipant>?>) -> Unit,
    onNavigateToUserProfile: (userId: Int) -> Unit,
    imageLoader: ImageLoader,
    state: ChatProfileState,
    uiEvent: Flow<ChatProfileUiEvent>,
    onEvent: (event: ChatProfileEvent) -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is ChatProfileUiEvent.OnShowSnackbar -> {
                    onShowSnackbar(event.message)
                }

                is ChatProfileUiEvent.OnNavigateBack -> {
                    onNavigateBack()
                }

                is ChatProfileUiEvent.LeftChat -> {
                    onNavigateBackToChats()
                }
            }
        }
    }

    val usersOnline = remember(state.chat?.participants) {
        state.chat?.participants?.count { it.user.isOnline } ?: 0
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val isTopBarCollapsed = remember(scrollBehavior.state.collapsedFraction) {
        scrollBehavior.state.collapsedFraction > 0.5f
    }

    val showMoreOptionsMenu = rememberSaveable {
        mutableStateOf(false)
    }

    if (state.chat != null && state.showLeaveChatDialog) {
        DefaultDialogComponent(
            title = stringResource(id = R.string.text_leave_chat),
            text = stringResource(
                id = if (state.me?.role == ParticipantRole.CREATOR) {
                    R.string.text_leave_and_delete_chat_warning
                } else R.string.text_leave_chat_warning, state.chat.name
            ),
            confirmButtonText = stringResource(
                id = if (state.me?.role == ParticipantRole.CREATOR) {
                    R.string.text_delete_and_leave_chat
                } else R.string.text_leave_chat
            ),
            onDismiss = {
                onEvent(ChatProfileEvent.OnHideLeaveChatDialog)
            },
            onConfirm = {
                onEvent(ChatProfileEvent.OnLeaveChat)
            }
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (state.chat != null) {
                LargeTopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (state.chat.chatPictureUrl != null) {
                                AsyncImage(
                                    model = state.chat.chatPictureUrl,
                                    contentDescription = stringResource(R.string.desc_chat_picture),
                                    imageLoader = imageLoader,
                                    modifier = Modifier
                                        .padding(start = MaterialTheme.padding.small)
                                        .size(if (isTopBarCollapsed) 50.dp else 60.dp)
                                        .clip(RoundedCornerShape(30.dp)),
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .padding(start = MaterialTheme.padding.small)
                                        .size(if (isTopBarCollapsed) 50.dp else 60.dp)
                                        .clip(RoundedCornerShape(30.dp))
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = state.chat.name.first().toString(),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                            Column {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text(
                                        text = state.chat.name,
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = stringResource(
                                            R.string.text_online_members,
                                            state.chat.participants.size,
                                            usersOnline
                                        ),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                                    )
                                }
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
                        IconButton(
                            onClick = {
                                onNavigateToEditChat(state.chat.id) { chatResult ->
                                    if (chatResult != null) {
                                        onEvent(ChatProfileEvent.OnUpdateChat(chatResult))
                                    }
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = stringResource(R.string.navigate_to_edit_chat_desc),
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
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
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(
                                                id = if (state.me?.role == ParticipantRole.CREATOR) {
                                                    R.string.text_delete_and_leave_chat
                                                } else R.string.text_leave_chat
                                            ),
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_leave),
                                            contentDescription = stringResource(R.string.desc_leave_chat),
                                            modifier = Modifier.size(24.dp),
                                        )
                                    },
                                    onClick = {
                                        showMoreOptionsMenu.value = false
                                        onEvent(ChatProfileEvent.OnShowLeaveChatDialog)
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column {
                if (state.chat != null) {
                    LazyColumn {
                        item {
                            IconToTextComponent(
                                text = stringResource(R.string.text_add_new_participants),
                                icon = painterResource(id = R.drawable.ic_add_contact),
                                color = MaterialTheme.colorScheme.primary,
                                textStyle = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onNavigateToAddChatParticipants(state.chat.id) { participants ->
                                            if (!participants.isNullOrEmpty()) {
                                                onEvent(
                                                    ChatProfileEvent.OnAddChatParticipants(
                                                        participants = participants
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    .padding(
                                        bottom = MaterialTheme.padding.medium,
                                        top = MaterialTheme.padding.medium
                                    ),
                            )
                        }
                        items(state.chat.participants, key = { it.user.id }) { participant ->
                            ChatParticipantComponent(
                                participant = participant,
                                imageLoader = imageLoader,
                                modifier = Modifier
                                    .animateItemPlacement()
                                    .fillMaxWidth()
                                    .clickable {
                                        if (participant.user.id != state.me?.user?.id) {
                                            onNavigateToUserProfile(participant.user.id)
                                        }
                                    }
                                    .padding(
                                        start = MaterialTheme.padding.small,
                                        end = MaterialTheme.padding.small,
                                        bottom = MaterialTheme.padding.extraSmall,
                                        top = MaterialTheme.padding.extraSmall
                                    ),
                            )
                        }
                    }
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

}