package com.example.itami_chat.profile_feature.presentation.search_users

sealed class SearchUsersUiEvent {

    data class OnShowSnackbar(val message: String): SearchUsersUiEvent()

    data object OnNavigateBack: SearchUsersUiEvent()

}
