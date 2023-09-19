package com.example.itami_chat.core.data.repository

import android.app.Application
import com.example.itami_chat.R
import com.example.itami_chat.core.data.mapper.toContactRequest
import com.example.itami_chat.core.data.mapper.toSimpleUser
import com.example.itami_chat.core.data.preferences.auth.AuthManager
import com.example.itami_chat.core.data.remote.dto.response.FailedApiResponse
import com.example.itami_chat.core.data.remote.service.ContactsApiService
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.exception.ServerErrorException
import com.example.itami_chat.core.domain.exception.UnauthorizedException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.ContactRequest
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
                val contacts = response.body()?.data?.map {
                    it.toSimpleUser()
                } ?: return AppResponse.failed(ServerErrorException)
                return AppResponse.success(contacts)
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

    override suspend fun getContactRequests(): AppResponse<List<ContactRequest>> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = contactsApiService.getContactRequests("Bearer $token")

            if (response.isSuccessful) {
                val contactRequests = response.body()?.data?.map { it.toContactRequest() }
                    ?: return AppResponse.failed(ServerErrorException)
                return AppResponse.success(contactRequests)
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

    override suspend fun deleteContact(userId: Int): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = contactsApiService.deleteContact("Bearer $token", userId)

            if (response.isSuccessful) {
                return AppResponse.success(Unit)
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

    override suspend fun sendContactRequest(userId: Int): AppResponse<ContactRequest> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = contactsApiService.sendContactRequest("Bearer $token", userId)

            if (response.isSuccessful) {
                val contactRequest =
                    response.body()?.data?.toContactRequest() ?: return AppResponse.failed(
                        ServerErrorException
                    )
                return AppResponse.success(contactRequest)
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

    override suspend fun acceptContactRequest(requestId: Int): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = contactsApiService.acceptContactRequest("Bearer $token", requestId)

            if (response.isSuccessful) {
                return AppResponse.success(Unit)
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

    override suspend fun declineContactRequest(requestId: Int): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = contactsApiService.declineContactRequest("Bearer $token", requestId)

            if (response.isSuccessful) {
                return AppResponse.success(Unit)
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

    override suspend fun cancelContactRequest(requestId: Int): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = contactsApiService.cancelContactRequest("Bearer $token", requestId)

            if (response.isSuccessful) {
                return AppResponse.success(Unit)
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