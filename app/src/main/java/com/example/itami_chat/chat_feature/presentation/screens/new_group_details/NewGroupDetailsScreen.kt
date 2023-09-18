package com.example.itami_chat.chat_feature.presentation.screens.new_group_details

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
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.itami_chat.R
import com.example.itami_chat.core.presentation.components.UserComponent
import com.example.itami_chat.core.presentation.components.UnderlinedTextFieldComponent
import com.example.itami_chat.core.presentation.state.StandardTextFieldState
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NewGroupDetailsScreen(
    imageLoader: ImageLoader,
    onShowSnackbar: (message: String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateBackToChats: () -> Unit,
    state: NewGroupDetailsState,
    chatNameState: StandardTextFieldState,
    uiEvent: Flow<NewGroupDetailsUiEvent>,
    onEvent: (NewGroupDetailsEvent) -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is NewGroupDetailsUiEvent.OnChatCreated -> {
                    onNavigateBackToChats()
                }

                is NewGroupDetailsUiEvent.OnShowSnackbar -> {
                    onShowSnackbar(event.message)
                }
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let { onEvent(NewGroupDetailsEvent.OnPictureUriChange(uri)) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_new_group),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
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
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier.padding(bottom = MaterialTheme.padding.medium)
            ) {
                FloatingActionButton(
                    onClick = {
                        if (!state.isCreatingChat) {
                            onEvent(NewGroupDetailsEvent.OnCreateChat)
                        }
                    },
                    modifier = Modifier
                        .size(60.dp),
                    shape = RoundedCornerShape(30.dp),
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_done),
                        contentDescription = stringResource(R.string.desc_navigate_next),
                        modifier = Modifier
                            .size(28.dp)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
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
                        .padding(MaterialTheme.padding.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (state.chatPictureUri == null) {
                        IconButton(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(30.dp))
                                .background(MaterialTheme.colorScheme.primary),
                            onClick = {
                                launcher.launch(
                                    PickVisualMediaRequest(
                                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add_photo),
                                contentDescription = stringResource(id = R.string.desc_chat_picture),
                                modifier = Modifier.size(28.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    } else {
                        AsyncImage(
                            model = state.chatPictureUri,
                            contentDescription = stringResource(id = R.string.desc_chat_picture),
                            imageLoader = imageLoader,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(30.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .clickable {
                                    launcher.launch(
                                        PickVisualMediaRequest(
                                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                },
                        )
                    }
                    Spacer(modifier = Modifier.width(MaterialTheme.padding.small))
                    UnderlinedTextFieldComponent(
                        modifier = Modifier.fillMaxWidth(),
                        textValue = { chatNameState.text },
                        onValueChange = { newValue ->
                            onEvent(NewGroupDetailsEvent.OnChatNameValueChange(newValue))
                        },
                        error = { chatNameState.errorMessage },
                        label = stringResource(R.string.title_chat_name)
                    )
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                if (state.participantsErrorMessage == null) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        items(items = state.participants, key = { it.id }) { user ->
                            UserComponent(
                                simpleUser = user,
                                imageLoader = imageLoader,
                                modifier = Modifier
                                    .animateItemPlacement()
                                    .fillMaxWidth()
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
            if (state.participantsErrorMessage != null) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.participantsErrorMessage,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                    TextButton(
                        onClick = {
                            onEvent(NewGroupDetailsEvent.OnReloadParticipants)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.text_try_again),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            if (state.isCreatingChat && state.isLoadingParticipants) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

}