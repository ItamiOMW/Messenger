package com.example.itami_chat.core.data.remote.dto.response

import com.example.itami_chat.core.data.remote.ExceptionCode
import com.example.itami_chat.core.domain.exception.ConflictException
import com.example.itami_chat.core.domain.exception.ForbiddenException
import com.example.itami_chat.core.domain.exception.InvalidEmailException
import com.example.itami_chat.core.domain.exception.InvalidPasswordException
import com.example.itami_chat.core.domain.exception.InvalidVerificationCodeException
import com.example.itami_chat.core.domain.exception.NotFoundException
import com.example.itami_chat.core.domain.exception.ServerErrorException
import com.example.itami_chat.core.domain.exception.UnauthorizedException
import com.example.itami_chat.core.domain.exception.UserAlreadyExistsException
import com.example.itami_chat.core.domain.exception.UserDoesNotExistException
import com.example.itami_chat.core.domain.exception.UserNotActiveException
import kotlinx.serialization.Serializable


@Serializable
data class FailedApiResponse(
    val message: String? = null,
    val exceptionCode: String? = null,
) {
    fun toException(): Exception {
        return when (this.exceptionCode) {
            ExceptionCode.BadRequest.code -> Exception()
            ExceptionCode.InvalidVerificationCode.code -> InvalidVerificationCodeException
            ExceptionCode.InvalidPasswordResetCode.code -> InvalidVerificationCodeException
            ExceptionCode.InvalidEmail.code -> InvalidEmailException
            ExceptionCode.InvalidPassword.code -> InvalidPasswordException

            ExceptionCode.Unauthorized.code -> UnauthorizedException

            ExceptionCode.Forbidden.code -> ForbiddenException
            ExceptionCode.UserNotActive.code -> UserNotActiveException
            ExceptionCode.PasswordResetNotAllowed.code -> ForbiddenException

            ExceptionCode.NotFound.code -> NotFoundException
            ExceptionCode.UserDoesNotExist.code -> UserDoesNotExistException

            ExceptionCode.Conflict.code -> ConflictException
            ExceptionCode.UserAlreadyExists.code -> UserAlreadyExistsException

            ExceptionCode.ServerError.code -> ServerErrorException
            else -> {
                Exception()
            }
        }
    }
}


