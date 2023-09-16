package com.example.itami_chat.chat_feature.domain.model

sealed class ChatEvent {

    data class MessageSent(val message: Message) : ChatEvent()

    data class MessageUpdated(val message: Message) : ChatEvent()

    data class MessageDeleted(val message: Message) : ChatEvent()

    data class ChatCreated(val chat: Chat) : ChatEvent()

    data class ChatUpdated(val chat: Chat) : ChatEvent()

    data class ChatDeleted(val chat: Chat) : ChatEvent()

    data class LeftChat(val userLeftId: Int): ChatEvent()

    data class DeleteChatParticipant(val participantId: Int): ChatEvent()

    data class AddChatParticipants(val participants: List<ChatParticipant>): ChatEvent()
}
