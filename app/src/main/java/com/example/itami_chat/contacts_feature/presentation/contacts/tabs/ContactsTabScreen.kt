package com.example.itami_chat.contacts_feature.presentation.contacts.tabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.ImageLoader
import com.example.itami_chat.contacts_feature.presentation.components.ContactComponent
import com.example.itami_chat.contacts_feature.presentation.contacts.ContactsEvent
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.presentation.components.pull_refresh.PullRefreshIndicator
import com.example.itami_chat.core.presentation.components.pull_refresh.pullRefresh
import com.example.itami_chat.core.presentation.components.pull_refresh.rememberPullRefreshState
import com.example.itami_chat.core.presentation.ui.theme.padding

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactsTabScreen(
    imageLoader: ImageLoader,
    contacts: () -> List<SimpleUser>,
    isLoadingContacts: () -> Boolean,
    isLoading: () -> Boolean,
    onEvent: (event: ContactsEvent) -> Unit,
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoadingContacts(),
        onRefresh = {
            if (!isLoadingContacts()) {
                onEvent(ContactsEvent.OnRefreshContacts)
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            items(contacts(), key = { it.id }) { user ->
                ContactComponent(
                    simpleUser = user,
                    imageLoader = imageLoader,
                    onDeleteContact = {
                        if (!isLoading()) {
                            onEvent(ContactsEvent.OnDeleteContact(user.id))
                        }
                    },
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
        PullRefreshIndicator(
            refreshing = { isLoadingContacts() },
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}