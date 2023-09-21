package com.example.itami_chat.settings_feature.data.repository

import android.app.Application
import com.example.itami_chat.R
import com.example.itami_chat.core.data.mapper.toSimpleUser
import com.example.itami_chat.core.data.preferences.auth.AuthManager
import com.example.itami_chat.core.data.remote.dto.response.FailedApiResponse
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.exception.UnauthorizedException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.MessagesPermission
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.domain.preferences.UserManager
import com.example.itami_chat.core.utils.NetworkUtil
import com.example.itami_chat.settings_feature.data.remote.UserSettingsApiService
import com.example.itami_chat.settings_feature.data.remote.dto.ChangeMessagesPermission
import com.example.itami_chat.settings_feature.data.remote.dto.ChangePasswordRequest
import com.example.itami_chat.settings_feature.data.remote.dto.VerifyPasswordChangeCodeRequest
import com.example.itami_chat.settings_feature.domain.repository.UserSettingsRepository
import com.google.gson.Gson
import java.io.IOException
import javax.inject.Inject


class UserSettingsRepositoryImpl @Inject constructor(
    private val userSettingsApiService: UserSettingsApiService,
    private val authManager: AuthManager,
    private val userManager: UserManager,
    private val application: Application,
    private val gson: Gson,
) : UserSettingsRepository {

    override suspend fun sendPasswordChangeCode(): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)

            val response = userSettingsApiService.sendPasswordChangeCode("Bearer $token")

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

    override suspend fun verifyPasswordChangeCode(code: Int): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)

            val response = userSettingsApiService.verifyPasswordChangeCode(
                "Bearer $token",
                VerifyPasswordChangeCodeRequest(code)
            )

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

    override suspend fun changePassword(newPassword: String): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)

            val response = userSettingsApiService.changePassword(
                "Bearer $token",
                ChangePasswordRequest(newPassword)
            )

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

    override suspend fun changeMessagesPermission(permission: MessagesPermission): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)

            val response = userSettingsApiService.changeMessagesPermission(
                token = "Bearer $token",
                changeMessagesPermission = ChangeMessagesPermission(permission)
            )

            if (response.isSuccessful) {
                userManager.changeMessagesPermission(permission)
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

    override suspend fun deleteAccount(): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = userSettingsApiService.deleteAccount("Bearer $token")

            if (response.isSuccessful) {
                authManager.setToken(null)
                userManager.setUser(null)
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

    override suspend fun getBlockedUsers(): AppResponse<List<SimpleUser>> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = userSettingsApiService.getBlockedUsers("Bearer $token")

            if (response.isSuccessful) {
                val blockedUsers = response.body()?.data?.map { it.toSimpleUser() } ?: emptyList()
                return AppResponse.success(blockedUsers)
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