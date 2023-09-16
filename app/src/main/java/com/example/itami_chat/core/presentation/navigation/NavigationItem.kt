package com.example.itami_chat.core.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.itami_chat.R

sealed class NavigationItem(
    @StringRes val titleResId: Int,
    @StringRes val descResId: Int,
    @DrawableRes val iconRedId: Int,
    val route: String,
) {

    data object SettingsFeature: NavigationItem(
        titleResId = R.string.title_settings,
        descResId = R.string.title_settings,
        iconRedId = R.drawable.ic_settings,
        route = Graph.SETTINGS_GRAPH,
    )

    data object ContactsFeature: NavigationItem(
        titleResId = R.string.title_contacts,
        descResId = R.string.title_contacts,
        iconRedId = R.drawable.ic_person,
        route = Graph.CONTACTS_GRAPH,
    )

    data object CreateMessageScreen: NavigationItem(
        titleResId = R.string.title_new_message,
        descResId = R.string.title_new_message,
        iconRedId = R.drawable.ic_write_message,
        route = Screen.NewMessage.fullRoute,
    )

    data object CreateGroupScreen: NavigationItem(
        titleResId = R.string.title_new_group,
        descResId = R.string.title_new_group,
        iconRedId = R.drawable.ic_new_group,
        route = Screen.NewGroupParticipants.fullRoute,
    )

}
