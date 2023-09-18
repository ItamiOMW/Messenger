package com.example.itami_chat.authentication_feature.presentation.create_profile

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.components.AnimatedCounter
import com.example.itami_chat.core.presentation.components.ButtonComponent
import com.example.itami_chat.core.presentation.components.CircularProgressBarComponent
import com.example.itami_chat.core.presentation.components.ImagePickerComponent
import com.example.itami_chat.core.presentation.components.TextErrorComponent
import com.example.itami_chat.core.presentation.components.UnderlinedTextFieldComponent
import com.example.itami_chat.core.presentation.state.StandardTextFieldState
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import com.example.itami_chat.core.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


@Composable
fun CreateProfileScreen(
    onNavigateToChats: () -> Unit,
    onNavigateBack: () -> Unit,
    imageLoader: ImageLoader,
    state: CreateProfileState,
    fullNameState: StandardTextFieldState,
    bioState: StandardTextFieldState,
    imageUriState: Uri?,
    uiEvent: Flow<CreateProfileUiEvent>,
    onEvent: (CreateProfileEvent) -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                CreateProfileUiEvent.OnNavigateToChats -> onNavigateToChats()
            }
        }
    }

    BackHandler {
        onNavigateBack()
    }

    val fullNameCharactersLeft = remember(fullNameState.text.length) {
        Constants.MAX_FULL_NAME_LENGTH - fullNameState.text.length
    }

    val bioCharactersLeft = remember(bioState.text.length) {
        Constants.MAX_BIO_LENGTH - bioState.text.length
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let { onEvent(CreateProfileEvent.OnImageUriChange(uri)) } }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
            IconButton(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = MaterialTheme.padding.extraMedium),
                onClick = onNavigateBack
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = stringResource(
                        R.string.desc_navigate_back
                    )
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MaterialTheme.padding.extraLarge,
                        end = MaterialTheme.padding.extraLarge
                    ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Text(
                    text = stringResource(R.string.title_create_profile),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.text_create_profile),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
            ImagePickerComponent(
                modifier = Modifier
                    .size(120.dp),
                imagePath = { imageUriState },
                imageLoader = imageLoader,
                onAddImageButtonClicked = {
                    launcher.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MaterialTheme.padding.small,
                        end = MaterialTheme.padding.small
                    ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                UnderlinedTextFieldComponent(
                    modifier = Modifier.fillMaxWidth(),
                    textValue = { fullNameState.text },
                    onValueChange = { newValue ->
                        if (newValue.length <= Constants.MAX_FULL_NAME_LENGTH) {
                            onEvent(CreateProfileEvent.OnFullNameInputChange(newValue))
                        }
                    },
                    keyboardType = KeyboardType.Email,
                    error = { fullNameState.errorMessage },
                    label = stringResource(R.string.title_full_name),
                    enabled = { !state.isLoading },
                    trailingContent = {
                        AnimatedCounter(
                            count = fullNameCharactersLeft
                        )
                    }
                )
                UnderlinedTextFieldComponent(
                    modifier = Modifier.fillMaxWidth(),
                    textValue = { bioState.text },
                    onValueChange = { newValue ->
                        if (newValue.length <= Constants.MAX_BIO_LENGTH) {
                            onEvent(CreateProfileEvent.OnBioInputChange(newValue))
                        }
                    },
                    keyboardType = KeyboardType.Email,
                    error = { bioState.errorMessage },
                    label = stringResource(R.string.title_about_me),
                    enabled = { !state.isLoading },
                    trailingContent = {
                        AnimatedCounter(
                            count = bioCharactersLeft
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextErrorComponent(text = { state.error })
                ButtonComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.padding.large,
                            end = MaterialTheme.padding.large,
                        ),
                    text = stringResource(R.string.text_continue),
                    enabled = { !state.isLoading },
                    onButtonClick = { onEvent(CreateProfileEvent.OnCreateProfile) }
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
            TextButton(
                onClick = onNavigateToChats,
                enabled = !state.isLoading
            ) {
                Text(
                    text = stringResource(R.string.title_skip),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        CircularProgressBarComponent(
            modifier = Modifier.align(Alignment.Center),
            isLoading = { state.isLoading }
        )
    }

}

@Preview
@Composable
private fun CreateProfileScreenPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = false) {
        CreateProfileScreen(
            onNavigateToChats = { },
            onNavigateBack = { },
            imageLoader = ImageLoader(LocalContext.current),
            state = CreateProfileState(),
            fullNameState = StandardTextFieldState(),
            bioState = StandardTextFieldState(),
            imageUriState = null,
            uiEvent = flow { },
            onEvent = { }
        )
    }
}