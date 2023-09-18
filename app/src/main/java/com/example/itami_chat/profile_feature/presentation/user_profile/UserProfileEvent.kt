package com.example.itami_chat.profile_feature.presentation.user_profile

sealed class UserProfileEvent {

    data object OnDeleteContact: UserProfileEvent()

    data object OnBlockUser : UserProfileEvent()

    data object OnUnblockUser : UserProfileEvent()

    data object OnSendContactRequest : UserProfileEvent()

    data object OnCancelContactRequest : UserProfileEvent()

    data object OnAcceptContactRequest : UserProfileEvent()

    data object OnDeclineContactRequest : UserProfileEvent()

}
