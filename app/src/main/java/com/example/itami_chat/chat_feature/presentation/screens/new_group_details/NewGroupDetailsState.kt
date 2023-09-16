package com.example.itami_chat.chat_feature.presentation.screens.new_group_details

import android.net.Uri
import com.example.itami_chat.core.domain.model.SimpleUser

data class NewGroupDetailsState(
    val chatPictureUri: Uri? = null,
    val participants: List<SimpleUser> = emptyList(),
    val isCreatingChat: Boolean = false,
    val isLoadingParticipants: Boolean = false,
    val participantsErrorMessage: String? = null
)
