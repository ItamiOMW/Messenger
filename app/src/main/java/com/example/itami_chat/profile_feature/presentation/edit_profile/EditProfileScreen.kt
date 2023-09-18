package com.example.itami_chat.profile_feature.presentation.edit_profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.example.itami_chat.R
import com.example.itami_chat.core.presentation.components.ImagePickerComponent
import com.example.itami_chat.core.presentation.components.TextFieldInfoComponent
import com.example.itami_chat.core.presentation.state.StandardTextFieldState
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onShowSnackbar: (message: String) -> Unit,
    onNavigateBack: () -> Unit,
    imageLoader: ImageLoader,
    state: EditProfileState,
    pictureUriState: Uri?,
    fullNameInputState: StandardTextFieldState,
    usernameInputState: StandardTextFieldState,
    bioInputState: StandardTextFieldState,
    uiEvent: Flow<EditProfileUiEvent>,
    onEvent: (event: EditProfileEvent) -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is EditProfileUiEvent.OnNavigateBack -> {
                    onNavigateBack()
                }

                is EditProfileUiEvent.OnShowSnackbar -> {
                    onShowSnackbar(event.message)
                }
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let { onEvent(EditProfileEvent.OnPictureUriChange(uri)) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_edit_profile),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNavigateBack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = stringResource(R.string.desc_navigate_back),
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (!state.isLoading) {
                                onEvent(EditProfileEvent.OnEditProfile)
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_done),
                            contentDescription = stringResource(R.string.desc_done_icon),
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (state.myUser != null) {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    ImagePickerComponent(
                        modifier = Modifier
                            .size(150.dp),
                        imagePath = {
                            pictureUriState ?: state.myUser.profilePictureUrl
                        },
                        imageLoader = imageLoader,
                        onAddImageButtonClicked = {
                            launcher.launch(
                                PickVisualMediaRequest(
                                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        imageSize = 150.dp,
                        fabSize = 50.dp,
                        iconSize = 24.dp
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    TextFieldInfoComponent(
                        value = { fullNameInputState.text },
                        onValueChange = { newValue ->
                            onEvent(EditProfileEvent.OnFullNameInputChange(newValue))
                        },
                        error = { fullNameInputState.errorMessage },
                        hint = stringResource(id = R.string.hint_not_a_username_will_be_displayed),
                        label = stringResource(id = R.string.title_full_name),
                        icon = painterResource(id = R.drawable.ic_person),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.padding.small,
                                top = MaterialTheme.padding.small
                            ),
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                    TextFieldInfoComponent(
                        value = { usernameInputState.text },
                        onValueChange = { newValue ->
                            onEvent(EditProfileEvent.OnUsernameInputChange(newValue))
                        },
                        error = { usernameInputState.errorMessage },
                        hint = stringResource(id = R.string.hint_able_to_find_by_username),
                        label = stringResource(id = R.string.title_username),
                        icon = painterResource(id = R.drawable.ic_alternate_email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.padding.small,
                                top = MaterialTheme.padding.small
                            ),
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                    TextFieldInfoComponent(
                        value = { bioInputState.text },
                        onValueChange = { newValue ->
                            onEvent(EditProfileEvent.OnBioInputChange(newValue))
                        },
                        error = { bioInputState.errorMessage },
                        hint = stringResource(id = R.string.hint_about_me),
                        label = stringResource(id = R.string.title_about_me),
                        icon = painterResource(id = R.drawable.ic_info),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.padding.small,
                                top = MaterialTheme.padding.small
                            ),
                    )
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}