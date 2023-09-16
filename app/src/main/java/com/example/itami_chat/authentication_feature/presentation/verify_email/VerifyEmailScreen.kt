package com.example.itami_chat.authentication_feature.presentation.verify_email

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.components.ButtonComponent
import com.example.itami_chat.core.presentation.components.CircularProgressBarComponent
import com.example.itami_chat.core.presentation.components.OtpTextField
import com.example.itami_chat.core.presentation.components.TextErrorComponent
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


@Composable
fun VerifyEmailScreen(
    email: String,
    onNavigateToCreateProfile: () -> Unit,
    onNavigateBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    state: VerifyEmailState,
    uiEvent: Flow<VerifyEmailUiEvent>,
    onEvent: (VerifyEmailEvent) -> Unit,
) {


    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                VerifyEmailUiEvent.OnEmailVerified -> {
                    onNavigateToCreateProfile()
                }

                is VerifyEmailUiEvent.OnShowSnackbar -> {
                    onShowSnackbar(event.message)
                }
            }
        }
    }

    BackHandler {
        onNavigateBack()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
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
                    text = stringResource(R.string.title_enter_code),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(id = R.string.text_enter_code, email),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
            OtpTextField(
                otpText = { state.codeValue },
                onOtpTextChange = { newValue ->
                    onEvent(VerifyEmailEvent.OnCodeInputChange(newValue))
                }
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.enormous))
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
                    text = stringResource(R.string.text_confirm),
                    enabled = { !state.isLoading },
                    onButtonClick = { onEvent(VerifyEmailEvent.OnConfirm) }
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
            TextButton(
                onClick = {
                    onEvent(VerifyEmailEvent.OnResendCode)
                },
                enabled = !state.isLoading
            ) {
                Text(
                    text = stringResource(R.string.text_resend_code),
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
fun VerifyEmailScreenPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = false) {
        VerifyEmailScreen(
            email = "itamiomw@gmail.com",
            onNavigateToCreateProfile = { },
            onNavigateBack = { },
            onShowSnackbar = {},
            state = VerifyEmailState(),
            uiEvent = flow { },
            onEvent = {}
        )
    }
}