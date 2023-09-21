package com.example.itami_chat.settings_feature.presentation.screens.change_password

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.itami_chat.R
import com.example.itami_chat.core.presentation.components.ButtonComponent
import com.example.itami_chat.core.presentation.components.UnderlinedTextFieldComponent
import com.example.itami_chat.core.presentation.state.PasswordTextFieldState
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    onShowSnackbar: (message: String) -> Unit,
    onNavigateBack: () -> Unit,
    state: ChangePasswordState,
    passwordState: PasswordTextFieldState,
    confirmPasswordState: PasswordTextFieldState,
    uiEvent: Flow<ChangePasswordUiEvent>,
    onEvent: (event: ChangePasswordEvent) -> Unit,
) {
    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is ChangePasswordUiEvent.OnNavigateBack -> {
                    onNavigateBack()
                }

                is ChangePasswordUiEvent.OnShowSnackbar -> {
                    onShowSnackbar(event.message)
                }
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_change_password),
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
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
                Text(
                    text = stringResource(id = R.string.text_come_up_with_new_password),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.padding.extraLarge,
                            end = MaterialTheme.padding.extraLarge
                        ),
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
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
                        textValue = { passwordState.text },
                        onValueChange = { newValue ->
                            onEvent(ChangePasswordEvent.OnPasswordInputChange(newValue))
                        },
                        error = { passwordState.errorMessage },
                        label = stringResource(R.string.title_password),
                        enabled = { !state.isLoading },
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (passwordState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingContent = {
                            IconButton(
                                onClick = { onEvent(ChangePasswordEvent.OnChangePasswordVisibility) }
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(
                                        id = if (passwordState.isPasswordVisible) R.drawable.ic_visibility_off
                                        else R.drawable.ic_visibility
                                    ),
                                    contentDescription = stringResource(R.string.desc_change_password_visibility)
                                )
                            }
                        }
                    )
                    UnderlinedTextFieldComponent(
                        modifier = Modifier.fillMaxWidth(),
                        textValue = { confirmPasswordState.text },
                        onValueChange = { newValue ->
                            onEvent(ChangePasswordEvent.OnConfirmPasswordInputChange(newValue))
                        },
                        error = { confirmPasswordState.errorMessage },
                        label = stringResource(R.string.title_confirm_password),
                        enabled = { !state.isLoading },
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (confirmPasswordState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingContent = {
                            IconButton(
                                onClick = { onEvent(ChangePasswordEvent.OnChangeConfirmPasswordVisibility) }
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(
                                        id = if (confirmPasswordState.isPasswordVisible) R.drawable.ic_visibility_off
                                        else R.drawable.ic_visibility
                                    ),
                                    contentDescription = stringResource(R.string.desc_change_password_visibility)
                                )
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                ButtonComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.padding.large,
                            end = MaterialTheme.padding.large,
                        ),
                    text = stringResource(R.string.text_continue),
                    enabled = { !state.isLoading },
                    onButtonClick = { onEvent(ChangePasswordEvent.OnChangePassword) }
                )
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