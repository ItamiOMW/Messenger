package com.example.itami_chat.chat_feature.presentation.screens.edit_chat

import android.net.Uri
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant


sealed class EditChatEvent {

    data class OnChatNameValueChange(val newValue: String) : EditChatEvent()

    data class OnChatPictureUriChange(val uri: Uri) : EditChatEvent()

    data object OnEditChat : EditChatEvent()

    data class OnDeleteParticipant(val participant: ChatParticipant) : EditChatEvent()

    data class OnAssignAdminRole(val participant: ChatParticipant) : EditChatEvent()

    data class OnRemoveAdminRole(val participant: ChatParticipant) : EditChatEvent()

    data class OnShowEditParticipantDialog(val participant: ChatParticipant) : EditChatEvent()

    data object OnHideEditParticipantDialog : EditChatEvent()

}
