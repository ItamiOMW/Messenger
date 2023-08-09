package com.example.itami_chat.authentication_feature.presentation.onboarding

import com.example.itami_chat.core.domain.model.Theme

data class OnboardingState(
    val isDarkTheme: Boolean = false,
    val theme: Theme = Theme.Default
)
