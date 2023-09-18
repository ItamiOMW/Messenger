package com.example.itami_chat.profile_feature.presentation.user_profile

sealed class UserProfileUiEvent {

    data object OnNavigateBack: UserProfileUiEvent()

    data class OnShowSnackbar(val message: String): UserProfileUiEvent()

}
