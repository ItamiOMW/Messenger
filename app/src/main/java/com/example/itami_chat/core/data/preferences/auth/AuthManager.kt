package com.example.itami_chat.core.data.preferences.auth

interface AuthManager {

    val token: String?

    fun setToken(token: String?)

}