package com.example.itami_chat.profile_feature.presentation.search_users

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.example.itami_chat.R
import com.example.itami_chat.core.presentation.components.UserComponent
import com.example.itami_chat.core.presentation.ui.theme.padding
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchUsersScreen(
    onShowSnackbar: (message: String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToUserProfile: (userId: Int) -> Unit,
    imageLoader: ImageLoader,
    state: SearchUsersState,
    searchInputState: String,
    uiEvent: Flow<SearchUsersUiEvent>,
    onEvent: (event: SearchUsersEvent) -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is SearchUsersUiEvent.OnNavigateBack -> {
                    onNavigateBack()
                }

                is SearchUsersUiEvent.OnShowSnackbar -> {
                    onShowSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = searchInputState,
                        onValueChange = { newValue ->
                            onEvent(SearchUsersEvent.OnSearchInputChange(newValue))
                        },
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
                                text = stringResource(R.string.title_search_users),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
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
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (state.users.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(items = state.users, key = { it.id }) { user ->
                        UserComponent(
                            simpleUser = user,
                            imageLoader = imageLoader,
                            modifier = Modifier
                                .animateItemPlacement()
                                .fillMaxWidth()
                                .clickable {
                                    onNavigateToUserProfile(user.id)
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
                Text(
                    text = stringResource(R.string.text_no_users_found),
                    color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
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