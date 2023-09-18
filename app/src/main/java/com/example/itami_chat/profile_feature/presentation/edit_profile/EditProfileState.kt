package com.example.itami_chat.profile_feature.presentation.edit_profile

import com.example.itami_chat.core.domain.model.MyUser

data class EditProfileState(
    val myUser: MyUser? = null,

    val isLoading: Boolean = false,
)
