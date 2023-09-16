package com.example.itami_chat.chat_feature.presentation.screens.dialog_chat

import com.example.itami_chat.chat_feature.domain.model.Message

sealed class DialogChatEvent {

    data class OnMessageInputValueChange(val newValue: String) : DialogChatEvent()

    data class OnEditMessageInputValueChange(val newValue: String) : DialogChatEvent()

    data object OnSendMessage : DialogChatEvent()

    data class OnReadMessage(val messageId: Int) : DialogChatEvent()

    data object OnDeleteChat : DialogChatEvent()

    data object OnLoadNextMessages : DialogChatEvent()

    data class OnShowDeleteMessageDialog(val message: Message) : DialogChatEvent()

    data object OnHideDeleteMessageDialog : DialogChatEvent()

    data class OnDeleteMessage(val message: Message) : DialogChatEvent()

    data class OnShowEditMessageTextField(val message: Message) : DialogChatEvent()

    data object OnHideEditMessageTextField : DialogChatEvent()

    data class OnEditMessage(val message: Message) : DialogChatEvent()

    data object OnShowDeleteChatDialog : DialogChatEvent()

    data object OnHideDeleteChatDialog : DialogChatEvent()

}
