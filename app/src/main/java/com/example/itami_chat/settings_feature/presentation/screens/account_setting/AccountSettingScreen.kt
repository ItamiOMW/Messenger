package com.example.itami_chat.settings_feature.presentation.screens.account_setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.itami_chat.R
import com.example.itami_chat.core.presentation.components.DefaultDialogComponent
import com.example.itami_chat.core.presentation.components.IconToTextComponent
import com.example.itami_chat.core.presentation.ui.theme.padding
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingScreen(
    onShowSnackbar: (message: String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToVerifyPasswordChange: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    state: AccountSettingState,
    uiEvent: Flow<AccountSettingsUiEvent>,
    onEvent: (event: AccountSettingsEvent) -> Unit,
) {
    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is AccountSettingsUiEvent.OnChangePasswordCodeSent -> {
                    onNavigateToVerifyPasswordChange()
                }

                is AccountSettingsUiEvent.OnShowSnackbar -> {
                    onShowSnackbar(event.message)
                }

                is AccountSettingsUiEvent.OnAccountDeleted -> {
                    onNavigateToOnboarding()
                }

                is AccountSettingsUiEvent.OnLogout -> {
                    onNavigateToOnboarding()
                }
            }
        }
    }

    if (state.showDeleteAccountDialog) {
        DefaultDialogComponent(
            title = stringResource(R.string.title_delete_account),
            text = stringResource(R.string.text_delete_account_warning),
            confirmButtonText = stringResource(R.string.text_delete),
            onConfirm = {
                if (!state.isLoading) {
                    onEvent(AccountSettingsEvent.OnHideDeleteAccountDialog)
                    onEvent(AccountSettingsEvent.OnDeleteAccount)
                }
            },
            onDismiss = {
                onEvent(AccountSettingsEvent.OnHideDeleteAccountDialog)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_account),
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
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconToTextComponent(
                    text = stringResource(R.string.text_change_password),
                    icon = painterResource(id = R.drawable.ic_lock_reset),
                    color = MaterialTheme.colorScheme.onBackground,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (!state.isLoading) {
                                onEvent(AccountSettingsEvent.OnSendPasswordChangeCode)
                            }
                        }
                        .padding(
                            bottom = MaterialTheme.padding.medium,
                            top = MaterialTheme.padding.medium
                        ),
                )
                IconToTextComponent(
                    text = stringResource(R.string.text_delete_account),
                    icon = painterResource(id = R.drawable.ic_delete),
                    color = MaterialTheme.colorScheme.onBackground,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (!state.isLoading) {
                                onEvent(AccountSettingsEvent.OnShowDeleteAccountDialog)
                            }
                        }
                        .padding(
                            bottom = MaterialTheme.padding.medium,
                            top = MaterialTheme.padding.medium
                        ),
                )
                IconToTextComponent(
                    text = stringResource(R.string.title_logout),
                    icon = painterResource(id = R.drawable.ic_leave),
                    color = MaterialTheme.colorScheme.onBackground,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (!state.isLoading) {
                                onEvent(AccountSettingsEvent.OnLogout)
                            }
                        }
                        .padding(
                            bottom = MaterialTheme.padding.medium,
                            top = MaterialTheme.padding.medium
                        ),
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