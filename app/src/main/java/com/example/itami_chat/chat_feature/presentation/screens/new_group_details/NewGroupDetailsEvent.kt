package com.example.itami_chat.chat_feature.presentation.screens.new_group_details

import android.net.Uri

sealed class NewGroupDetailsEvent {

    data class OnChatNameValueChange(val newValue: String) : NewGroupDetailsEvent()

    data class OnPictureUriChange(val uri: Uri): NewGroupDetailsEvent()

    data object OnCreateChat: NewGroupDetailsEvent()

    data object OnReloadParticipants : NewGroupDetailsEvent()

}