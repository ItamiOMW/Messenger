package com.example.itami_chat.chat_feature.presentation.screens.chats

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.ChatType
import com.example.itami_chat.chat_feature.presentation.components.DialogChatComponent
import com.example.itami_chat.chat_feature.presentation.components.GroupChatComponent
import com.example.itami_chat.chat_feature.presentation.components.NavigationDrawerComponent
import com.example.itami_chat.core.domain.model.MessagesPermission
import com.example.itami_chat.core.domain.model.MyUser
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.util.NavigationItem
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.example.itami_chat.core.presentation.ui.theme.padding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatsScreen(
    onShowSnackbar: (message: String) -> Unit,
    onNavigateToRoute: (route: String) -> Unit,
    onNavigateToGroupChat: (chatId: Int) -> Unit,
    onNavigateToDialogChat: (dialogUserId: Int) -> Unit,
    onNavigateToSearchUsers: () -> Unit,
    onNavigateToNewMessage: () -> Unit,
    onNavigateToCreateNewGroup: () -> Unit,
    onNavigateToSettings: () -> Unit,
    imageLoader: ImageLoader,
    state: ChatsState,
    onEvent: (ChatsEvent) -> Unit,
    uiEvent: Flow<ChatsUiEvent>,
) {
    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is ChatsUiEvent.OnShowSnackbar -> {
                    onShowSnackbar(event.message)
                }
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val lazyListState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    val showMoreOptionsMenu = rememberSaveable {
        mutableStateOf(false)
    }

    if (state.myUser != null) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = true,
            drawerContent = {
                NavigationDrawerComponent(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    myUser = state.myUser,
                    isDarkMode = state.isDarkMode,
                    imageLoader = imageLoader,
                    items = listOf(
                        NavigationItem.ContactsFeature,
                        NavigationItem.CreateGroupScreen,
                        NavigationItem.CreateMessageScreen,
                    ),
                    bottomNavigationItem = NavigationItem.SettingsFeature,
                    onItemClick = { navItem ->
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        onNavigateToRoute(navItem.route)
                    },
                    onChangeDarkModeState = {
                        onEvent(ChatsEvent.ChangeDarkModeState)
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        drawerState.open()
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_menu),
                                    contentDescription = stringResource(R.string.menu_ic_desc),
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        title = {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        actions = {
                            IconButton(
                                onClick = { onNavigateToSearchUsers() }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_search),
                                    contentDescription = stringResource(R.string.search_ic_desc),
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Box {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            showMoreOptionsMenu.value = true
                                        }
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
                                                text = stringResource(R.string.text_new_message),
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                        },
                                        onClick = {
                                            showMoreOptionsMenu.value = false
                                            onNavigateToNewMessage()
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = stringResource(R.string.text_new_group),
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                        },
                                        onClick = {
                                            showMoreOptionsMenu.value = false
                                            onNavigateToCreateNewGroup()
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = stringResource(R.string.title_settings),
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                        },
                                        onClick = {
                                            showMoreOptionsMenu.value = false
                                            onNavigateToSettings()
                                        }
                                    )
                                }
                            }
                        }
                    )
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = !lazyListState.isScrollInProgress,
                        modifier = Modifier.padding(bottom = MaterialTheme.padding.medium),
                    ) {
                        FloatingActionButton(
                            onClick = {
                                onNavigateToNewMessage()
                            },
                            modifier = Modifier.size(60.dp),
                            shape = RoundedCornerShape(30.dp),
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.primary,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_write_message),
                                contentDescription = stringResource(R.string.desc_send_new_message),
                                modifier = Modifier
                                    .size(28.dp)
                                    .padding(start = 3.dp)
                            )
                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.End
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    LazyColumn(
                        modifier = Modifier.animateContentSize(),
                        state = lazyListState
                    ) {
                        items(items = state.chats, key = { item -> item.id }) { chat ->
                            when (chat.type) {
                                ChatType.DIALOG -> {
                                    DialogChatComponent(
                                        modifier = Modifier
                                            .animateItemPlacement()
                                            .fillMaxWidth()
                                            .clickable {
                                                onNavigateToDialogChat(
                                                    chat.participants.first { it.user.id != state.myUser.id }.user.id
                                                )
                                            }
                                            .padding(
                                                top = MaterialTheme.padding.small
                                            ),
                                        chat = chat,
                                        myUserId = state.myUser.id,
                                        imageLoader = imageLoader
                                    )
                                }

                                ChatType.GROUP -> {
                                    GroupChatComponent(
                                        modifier = Modifier
                                            .animateItemPlacement()
                                            .fillMaxWidth()
                                            .clickable { onNavigateToGroupChat(chat.id) }
                                            .padding(
                                                bottom = MaterialTheme.padding.small,
                                                top = MaterialTheme.padding.small
                                            ),
                                        chat = chat,
                                        myUserId = state.myUser.id,
                                        imageLoader = imageLoader
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun ChatsScreenPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = false) {
        ChatsScreen(
            onNavigateToRoute = {},
            onNavigateToGroupChat = { },
            onNavigateToDialogChat = { },
            onNavigateToSearchUsers = { },
            onNavigateToNewMessage = { },
            onNavigateToCreateNewGroup = {},
            onNavigateToSettings = { },
            onShowSnackbar = {},
            state = ChatsState().copy(
                myUser = MyUser(
                    0,
                    "itamiomw@gmail.com",
                    "Itami",
                    null,
                    null,
                    null,
                    0,
                    0,
                    MessagesPermission.ANYONE
                )
            ),
            onEvent = { },
            uiEvent = flow { },
            imageLoader = ImageLoader(LocalContext.current)
        )
    }
}