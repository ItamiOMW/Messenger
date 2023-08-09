package com.example.itami_chat.chat_feature.presentation.chats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow


//Temporary implementation to test AuthFeature
@Composable
fun ChatsScreen(
    onLogout: () -> Unit,
    onEvent: (ChatsEvent) -> Unit,
    uiEvent: Flow<ChatsUiEvent>,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                ChatsUiEvent.OnLogoutSuccessful -> onLogout()
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .clickable {
                onEvent(ChatsEvent.OnLogout)
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Temporary chats screen impl. Click to logout")
    }

}