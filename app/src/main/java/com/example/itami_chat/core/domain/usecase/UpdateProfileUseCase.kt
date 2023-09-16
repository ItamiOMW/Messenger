package com.example.itami_chat.core.domain.usecase

import com.example.itami_chat.core.domain.model.ProfileValidationResult
import com.example.itami_chat.core.domain.model.UpdateProfileData
import com.example.itami_chat.core.domain.repository.UserRepository
import com.example.itami_chat.core.domain.util.ValidationUtil

class UpdateProfileUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(
        updateProfileData: UpdateProfileData,
        profilePictureUri: String? = null,
    ): ProfileValidationResult {

        val fullNameError = ValidationUtil.validateFullName(updateProfileData.fullName)
        val usernameError = ValidationUtil.validateUsername(updateProfileData.username)
        val bioError = ValidationUtil.validateBio(updateProfileData.bio)

        if (fullNameError != null || usernameError != null || bioError != null) {
            return ProfileValidationResult(fullNameError, usernameError, bioError)
        }

        val response = userRepository.updateProfile(updateProfileData, profilePictureUri)
        return ProfileValidationResult(response = response)

    }

}