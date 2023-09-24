package com.example.itami_chat.core.data.repository

import android.app.Application
import androidx.core.net.toUri
import com.example.itami_chat.R
import com.example.itami_chat.core.data.mapper.toMyUser
import com.example.itami_chat.core.data.mapper.toSimpleUser
import com.example.itami_chat.core.data.mapper.toUserProfile
import com.example.itami_chat.core.data.preferences.auth.AuthManager
import com.example.itami_chat.core.data.remote.dto.response.FailedApiResponse
import com.example.itami_chat.core.data.remote.service.UserApiService
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.exception.ServerErrorException
import com.example.itami_chat.core.domain.exception.UnauthorizedException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.domain.model.UpdateProfileData
import com.example.itami_chat.core.domain.model.UserProfile
import com.example.itami_chat.core.domain.preferences.UserManager
import com.example.itami_chat.core.domain.repository.UserRepository
import com.example.itami_chat.core.utils.NetworkUtil
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val authManager: AuthManager,
    private val userManager: UserManager,
    private val application: Application,
    private val userApiService: UserApiService,
    private val gson: Gson,
) : UserRepository {

    override suspend fun updateProfile(
        updateProfileData: UpdateProfileData,
        profilePictureUri: String?,
    ): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val profilePictureFile = profilePictureUri?.toUri()?.path?.let { File(it) }
//            val profilePictureFile = profilePictureUri?.toUri()?.toFile()

            val response = userApiService.updateProfile(
                token = "Bearer $token",
                updateProfileData = MultipartBody.Part
                    .createFormData(
                        name = "update_profile_data",
                        value = gson.toJson(updateProfileData)
                    ),
                profilePicture = profilePictureFile?.let { file ->
                    MultipartBody.Part
                        .createFormData(
                            name = "profile_picture",
                            filename = file.name,
                            file.asRequestBody()
                        )
                }
            )

            if (response.isSuccessful) {
                val user = response.body()?.data ?: return AppResponse.failed(ServerErrorException)
                userManager.setUser(user.toMyUser())
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

    override suspend fun getUserProfile(id: Int): AppResponse<UserProfile> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)

            val response = userApiService.getProfile(token = "Bearer $token", id = id)

            if (response.isSuccessful) {
                val profile = response.body()?.data?.toUserProfile() ?: return AppResponse.failed(
                    ServerErrorException
                )
                return AppResponse.success(profile)
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

    override suspend fun getUsersByIds(userIds: List<Int>): AppResponse<List<SimpleUser>> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = userApiService.getUsersByIds(token = "Bearer $token", userIds)
            if (response.isSuccessful) {
                val users = response.body()?.data?.map { it.toSimpleUser() } ?: emptyList()
                return AppResponse.success(users)
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

    override suspend fun searchForUsers(query: String): AppResponse<List<SimpleUser>> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = userApiService.searchForUsers(token = "Bearer $token", query)
            if (response.isSuccessful) {
                val users = response.body()?.data?.map { it.toSimpleUser() } ?: emptyList()
                return AppResponse.success(users)
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

    override suspend fun blockUser(id: Int): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = userApiService.blockUser(token = "Bearer $token", id)
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

    override suspend fun unblockUser(id: Int): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = userApiService.unblockUser(token = "Bearer $token", id)
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

    override suspend fun logout(): AppResponse<Unit> {
        userManager.setUser(null)
        authManager.setToken(null)
        return AppResponse.success(Unit)
    }

}