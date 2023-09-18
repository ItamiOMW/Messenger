package com.example.itami_chat.profile_feature.presentation.user_profile

import com.example.itami_chat.core.domain.model.UserProfile


data class UserProfileState(
    val userProfile: UserProfile? = null,

    val isLoading: Boolean = false,
)
