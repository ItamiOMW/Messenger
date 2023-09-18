package com.example.itami_chat.profile_feature.presentation.edit_profile

import android.net.Uri

sealed class EditProfileEvent {

    data class OnPictureUriChange(val newUri: Uri): EditProfileEvent()

    data class OnFullNameInputChange(val newValue: String): EditProfileEvent()

    data class OnUsernameInputChange(val newValue: String): EditProfileEvent()

    data class OnBioInputChange(val newValue: String): EditProfileEvent()

    data object OnEditProfile: EditProfileEvent()

}
