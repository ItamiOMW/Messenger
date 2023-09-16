package com.example.itami_chat.core.data.repository

import android.app.Application
import com.example.itami_chat.R
import com.example.itami_chat.core.data.mapper.toSimpleUser
import com.example.itami_chat.core.data.preferences.auth.AuthManager
import com.example.itami_chat.core.data.remote.dto.response.FailedApiResponse
import com.example.itami_chat.core.data.remote.service.ContactsApiService
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.exception.ServerErrorException
import com.example.itami_chat.core.domain.exception.UnauthorizedException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.domain.repository.ContactsRepository
import com.example.itami_chat.core.utils.NetworkUtil
import com.google.gson.Gson
import java.io.IOException
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    private val contactsApiService: ContactsApiService,
    private val authManager: AuthManager,
    private val application: Application,
    private val gson: Gson,
) : ContactsRepository {

    override suspend fun getContacts(): AppResponse<List<SimpleUser>> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = contactsApiService.getContacts("Bearer $token")

            if (response.isSuccessful) {
                val contacts = response.body()?.data ?: return AppResponse.failed(ServerErrorException)
                return AppResponse.success(contacts.map { it.toSimpleUser() })
            }

            val failedApiResponse = gson.fromJson(
                response.errorBody()?.charStream(), FailedApiResponse::class.java
            )
            val exception = failedApiResponse.toException()

            return AppResponse.failed(
                exception = exception,
                message = failedApiResponse?.message
            )
        } catch (e: IOException) {
            AppResponse.failed(e, application.getString(R.string.error_couldnt_reach_server))
        } catch (e: Exception) {
            e.printStackTrace()
            AppResponse.failed(e, application.getString(R.string.error_unknown))

        }
    }

}