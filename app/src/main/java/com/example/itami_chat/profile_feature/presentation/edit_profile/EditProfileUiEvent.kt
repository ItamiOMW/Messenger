package com.example.itami_chat.profile_feature.presentation.edit_profile

sealed class EditProfileUiEvent {

    data object OnNavigateBack: EditProfileUiEvent()

    data class OnShowSnackbar(val message: String): EditProfileUiEvent()

}
