package com.example.itami_chat.profile_feature.presentation.search_users

sealed class SearchUsersEvent {

    data class OnSearchInputChange(val newValue: String): SearchUsersEvent()

}
