package com.example.itami_chat.authentication_feature.data.remote.dto.response

import com.google.gson.annotations.SerializedName


data class AuthResponse(
    @SerializedName("token") val token: String?,
    @SerializedName("user") val user: MyUserResponse?,
)
