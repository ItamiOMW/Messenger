package com.example.itami_chat.chat_feature.presentation.screens.edit_chat

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.chat_feature.presentation.components.EditChatParticipantComponent
import com.example.itami_chat.chat_feature.presentation.components.EditChatParticipantDialogComponent
import com.example.itami_chat.core.presentation.components.IconToTextComponent
import com.example.itami_chat.core.presentation.components.UnderlinedTextFieldComponent
import com.example.itami_chat.core.presentation.state.StandardTextFieldState
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditChatScreen(
    onShowSnackbar: (message: String) -> Unit,
    onNavigateBack: (chat: Chat?) -> Unit,
    imageLoader: ImageLoader,
    state: EditChatState,
    chatNameState: StandardTextFieldState,
    uiEvent: Flow<EditChatUiEvent>,
    onEvent: (event: EditChatEvent) -> Unit,
) {
    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is EditChatUiEvent.OnNavigateBack -> {
                    onNavigateBack(event.chat)
                }

                is EditChatUiEvent.OnShowSnackbar -> {
                    onShowSnackbar(event.message)
                }
            }
        }
    }

    BackHandler {
        onNavigateBack(state.chat)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let { onEvent(EditChatEvent.OnChatPictureUriChange(uri)) } }

    if (state.participantToEdit != null && state.me != null) {
        EditChatParticipantDialogComponent(
            chatParticipantToEdit = state.participantToEdit,
            me = state.me,
            onAssignAdminRole = {
                onEvent(EditChatEvent.OnAssignAdminRole(state.participantToEdit))
            },
            onRemoveAdminRole = {
                onEvent(EditChatEvent.OnRemoveAdminRole(state.participantToEdit))
            },
            onKickFromChat = {
                onEvent(EditChatEvent.OnDeleteParticipant(state.participantToEdit))
            },
            onDismiss = {
                onEvent(EditChatEvent.OnHideEditParticipantDialog)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNavigateBack(state.chat)
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
                title = {
                    Text(
                        text = stringResource(id = R.string.title_edit),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                actions = {
                    IconButton(
                        enabled = !state.isLoading,
                        onClick = {
                            onEvent(EditChatEvent.OnEditChat)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_done),
                            contentDescription = stringResource(R.string.edit_chat_desc),
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.padding.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (state.chatPictureUri == null) {
                        if (state.chat?.chatPictureUrl != null) {
                            AsyncImage(
                                model = state.chat.chatPictureUrl,
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
                                    text = state.chat?.name?.first().toString(),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        AsyncImage(
                            model = state.chatPictureUri,
                            contentDescription = stringResource(id = R.string.desc_chat_picture),
                            imageLoader = imageLoader,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(30.dp))
                                .background(MaterialTheme.colorScheme.primary),
                        )
                    }
                    Spacer(modifier = Modifier.width(MaterialTheme.padding.small))
                    UnderlinedTextFieldComponent(
                        modifier = Modifier.fillMaxWidth(),
                        textValue = { chatNameState.text },
                        onValueChange = { newValue ->
                            onEvent(EditChatEvent.OnChatNameValueChange(newValue))
                        },
                        error = { chatNameState.errorMessage },
                        label = stringResource(R.string.title_chat_name)
                    )
                }
                IconToTextComponent(
                    text = stringResource(R.string.text_set_photo),
                    icon = painterResource(id = R.drawable.ic_add_photo),
                    color = MaterialTheme.colorScheme.primary,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            launcher.launch(
                                PickVisualMediaRequest(
                                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                        .padding(
                            start = MaterialTheme.padding.small,
                            bottom = MaterialTheme.padding.medium,
                            top = MaterialTheme.padding.medium
                        ),
                )
                Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                LazyColumn {
                    if (state.chat != null && state.me != null) {
                        items(state.chat.participants, key = { it.user.id }) { participant ->
                            EditChatParticipantComponent(
                                participant = participant,
                                me = state.me,
                                imageLoader = imageLoader,
                                onMoreOptionsClicked = {
                                    onEvent(EditChatEvent.OnShowEditParticipantDialog(participant))
                                },
                                modifier = Modifier
                                    .animateItemPlacement()
                                    .fillMaxWidth()
                                    .clickable {
                                        //Todo open user profile
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
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}