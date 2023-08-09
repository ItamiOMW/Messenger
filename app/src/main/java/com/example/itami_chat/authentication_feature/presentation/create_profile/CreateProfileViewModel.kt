package com.example.itami_chat.authentication_feature.presentation.create_profile

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.exception.EmptyFullNameFieldException
import com.example.itami_chat.core.domain.exception.FullNameTooLongException
import com.example.itami_chat.core.domain.exception.LongBioException
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.UpdateProfileData
import com.example.itami_chat.core.domain.usecase.UpdateProfileUseCase
import com.example.itami_chat.core.presentation.state.StandardTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val application: Application,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : ViewModel() {


    var state by mutableStateOf(CreateProfileState())
        private set

    var fullNameState by mutableStateOf(StandardTextFieldState())
        private set

    var bioState by mutableStateOf(StandardTextFieldState())
        private set

    var imageUriState by mutableStateOf<Uri?>(null)
        private set

    private val _uiEvent = Channel<CreateProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: CreateProfileEvent) {
        when (event) {
            is CreateProfileEvent.OnFullNameInputChange -> {
                fullNameState = fullNameState.copy(text = event.newValue, errorMessage = null)
            }

            is CreateProfileEvent.OnBioInputChange -> {
                bioState = bioState.copy(text = event.newValue, errorMessage = null)
            }

            is CreateProfileEvent.OnImageUriChange -> {
                imageUriState = event.newUri
            }

            is CreateProfileEvent.OnCreateProfile -> {
                createProfile(
                    fullNameState.text,
                    bioState.text,
                    imageUriState
                )
            }
        }
    }

    private fun createProfile(
        fullName: String,
        bio: String?,
        imageUri: Uri?,
    ) {
        state = state.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val updateProfileData = UpdateProfileData(
                fullName = fullName,
                username = null,
                bio = bio
            )
            val result = updateProfileUseCase(updateProfileData, imageUri?.toString())

            if (result.fullNameError != null) {
                handleException(result.fullNameError, null)
            }

            if (result.bioError != null) {
                handleException(result.bioError, null)
            }

            when (val response = result.response) {
                is AppResponse.Success -> {
                    _uiEvent.send(CreateProfileUiEvent.OnNavigateToChats)
                }

                is AppResponse.Failed -> {
                    handleException(response.exception, response.message)
                }


                null -> Unit
            }
            state = state.copy(isLoading = false)
        }
    }


    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                state = state.copy(
                    error = application.getString(R.string.error_poor_network_connection)
                )
            }

            is EmptyFullNameFieldException -> {
                fullNameState = fullNameState.copy(
                    errorMessage = application.getString(R.string.error_empty_field)
                )
            }

            is FullNameTooLongException -> {
                fullNameState = fullNameState.copy(
                    errorMessage = application.getString(R.string.error_long_full_name)
                )
            }

            is LongBioException -> {
                bioState = bioState.copy(
                    errorMessage = application.getString(R.string.error_long_bio)
                )
            }

            else -> {
                state = state.copy(
                    error = message ?: application.getString(R.string.error_unknown)
                )
            }
        }
    }

}