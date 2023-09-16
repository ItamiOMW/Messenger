package com.example.itami_chat.chat_feature.presentation.screens.chat_profile

import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant


sealed class ChatProfileEvent {

    data class OnUpdateChat(val chat: Chat): ChatProfileEvent()

    data class OnAddChatParticipants(val participants: List<ChatParticipant>): ChatProfileEvent()

    data object OnLeaveChat: ChatProfileEvent()

    data object OnShowLeaveChatDialog: ChatProfileEvent()

    data object OnHideLeaveChatDialog: ChatProfileEvent()

}
