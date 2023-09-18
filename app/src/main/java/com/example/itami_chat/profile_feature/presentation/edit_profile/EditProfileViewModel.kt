package com.example.itami_chat.profile_feature.presentation.edit_profile

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
import com.example.itami_chat.core.domain.exception.LongUsernameException
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.exception.ShortUsernameException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.UpdateProfileData
import com.example.itami_chat.core.domain.preferences.UserManager
import com.example.itami_chat.core.domain.use_case.UpdateProfileUseCase
import com.example.itami_chat.core.presentation.state.StandardTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val userManager: UserManager,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<EditProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(EditProfileState())
        private set

    var profilePictureUriState by mutableStateOf<Uri?>(null)
        private set

    var fullNameInputState by mutableStateOf(StandardTextFieldState())
        private set

    var usernameInputState by mutableStateOf(StandardTextFieldState())
        private set

    var bioInputState by mutableStateOf(StandardTextFieldState())
        private set

    init {
        getMyUser()
    }

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.OnBioInputChange -> {
                bioInputState = bioInputState.copy(text = event.newValue, errorMessage = null)
            }

            is EditProfileEvent.OnFullNameInputChange -> {
                fullNameInputState = fullNameInputState.copy(text = event.newValue, errorMessage = null)
            }

            is EditProfileEvent.OnUsernameInputChange -> {
                usernameInputState = usernameInputState.copy(text = event.newValue, errorMessage = null)
            }

            is EditProfileEvent.OnPictureUriChange -> {
                profilePictureUriState = event.newUri
            }

            is EditProfileEvent.OnEditProfile -> {
                editProfile(
                    fullNameInputState.text,
                    usernameInputState.text.ifBlank { null },
                    bioInputState.text.ifBlank { null },
                    pictureUri = profilePictureUriState?.toString()
                )
            }
        }
    }

    private fun editProfile(
        fullName: String,
        username: String?,
        bio: String?,
        pictureUri: String?,
    ) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            val profileValidationResult = updateProfileUseCase(
                updateProfileData = UpdateProfileData(fullName, username, bio),
                profilePictureUri = pictureUri
            )
            if (profileValidationResult.fullNameError != null) {
                handleException(profileValidationResult.fullNameError, null)
            }
            if (profileValidationResult.usernameError != null) {
                handleException(profileValidationResult.usernameError, null)
            }
            if (profileValidationResult.bioError != null) {
                handleException(profileValidationResult.bioError, null)
            }
            when (val result = profileValidationResult.response) {
                is AppResponse.Success -> {
                    sendUiEvent(
                        EditProfileUiEvent.OnShowSnackbar(application.getString(R.string.text_profile_updated))
                    )
                }

                is AppResponse.Failed -> {
                    handleException(result.exception, result.message)
                }

                null -> Unit
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun getMyUser() {
        viewModelScope.launch {
            userManager.user.collect { myUser ->
                state = state.copy(myUser = myUser)
                fullNameInputState = fullNameInputState.copy(text = myUser.fullName)
                usernameInputState = usernameInputState.copy(text = myUser.username ?: "")
                bioInputState = bioInputState.copy(text = myUser.bio ?: "")
            }
        }
    }

    private fun sendUiEvent(event: EditProfileUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                sendUiEvent(
                    EditProfileUiEvent.OnShowSnackbar(
                        application.getString(R.string.error_poor_network_connection)
                    )
                )
            }

            is ShortUsernameException -> {
                usernameInputState = usernameInputState.copy(
                    errorMessage = application.getString(R.string.error_short_username)
                )
            }

            is LongUsernameException -> {
                usernameInputState = usernameInputState.copy(
                    errorMessage = application.getString(R.string.error_long_username)
                )
            }

            is EmptyFullNameFieldException -> {
                fullNameInputState = fullNameInputState.copy(
                    errorMessage = application.getString(R.string.error_empty_field)
                )
            }

            is FullNameTooLongException -> {
                fullNameInputState = fullNameInputState.copy(
                    errorMessage = application.getString(R.string.error_long_full_name)
                )
            }

            is LongBioException -> {
                bioInputState = bioInputState.copy(
                    errorMessage = application.getString(R.string.error_long_bio)
                )
            }

            else -> {
                sendUiEvent(
                    EditProfileUiEvent.OnShowSnackbar(
                        message = message ?: application.getString(
                            R.string.error_unknown
                        )
                    )
                )
            }
        }
    }

}
