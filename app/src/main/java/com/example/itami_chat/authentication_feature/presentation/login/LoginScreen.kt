package com.example.itami_chat.authentication_feature.presentation.login

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.components.ButtonComponent
import com.example.itami_chat.core.presentation.components.CircularProgressBarComponent
import com.example.itami_chat.core.presentation.components.TextErrorComponent
import com.example.itami_chat.core.presentation.components.UnderlinedTextFieldComponent
import com.example.itami_chat.core.presentation.state.PasswordTextFieldState
import com.example.itami_chat.core.presentation.state.StandardTextFieldState
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


@Composable
fun LoginScreen(
    onNavigateToChats: () -> Unit,
    onNavigateToVerifyEmail: (email: String) -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateBack: () -> Unit,
    state: LoginState,
    emailFieldState: StandardTextFieldState,
    passwordFieldState: PasswordTextFieldState,
    onEvent: (LoginEvent) -> Unit,
    uiEvent: Flow<LoginUiEvent>,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is LoginUiEvent.OnNavigateToChats -> onNavigateToChats()
                is LoginUiEvent.OnNavigateToVerifyEmail -> onNavigateToVerifyEmail(event.email)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
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
                    text = stringResource(R.string.title_log_in),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.text_fill_fields_below),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
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
                    textValue = { emailFieldState.text },
                    onValueChange = { newValue ->
                        onEvent(LoginEvent.OnEmailInputChange(newValue))
                    },
                    keyboardType = KeyboardType.Email,
                    error = { emailFieldState.errorMessage },
                    label = stringResource(R.string.title_email),
                    enabled = { !state.isLoggingIn },
                )
                UnderlinedTextFieldComponent(
                    modifier = Modifier.fillMaxWidth(),
                    textValue = { passwordFieldState.text },
                    onValueChange = { newValue ->
                        onEvent(LoginEvent.OnPasswordInputChange(newValue))
                    },
                    error = { passwordFieldState.errorMessage },
                    label = stringResource(R.string.title_password),
                    enabled = { !state.isLoggingIn },
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (passwordFieldState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingContent = {
                        IconButton(
                            onClick = { onEvent(LoginEvent.OnChangePasswordVisibility) }
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(
                                    id = if (passwordFieldState.isPasswordVisible) R.drawable.ic_visibility_off
                                    else R.drawable.ic_visibility
                                ),
                                contentDescription = stringResource(R.string.desc_change_password_visibility),
                            )
                        }
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
                    enabled = { !state.isLoggingIn },
                    onButtonClick = { onEvent(LoginEvent.OnLogin) }
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
            TextButton(
                onClick = onNavigateToForgotPassword,
                enabled = !state.isLoggingIn
            ) {
                Text(
                    text = stringResource(R.string.text_forgot_password),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        CircularProgressBarComponent(
            modifier = Modifier.align(Alignment.Center),
            isLoading = { state.isLoggingIn }
        )
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = true) {
        LoginScreen(
            onNavigateToChats = { },
            onNavigateToVerifyEmail = { },
            onNavigateToForgotPassword = {},
            onNavigateBack = {},
            state = LoginState(),
            emailFieldState = StandardTextFieldState(),
            passwordFieldState = PasswordTextFieldState(),
            onEvent = {},
            uiEvent = flow { }
        )
    }
}