package com.example.itami_chat.core.presentation.navigation

sealed class Screen(protected val route: String, vararg params: String) {

    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    data object Splash : Screen(SPLASH_SCREEN_ROUTE)

    data object Onboarding : Screen(ONBOARDING_SCREEN_ROUTE)

    data object Login : Screen(LOGIN_SCREEN_ROUTE)

    data object Register : Screen(REGISTER_SCREEN_ROUTE)

    data object VerifyEmail : Screen(VERIFY_EMAIL_SCREEN_ROUTE, EMAIL_ARG) {
        fun getRouteWithArgs(email: String) = route.appendParams(EMAIL_ARG to email)
    }

    data object CreateProfile : Screen(CREATE_PROFILE_SCREEN_ROUTE)

    data object ForgotPassword : Screen(FORGOT_PASSWORD_SCREEN_ROUTE)

    data object PasswordReset : Screen(PASSWORD_RESET_SCREEN_ROUTE, EMAIL_ARG) {
        fun getRouteWithArgs(email: String) = route.appendParams(EMAIL_ARG to email)
    }

    data object Chats : Screen(CHATS_SCREEN_ROUTE)

    data object DialogChat : Screen(DIALOG_CHAT_SCREEN_ROUTE, USER_ID_ARG) {
        fun getRouteWithArgs(userId: Int): String {
            return route.appendParams(USER_ID_ARG to userId)
        }
    }

    data object GroupChat : Screen(GROUP_CHAT_SCREEN_ROUTE, CHAT_ID_ARG) {
        fun getRouteWithArgs(chatId: Int): String {
            return route.appendParams(CHAT_ID_ARG to chatId)
        }
    }

    data object NewMessage : Screen(NEW_MESSAGE_SCREEN_ROUTE)

    data object NewGroupParticipants : Screen(NEW_GROUP_PARTICIPANTS_SCREEN_ROUTE)

    data object NewGroupDetails : Screen(NEW_GROUP_DETAILS_SCREEN_ROUTE, USER_IDS_ARG) {
        fun getRouteWithArgs(userIds: List<Int>): String {
            return route.appendParams(USER_IDS_ARG to userIds)
        }
    }

    data object ChatProfile : Screen(CHAT_PROFILE_SCREEN_ROUTE, CHAT_ID_ARG) {
        fun getRouteWithArgs(chatId: Int): String {
            return route.appendParams(CHAT_ID_ARG to chatId)
        }
    }

    data object EditChat : Screen(EDIT_CHAT_SCREEN_ROUTE, CHAT_ID_ARG) {
        fun getRouteWithArgs(chatId: Int): String {
            return route.appendParams(CHAT_ID_ARG to chatId)
        }
    }

    data object AddChatParticipants : Screen(ADD_CHAT_PARTICIPANTS_SCREEN_ROUTE, CHAT_ID_ARG) {
        fun getRouteWithArgs(chatId: Int): String {
            return route.appendParams(CHAT_ID_ARG to chatId)
        }
    }

    data object UserProfile : Screen(USER_PROFILE_ROUTE, USER_ID_ARG) {
        fun getRouteWithArgs(userId: Int): String {
            return route.appendParams(USER_ID_ARG to userId)
        }
    }

    data object EditProfile : Screen(EDIT_PROFILE_ROUTE)

    data object SearchUsers : Screen(SEARCH_USERS_ROUTE)

    data object Contacts : Screen(CONTACTS_SCREEN_ROUTE)

    data object Settings : Screen(SETTINGS_SCREEN_ROUTE)

    data object AccountSetting: Screen(ACCOUNT_SETTING_SCREEN_ROUTE)

    data object VerifyPasswordChange: Screen(VERIFY_PASSWORD_CHANGE_SCREEN_ROUTE)

    data object ChangePassword: Screen(CHANGE_PASSWORD_SCREEN_ROUTE)

    data object PrivacySetting: Screen(PRIVACY_SETTING_SCREEN_ROUTE)

    data object BlockedUsers: Screen(BLOCKED_USERS_SCREEN_ROUTE)

    data object MessagesPermission: Screen(MESSAGES_PERMISSION_SCREEN_ROUTE)

    data object AppearanceSetting: Screen(APPEARANCE_SETTING_SCREEN_ROUTE)


    companion object {

        //Auth feature
        private const val SPLASH_SCREEN_ROUTE = "splash"
        private const val ONBOARDING_SCREEN_ROUTE = "onboarding"
        private const val REGISTER_SCREEN_ROUTE = "register"
        private const val LOGIN_SCREEN_ROUTE = "login"
        private const val VERIFY_EMAIL_SCREEN_ROUTE = "verify_email"
        private const val CREATE_PROFILE_SCREEN_ROUTE = "create_profile"
        private const val FORGOT_PASSWORD_SCREEN_ROUTE = "forgot_password"
        private const val PASSWORD_RESET_SCREEN_ROUTE = "password_reset"

        //Chats feature
        private const val CHATS_SCREEN_ROUTE = "chats"
        private const val DIALOG_CHAT_SCREEN_ROUTE = "dialog_chat"
        private const val GROUP_CHAT_SCREEN_ROUTE = "group_chat"
        private const val NEW_MESSAGE_SCREEN_ROUTE = "new_message"
        private const val NEW_GROUP_PARTICIPANTS_SCREEN_ROUTE = "new_group_participants"
        private const val NEW_GROUP_DETAILS_SCREEN_ROUTE = "new_group_details"
        private const val CHAT_PROFILE_SCREEN_ROUTE = "chat_profile"
        private const val EDIT_CHAT_SCREEN_ROUTE = "edit_profile"
        private const val ADD_CHAT_PARTICIPANTS_SCREEN_ROUTE = "add_chat_participants"

        //Profile feature
        private const val USER_PROFILE_ROUTE = "user_profile"
        private const val EDIT_PROFILE_ROUTE = "edit_profile"
        private const val SEARCH_USERS_ROUTE = "search_users"

        //Settings feature
        private const val SETTINGS_SCREEN_ROUTE = "settings"
        private const val ACCOUNT_SETTING_SCREEN_ROUTE = "account_settings"
        private const val APPEARANCE_SETTING_SCREEN_ROUTE = "appearance_settings"
        private const val VERIFY_PASSWORD_CHANGE_SCREEN_ROUTE = "verify_password_change"
        private const val CHANGE_PASSWORD_SCREEN_ROUTE = "change_password"
        private const val PRIVACY_SETTING_SCREEN_ROUTE = "privacy_setting"
        private const val BLOCKED_USERS_SCREEN_ROUTE = "blocked_users"
        private const val MESSAGES_PERMISSION_SCREEN_ROUTE = "messages_permission"

        //Contacts feature
        private const val CONTACTS_SCREEN_ROUTE = "contacts"

        //Arguments
        const val EMAIL_ARG = "email_arg"
        const val USER_ID_ARG = "user_arg"
        const val CHAT_ID_ARG = "user_arg"
        const val USER_IDS_ARG = "user_ids_arg"

    }

}
