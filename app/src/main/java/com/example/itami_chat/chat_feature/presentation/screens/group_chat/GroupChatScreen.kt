package com.example.itami_chat.chat_feature.presentation.screens.group_chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.MessageType
import com.example.itami_chat.chat_feature.domain.model.ParticipantRole
import com.example.itami_chat.core.presentation.components.DefaultDialogComponent
import com.example.itami_chat.chat_feature.presentation.components.EditMessageTextField
import com.example.itami_chat.chat_feature.presentation.components.GroupMessageComponent
import com.example.itami_chat.chat_feature.presentation.components.MyMessageComponent
import com.example.itami_chat.chat_feature.presentation.util.getText
import com.example.itami_chat.core.presentation.state.StandardTextFieldState
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatScreen(
    onShowSnackbar: (message: String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToChatProfile: (chatId: Int) -> Unit,
    onNavigateToUserProfile: (userId: Int) -> Unit,
    imageLoader: ImageLoader,
    state: GroupChatState,
    messageInputState: StandardTextFieldState,
    editMessageInputState: StandardTextFieldState,
    uiEvent: Flow<GroupChatUiEvent>,
    onEvent: (GroupChatEvent) -> Unit,
) {
    val lazyListState = rememberLazyListState()

    val showMoreOptionsMenu = rememberSaveable {
        mutableStateOf(false)
    }

    val usersOnline = remember(state.chat?.participants) {
        state.chat?.participants?.count { it.user.isOnline } ?: 0
    }

    val screenWidth = LocalConfiguration.current.screenWidthDp

    val messageMaxWidth = remember(screenWidth) {
        (screenWidth * 0.75).dp
    }

    LaunchedEffect(key1 = true, key2 = state.messages.size) {
        uiEvent.collect { event ->
            when (event) {
                is GroupChatUiEvent.MessagePageLoaded -> {

                }

                is GroupChatUiEvent.NewMessage -> {
                    if (state.messages.isNotEmpty()) {
                        lazyListState.scrollToItem(0)
                    }
                }

                is GroupChatUiEvent.OnShowSnackbar -> {
                    onShowSnackbar(event.message)
                }

                GroupChatUiEvent.OnNavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    LaunchedEffect(key1 = lazyListState.canScrollForward) {
        if (!lazyListState.canScrollForward && !state.isLoadingNextMessages && !state.endReached && state.errorMessage == null) {
            onEvent(GroupChatEvent.OnLoadNextMessages)
        }
    }

    if (state.showLeaveChatDialog && state.chat != null) {
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
                onEvent(GroupChatEvent.OnHideLeaveChatDialog)
            },
            onConfirm = {
                onEvent(GroupChatEvent.OnLeaveChat)
            }
        )
    }

    if (state.showDeleteMessageDialog && state.messageToDelete != null) {
        DefaultDialogComponent(
            title = stringResource(id = R.string.text_delete_message),
            text = stringResource(id = R.string.text_delete_message_warning),
            confirmButtonText = stringResource(id = R.string.text_delete_message),
            onDismiss = {
                onEvent(GroupChatEvent.OnHideDeleteMessageDialog)
            },
            onConfirm = {
                onEvent(GroupChatEvent.OnDeleteMessage(state.messageToDelete))
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = TopAppBarDefaults.windowInsets,
                title = {
                    if (state.chat != null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(
                                    top = MaterialTheme.padding.small,
                                    bottom = MaterialTheme.padding.extraSmall
                                )
                                .clickable { onNavigateToChatProfile(state.chat.id) }
                        ) {
                            if (state.chat.chatPictureUrl != null) {
                                AsyncImage(
                                    model = state.chat.chatPictureUrl,
                                    contentDescription = stringResource(R.string.desc_chat_picture),
                                    imageLoader = imageLoader,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .padding(start = MaterialTheme.padding.small)
                                        .size(50.dp)
                                        .clip(RoundedCornerShape(30.dp)),
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .padding(start = MaterialTheme.padding.small)
                                        .size(50.dp)
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
                                    onEvent(GroupChatEvent.OnShowLeaveChatDialog)
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.messageToEdit != null) {
                    EditMessageTextField(
                        message = state.messageToEdit,
                        editMessageInputState = editMessageInputState,
                        onValueChange = { newValue ->
                            onEvent(GroupChatEvent.OnEditMessageInputValueChange(newValue))
                        },
                        onConfirm = {
                            onEvent(
                                GroupChatEvent.OnEditMessage(
                                    message = state.messageToEdit.copy(
                                        text = editMessageInputState.text
                                    )
                                )
                            )
                        },
                        onDismiss = {
                            onEvent(GroupChatEvent.OnHideEditMessageTextField)
                        }
                    )
                } else {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        value = messageInputState.text,
                        onValueChange = { newValue ->
                            onEvent(GroupChatEvent.OnMessageInputValueChange(newValue))
                        },
                        maxLines = 5,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                                0.6f
                            ),
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                                0.6f
                            )
                        ),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.title_message),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f)
                            )
                        }
                    )
                    AnimatedVisibility(
                        visible = messageInputState.text.isNotBlank(),
                        modifier = Modifier.weight(0.2f)
                    ) {
                        IconButton(
                            onClick = {
                                if (!state.isSendingMessage) {
                                    onEvent(GroupChatEvent.OnSendMessage)
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_send),
                                contentDescription = stringResource(R.string.desc_send_message),
                                modifier = Modifier.size(32.dp),
                                tint = if (state.isSendingMessage) MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                    0.6f
                                )
                                else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                reverseLayout = true
            ) {
                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                }
                item {
                    if (state.isLoadingNextMessages) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                if (state.chat != null) {
                    itemsIndexed(
                        items = state.messages,
                        key = { index, item -> item.id }
                    ) { index, message ->
                        if (state.me?.user?.id !in message.usersSeenMessage) {
                            onEvent(GroupChatEvent.OnReadMessage(message.id))
                        }
                        if (message.type == MessageType.MESSAGE) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                if (message.authorId == state.me?.user?.id) {
                                    MyMessageComponent(
                                        message = message,
                                        imageLoader = imageLoader,
                                        onImageClicked = { url ->

                                        },
                                        onEditMessage = {
                                            onEvent(
                                                GroupChatEvent.OnShowEditMessageTextField(
                                                    message
                                                )
                                            )
                                        },
                                        onDeleteMessage = {
                                            onEvent(GroupChatEvent.OnShowDeleteMessageDialog(message))
                                        },
                                        modifier = Modifier
                                            .sizeIn(maxWidth = messageMaxWidth)
                                            .align(Alignment.CenterEnd)
                                            .padding(end = MaterialTheme.padding.small)
                                    )
                                } else {
                                    GroupMessageComponent(
                                        message = message,
                                        imageLoader = imageLoader,
                                        onUserProfilePictureClicked = {
                                            onNavigateToUserProfile(message.authorId)
                                        },
                                        modifier = Modifier
                                            .sizeIn(maxWidth = messageMaxWidth)
                                            .align(Alignment.CenterStart)
                                            .padding(start = MaterialTheme.padding.small)
                                    )
                                }
                            }
                        } else {
                            if (state.me != null) {
                                Text(
                                    text = message.getText(LocalContext.current, state.me.user.id),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(
                                            start = MaterialTheme.padding.medium,
                                            end = MaterialTheme.padding.medium,
                                        )
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
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