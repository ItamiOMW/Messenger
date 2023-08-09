package com.example.itami_chat.authentication_feature.presentation.create_profile

import android.net.Uri

sealed class CreateProfileEvent {

    data class OnFullNameInputChange(val newValue: String): CreateProfileEvent()

    data class OnBioInputChange(val newValue: String): CreateProfileEvent()

    data class OnImageUriChange(val newUri: Uri): CreateProfileEvent()

    data object OnCreateProfile: CreateProfileEvent()

}
