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

    data object VerifyEmail: Screen(VERIFY_EMAIL_SCREEN_ROUTE, EMAIL_ARG) {
        fun getRouteWithArgs(email: String) = route.appendParams(EMAIL_ARG to email)
    }

    data object CreateProfile: Screen(CREATE_PROFILE_SCREEN_ROUTE)

    data object ForgotPassword: Screen(FORGOT_PASSWORD_SCREEN_ROUTE)

    data object PasswordReset: Screen(PASSWORD_RESET_SCREEN_ROUTE, EMAIL_ARG) {
        fun getRouteWithArgs(email: String) = route.appendParams(EMAIL_ARG to email)
    }



    data object Chats : Screen(CHATS_SCREEN_ROUTE)


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

        const val MESSAGE_ARG = "message_arg"
        const val EMAIL_ARG = "email_arg"

    }

}
