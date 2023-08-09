package com.example.itami_chat.authentication_feature.data.repository

import android.app.Application
import com.example.itami_chat.R
import com.example.itami_chat.authentication_feature.data.remote.AuthApiService
import com.example.itami_chat.authentication_feature.data.remote.dto.request.LoginRequest
import com.example.itami_chat.authentication_feature.data.remote.dto.request.PasswordResetRequest
import com.example.itami_chat.authentication_feature.data.remote.dto.request.RegisterRequest
import com.example.itami_chat.authentication_feature.data.remote.dto.request.SendPasswordResetCodeRequest
import com.example.itami_chat.authentication_feature.data.remote.dto.request.SendVerificationCodeRequest
import com.example.itami_chat.authentication_feature.data.remote.dto.request.VerifyEmailRequest
import com.example.itami_chat.authentication_feature.domain.repository.AuthRepository
import com.example.itami_chat.core.data.mapper.toDataStoreUser
import com.example.itami_chat.core.data.preferences.auth.AuthManager
import com.example.itami_chat.core.data.preferences.user.UserManager
import com.example.itami_chat.core.data.remote.dto.response.FailedApiResponse
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.exception.UnauthorizedException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.utils.NetworkUtil
import com.google.gson.Gson
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authManager: AuthManager,
    private val userManager: UserManager,
    private val authApiService: AuthApiService,
    private val application: Application,
    private val gson: Gson,
) : AuthRepository {

    override suspend fun login(email: String, password: String): AppResponse<Unit> {
        return try {
            val isNetworkAvailable = NetworkUtil.isNetworkAvailable(application)

            if (!isNetworkAvailable) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val response = authApiService.login(LoginRequest(email, password))

            if (response.isSuccessful) {
                val token = response.body()?.data?.token
                    ?: return AppResponse.failed(UnauthorizedException, "Backend eblan.")

                val user = response.body()?.data?.user
                    ?: return AppResponse.failed(UnauthorizedException, "Backend eblan.")

                authManager.setToken(token)
                userManager.setUser(user.toDataStoreUser())
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


    override suspend fun register(email: String, password: String): AppResponse<Unit> {
        return try {
            val isNetworkAvailable = NetworkUtil.isNetworkAvailable(application)

            if (!isNetworkAvailable) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val response = authApiService.register(RegisterRequest(email, password))

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
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }


    override suspend fun authenticate(): AppResponse<Unit> {
        return try {
            val isNetworkAvailable = NetworkUtil.isNetworkAvailable(application)

            if (!isNetworkAvailable) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.authToken ?: return AppResponse.failed(UnauthorizedException)

            val response = authApiService.authenticate("Bearer $token")

            if (response.isSuccessful) {
                val user = response.body()?.data ?: return AppResponse.failed(UnauthorizedException)
                userManager.setUser(user.toDataStoreUser())
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
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }


    override suspend fun verifyEmail(email: String, code: Int): AppResponse<Unit> {
        return try {
            val isNetworkAvailable = NetworkUtil.isNetworkAvailable(application)

            if (!isNetworkAvailable) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val response = authApiService.verifyEmail(VerifyEmailRequest(email, code))

            if (response.isSuccessful) {
                val token = response.body()?.data?.token
                    ?: return AppResponse.failed(UnauthorizedException, "Backend eblan.")

                val user = response.body()?.data?.user
                    ?: return AppResponse.failed(UnauthorizedException, "Backend eblan.")

                authManager.setToken(token)
                userManager.setUser(user.toDataStoreUser())
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
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }


    override suspend fun resendEmailVerificationCode(email: String): AppResponse<Unit> {
        return try {
            val isNetworkAvailable = NetworkUtil.isNetworkAvailable(application)

            if (!isNetworkAvailable) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val response =
                authApiService.sendEmailVerificationCode(SendVerificationCodeRequest(email))

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
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }

    }

    override suspend fun sendPasswordResetCode(email: String): AppResponse<Unit> {
        return try {
            val isNetworkAvailable = NetworkUtil.isNetworkAvailable(application)

            if (!isNetworkAvailable) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val response = authApiService.sendPasswordResetCode(SendPasswordResetCodeRequest(email))

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
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }

    }

    override suspend fun resetPassword(
        email: String,
        code: Int,
        newPassword: String,
    ): AppResponse<Unit> {
        return try {
            val isNetworkAvailable = NetworkUtil.isNetworkAvailable(application)

            if (!isNetworkAvailable) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val response = authApiService.resetPassword(
                PasswordResetRequest(email, code, newPassword)
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
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }

}
