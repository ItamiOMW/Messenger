package com.example.itami_chat.settings_feature.presentation.screens.verify_password_change

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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.itami_chat.R
import com.example.itami_chat.core.presentation.components.ButtonComponent
import com.example.itami_chat.core.presentation.components.OtpTextField
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyPasswordChangeScreen(
    onShowSnackbar: (message: String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    state: VerifyPasswordChangeState,
    codeState: String,
    uiEvent: Flow<VerifyPasswordChangeUiEvent>,
    onEvent: (event: VerifyPasswordChangeEvent) -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is VerifyPasswordChangeUiEvent.OnCodeVerified -> {
                    onNavigateToChangePassword()
                }

                is VerifyPasswordChangeUiEvent.OnShowSnackbar -> {
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
                        text = stringResource(id = R.string.title_verify_code),
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
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                Text(
                    text = stringResource(
                        id = R.string.text_enter_code,
                        state.myUser?.email ?: ""
                    ),
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
                OtpTextField(
                    otpText = { codeState },
                    onOtpTextChange = { newValue ->
                        onEvent(VerifyPasswordChangeEvent.OnCodeValueChange(newValue))
                    }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.enormous))
                ButtonComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.padding.large,
                            end = MaterialTheme.padding.large,
                        ),
                    text = stringResource(R.string.text_confirm),
                    enabled = { !state.isLoading },
                    onButtonClick = { onEvent(VerifyPasswordChangeEvent.OnVerifyCodeChange) }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
                TextButton(
                    onClick = {
                        onEvent(VerifyPasswordChangeEvent.OnResendCode)
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
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}