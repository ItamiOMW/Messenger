package com.example.itami_chat.core.data.preferences.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class EncryptedAuthManager @Inject constructor(
    @ApplicationContext context: Context,
) : AuthManager {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        TOKEN_STORAGE_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    override val authToken: String?
        get() = sharedPreferences.getString(AUTH_TOKEN_KEY, null)


    override fun setToken(token: String?) {
        sharedPreferences.edit()
            .putString(AUTH_TOKEN_KEY, token)
            .apply()
    }


    companion object {

        private const val TOKEN_STORAGE_FILE_NAME = "token_storage"

        private const val AUTH_TOKEN_KEY = "auth_token_key"
    }


}