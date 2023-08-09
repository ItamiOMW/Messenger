package com.example.itami_chat.core.domain.util

import android.util.Patterns
import com.example.itami_chat.core.domain.exception.EmptyConfirmPasswordFieldException
import com.example.itami_chat.core.domain.exception.EmptyEmailFieldException
import com.example.itami_chat.core.domain.exception.EmptyFullNameFieldException
import com.example.itami_chat.core.domain.exception.EmptyPasswordFieldException
import com.example.itami_chat.core.domain.exception.FullNameTooLongException
import com.example.itami_chat.core.domain.exception.InvalidEmailException
import com.example.itami_chat.core.domain.exception.InvalidVerificationCodeException
import com.example.itami_chat.core.domain.exception.LongBioException
import com.example.itami_chat.core.domain.exception.LongUsernameException
import com.example.itami_chat.core.domain.exception.PasswordsDoNotMatchException
import com.example.itami_chat.core.domain.exception.ShortPasswordException
import com.example.itami_chat.core.domain.exception.ShortUsernameException
import com.example.itami_chat.core.utils.Constants

object ValidationUtil {

    fun validateEmail(email: String): Exception? {
        val trimmedEmail = email.trim()

        if (trimmedEmail.isBlank()) {
            return EmptyEmailFieldException
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            return InvalidEmailException
        }

        return null
    }

    fun validatePassword(password: String): Exception? {
        val trimmedPassword = password.trim()

        if (trimmedPassword.isBlank()) {
            return EmptyPasswordFieldException
        }

        if (trimmedPassword.length < Constants.MIN_PASSWORD_LENGTH) {
            return ShortPasswordException
        }

        return null
    }

    fun validatePasswords(password: String, confirmPassword: String): Exception? {

        val trimmedPassword = password.trim()
        val trimmedConfirmPassword = confirmPassword.trim()

        if (trimmedConfirmPassword.isBlank()) {
            return EmptyConfirmPasswordFieldException
        }

        if (trimmedPassword != trimmedConfirmPassword) {
            return PasswordsDoNotMatchException
        }

        return null
    }

    fun validateVerificationCode(code: Int): Exception? {
        if (code < Constants.MIN_CODE_VALUE || code > Constants.MAX_CODE_VALUE) {
            return InvalidVerificationCodeException
        }

        return null
    }

    fun validateFullName(fullName: String): Exception? {
        val trimmedFullName = fullName.trim()

        if (trimmedFullName.isBlank()) {
            return EmptyFullNameFieldException
        }

        if (trimmedFullName.length > Constants.MAX_FULL_NAME_LENGTH) {
            return FullNameTooLongException
        }

        return null
    }

    fun validateUsername(username: String?): Exception? {
        if (username == null) {
            return null
        }

        val trimmedUsername = username.trim()

        if (trimmedUsername.length < Constants.MIN_USERNAME_LENGTH) {
            return ShortUsernameException
        }

        if (trimmedUsername.length > Constants.MAX_FULL_NAME_LENGTH) {
            return LongUsernameException
        }

        return null
    }

    fun validateBio(bio: String?): Exception? {
        if (bio == null) {
            return null
        }

        val trimmedBio = bio.trim()

        if (trimmedBio.length > Constants.MAX_BIO_LENGTH) {
            return LongBioException
        }

        return null
    }

}