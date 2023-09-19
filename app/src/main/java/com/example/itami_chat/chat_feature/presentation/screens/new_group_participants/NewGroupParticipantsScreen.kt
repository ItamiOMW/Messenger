package com.example.itami_chat.chat_feature.presentation.screens.new_group_participants

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.example.itami_chat.R
import com.example.itami_chat.core.presentation.components.UserComponent
import com.example.itami_chat.core.presentation.components.SmallUserComponent
import com.example.itami_chat.core.presentation.components.pull_refresh.PullRefreshIndicator
import com.example.itami_chat.core.presentation.components.pull_refresh.pullRefresh
import com.example.itami_chat.core.presentation.components.pull_refresh.rememberPullRefreshState
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NewGroupParticipantsScreen(
    onNavigateBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    onNavigateToNewGroupDetails: (userIds: List<Int>) -> Unit,
    imageLoader: ImageLoader,
    state: NewGroupParticipantsState,
    searchQueryState: String,
    uiEvent: Flow<NewGroupParticipantsUiEvent>,
    onEvent: (event: NewGroupParticipantsEvent) -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is NewGroupParticipantsUiEvent.OnShowSnackbar -> {
                    onShowSnackbar(event.message)
                }
            }
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoadingContacts,
        onRefresh = { onEvent(NewGroupParticipantsEvent.OnUpdateContactsList) }
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
                                onEvent(NewGroupParticipantsEvent.OnSearchQueryInputChange(newValue))
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
                            text = stringResource(id = R.string.title_new_group),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (state.isSearchFieldVisible) {
                                onEvent(NewGroupParticipantsEvent.OnChangeSearchFieldVisibility)
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
                            onClick = { onEvent(NewGroupParticipantsEvent.OnChangeSearchFieldVisibility) }
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
        floatingActionButton = {
            Box(
                modifier = Modifier.padding(bottom = MaterialTheme.padding.medium)
            ) {
                FloatingActionButton(
                    onClick = {
                        onNavigateToNewGroupDetails(state.selectedContacts.map { it.id })
                    },
                    modifier = Modifier
                        .size(60.dp),
                    shape = RoundedCornerShape(30.dp),
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
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
                .pullRefresh(pullRefreshState, enabled = !state.isLoadingContacts),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    AnimatedVisibility(visible = state.selectedContacts.isNotEmpty()) {
                        LazyVerticalGrid(
                            modifier = Modifier
                                .padding(
                                    start = MaterialTheme.padding.small,
                                    end = MaterialTheme.padding.small
                                )
                                .animateContentSize(),
                            columns = GridCells.Adaptive(120.dp)
                        ) {
                            items(state.selectedContacts, key = { it.id }) { simpleUser ->
                                SmallUserComponent(
                                    modifier = Modifier
                                        .animateItemPlacement()
                                        .sizeIn(maxWidth = 120.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable {
                                            onEvent(
                                                NewGroupParticipantsEvent.OnSelectUser(
                                                    simpleUser
                                                )
                                            )
                                        },
                                    simpleUser = simpleUser,
                                    imageLoader = imageLoader
                                )
                            }
                        }
                    }
                    AnimatedVisibility(visible = state.selectedContacts.isEmpty()) {
                        Text(
                            text = stringResource(R.string.text_who_to_add_to_the_chat),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                            modifier = Modifier.padding(start = MaterialTheme.padding.medium)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                if (state.contacts.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        items(items = state.contacts, key = { it.id }) { user ->
                            UserComponent(
                                simpleUser = user,
                                imageLoader = imageLoader,
                                isSelected = state.selectedContacts.any { it.id == user.id },
                                modifier = Modifier
                                    .animateItemPlacement()
                                    .fillMaxWidth()
                                    .clickable {
                                        onEvent(NewGroupParticipantsEvent.OnSelectUser(user))
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