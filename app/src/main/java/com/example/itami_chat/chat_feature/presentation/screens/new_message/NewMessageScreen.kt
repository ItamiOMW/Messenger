package com.example.itami_chat.chat_feature.presentation.screens.new_message

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.components.IconToTextComponent
import com.example.itami_chat.core.presentation.components.UserComponent
import com.example.itami_chat.core.presentation.components.pull_refresh.PullRefreshIndicator
import com.example.itami_chat.core.presentation.components.pull_refresh.pullRefresh
import com.example.itami_chat.core.presentation.components.pull_refresh.rememberPullRefreshState
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NewMessageScreen(
    onShowSnackbar: (message: String) -> Unit,
    onNavigateToSearchUsers: () -> Unit,
    onNavigateToNewGroup: () -> Unit,
    onNavigateToDialogChat: (dialogUserId: Int) -> Unit,
    onNavigateBack: () -> Unit,
    imageLoader: ImageLoader,
    state: NewMessageState,
    searchQueryState: String,
    uiEvent: Flow<NewMessageUiEvent>,
    onEvent: (NewMessageEvent) -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is NewMessageUiEvent.OnShowSnackbar -> onShowSnackbar(event.message)
            }
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoadingContacts,
        onRefresh = { onEvent(NewMessageEvent.OnUpdateContactsList) }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (state.isSearchFieldVisible) {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = searchQueryState,
                            onValueChange = { newValue ->
                                onEvent(NewMessageEvent.OnSearchQueryInputChange(newValue))
                            },
                            enabled = !state.isLoadingContacts,
                            textStyle = MaterialTheme.typography.bodyLarge,
                            singleLine = true,
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
                                    text = stringResource(R.string.title_search),
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.title_new_message),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (state.isSearchFieldVisible) {
                                onEvent(NewMessageEvent.OnChangeSearchFieldVisibility)
                            } else {
                                onNavigateBack()
                            }
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
                    if (!state.isSearchFieldVisible) {
                        IconButton(
                            onClick = { onEvent(NewMessageEvent.OnChangeSearchFieldVisibility) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = stringResource(R.string.search_ic_desc),
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                }
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .pullRefresh(pullRefreshState, enabled = !state.isLoadingContacts),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column {
                    IconToTextComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onNavigateToNewGroup()
                            }
                            .padding(
                                bottom = MaterialTheme.padding.medium,
                                top = MaterialTheme.padding.medium
                            ),
                        text = stringResource(id = R.string.title_new_group),
                        icon = painterResource(id = R.drawable.ic_new_group),
                        color = MaterialTheme.colorScheme.onBackground,
                        textStyle = MaterialTheme.typography.bodyLarge
                    )
                    IconToTextComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onNavigateToSearchUsers()
                            }
                            .padding(
                                bottom = MaterialTheme.padding.medium,
                                top = MaterialTheme.padding.medium
                            ),
                        text = stringResource(R.string.title_new_contact),
                        icon = painterResource(id = R.drawable.ic_add_contact),
                        color = MaterialTheme.colorScheme.onBackground,
                        textStyle = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                if (state.contacts.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        items(items = state.contacts, key = { it.id }) { user ->
                            UserComponent(
                                simpleUser = user,
                                imageLoader = imageLoader,
                                modifier = Modifier
                                    .animateItemPlacement()
                                    .fillMaxWidth()
                                    .clickable {
                                        onNavigateToDialogChat(user.id)
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
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.text_no_contacts_found),
                            color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

            }
            PullRefreshIndicator(
                refreshing = { state.isLoadingContacts },
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}


@Preview
@Composable
fun NewMessageScreenPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = false) {
        NewMessageScreen(
            onShowSnackbar = { },
            onNavigateToSearchUsers = { },
            onNavigateToNewGroup = { },
            onNavigateToDialogChat = {},
            onNavigateBack = { },
            imageLoader = ImageLoader(LocalContext.current),
            state = NewMessageState(isSearchFieldVisible = false),
            searchQueryState = "",
            uiEvent = flow { },
            onEvent = {}
        )
    }
}