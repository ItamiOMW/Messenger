package com.example.itami_chat.chat_feature.presentation.screens.group_chat

import com.example.itami_chat.chat_feature.domain.model.Message

sealed class GroupChatEvent {

    data class OnMessageInputValueChange(val newValue: String) : GroupChatEvent()

    data class OnEditMessageInputValueChange(val newValue: String) : GroupChatEvent()

    data object OnSendMessage : GroupChatEvent()

    data class OnReadMessage(val messageId: Int): GroupChatEvent()

    data object OnLeaveChat : GroupChatEvent()

    data object OnLoadNextMessages : GroupChatEvent()

    data class OnShowDeleteMessageDialog(val message: Message) : GroupChatEvent()

    data object OnHideDeleteMessageDialog: GroupChatEvent()

    data class OnDeleteMessage(val message: Message) : GroupChatEvent()

    data class OnShowEditMessageTextField(val message: Message) : GroupChatEvent()

    data object OnHideEditMessageTextField: GroupChatEvent()

    data class OnEditMessage(val message: Message) : GroupChatEvent()

    data object OnShowLeaveChatDialog: GroupChatEvent()

    data object OnHideLeaveChatDialog: GroupChatEvent()

}
