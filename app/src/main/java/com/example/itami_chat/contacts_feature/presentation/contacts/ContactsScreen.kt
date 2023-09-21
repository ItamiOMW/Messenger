package com.example.itami_chat.contacts_feature.presentation.contacts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.example.itami_chat.R
import com.example.itami_chat.contacts_feature.presentation.contacts.tabs.ContactRequestsTabScreen
import com.example.itami_chat.contacts_feature.presentation.contacts.tabs.ContactsTabScreen
import com.example.itami_chat.contacts_feature.presentation.contacts.tabs.MyContactRequestsTabScreen
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.util.TabItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactsScreen(
    onShowSnackbar: (message: String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToSearchUsers: () -> Unit,
    onNavigateToUserProfile: (userId: Int) -> Unit,
    imageLoader: ImageLoader,
    state: ContactsState,
    theme: Theme,
    isDarkMode: Boolean,
    searchContactsQuery: String,
    uiEvent: Flow<ContactsUiEvent>,
    onEvent: (event: ContactsEvent) -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is ContactsUiEvent.OnShowSnackbar -> {
                    onShowSnackbar(event.message)
                }
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val tabItems = rememberSaveable(state) {
        listOf(
            TabItem(
                titleId = R.string.title_contacts,
                content = {
                    ContactsTabScreen(
                        imageLoader = imageLoader,
                        contacts = { state.contacts },
                        isLoadingContacts = { state.isLoadingContacts },
                        isLoading = { state.isLoading },
                        onEvent = onEvent,
                        onContactClicked = onNavigateToUserProfile
                    )
                }
            ),
            TabItem(
                titleId = R.string.title_contact_requests,
                content = {
                    ContactRequestsTabScreen(
                        imageLoader = imageLoader,
                        contactRequests = { state.contactRequests },
                        isLoadingContactRequests = { state.isLoadingContactRequests },
                        isLoading = { state.isLoading },
                        onEvent = onEvent
                    )
                }
            ),
            TabItem(
                titleId = R.string.title_my_contact_requests,
                content = {
                    MyContactRequestsTabScreen(
                        imageLoader = imageLoader,
                        myContactRequests = { state.myContactRequests },
                        isLoadingContactRequests = { state.isLoadingContactRequests },
                        isLoading = { state.isLoading },
                        onEvent = onEvent
                    )
                }
            ),
        )
    }

    val pagerState = rememberPagerState {
        tabItems.size
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        if (state.isSearchFieldVisible && pagerState.currentPage == 0) {
                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = searchContactsQuery,
                                onValueChange = { newValue ->
                                    onEvent(ContactsEvent.OnSearchContactsQueryChange(newValue))
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
                                if (state.isSearchFieldVisible && pagerState.currentPage == 0) {
                                    onEvent(ContactsEvent.OnChangeSearchFieldVisibility)
                                } else {
                                    onNavigateBack()
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_left),
                                contentDescription = stringResource(R.string.desc_navigate_back),
                                modifier = Modifier
                                    .size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        if (!state.isSearchFieldVisible && pagerState.currentPage == 0) {
                            IconButton(
                                onClick = { onEvent(ContactsEvent.OnChangeSearchFieldVisibility) }
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
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = MaterialTheme.colorScheme.surface,
                    indicator = { tabPositions ->
                        if (pagerState.currentPage < tabPositions.size) {
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                color = if (theme == Theme.Blue && !isDarkMode) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                ) {
                    tabItems.forEachIndexed { index, tabItem ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = {
                                Text(
                                    text = stringResource(id = tabItem.titleId),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (theme == Theme.Blue && !isDarkMode) MaterialTheme.colorScheme.onSurface
                                    else MaterialTheme.colorScheme.primary
                                )
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            Box(
                modifier = Modifier.padding(bottom = MaterialTheme.padding.medium)
            ) {
                FloatingActionButton(
                    onClick = {
                        onNavigateToSearchUsers()
                    },
                    modifier = Modifier
                        .size(60.dp),
                    shape = RoundedCornerShape(30.dp),
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_contact),
                        contentDescription = stringResource(R.string.desc_person_add_icon),
                        modifier = Modifier
                            .size(28.dp)
                            .padding(start = 3.dp)
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
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
            ) { page ->
                tabItems[page].content()
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