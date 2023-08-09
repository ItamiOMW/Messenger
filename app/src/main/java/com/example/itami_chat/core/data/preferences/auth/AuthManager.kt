package com.example.itami_chat.core.data.preferences.auth

interface AuthManager {

    val authToken: String?

    fun setToken(token: String?)

}