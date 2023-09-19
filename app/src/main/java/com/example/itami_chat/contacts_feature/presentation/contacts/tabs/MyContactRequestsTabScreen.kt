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
import com.example.itami_chat.contacts_feature.presentation.components.MyContactRequestComponent
import com.example.itami_chat.contacts_feature.presentation.contacts.ContactsEvent
import com.example.itami_chat.core.domain.model.ContactRequest
import com.example.itami_chat.core.presentation.components.pull_refresh.PullRefreshIndicator
import com.example.itami_chat.core.presentation.components.pull_refresh.pullRefresh
import com.example.itami_chat.core.presentation.components.pull_refresh.rememberPullRefreshState
import com.example.itami_chat.core.presentation.ui.theme.padding

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyContactRequestsTabScreen(
    imageLoader: ImageLoader,
    myContactRequests: () -> List<ContactRequest>,
    isLoadingContactRequests: () -> Boolean,
    isLoading: () -> Boolean,
    onEvent: (event: ContactsEvent) -> Unit,
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoadingContactRequests(),
        onRefresh = {
            if (!isLoadingContactRequests()) {
                onEvent(ContactsEvent.OnRefreshContactRequests)
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            items(myContactRequests(), key = { it.id }) { contactRequest ->
                MyContactRequestComponent(
                    contactRequest = contactRequest,
                    imageLoader = imageLoader,
                    onCancel = {
                        if (!isLoading()) {
                            onEvent(ContactsEvent.OnCancelContactRequest(contactRequest.id))
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
            refreshing = { isLoadingContactRequests() },
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}