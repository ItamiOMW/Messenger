package com.example.itami_chat.settings_feature.presentation.screens.messages_permission

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import com.example.itami_chat.core.domain.model.MessagesPermission
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesPermissionScreen(
    onShowSnackbar: (message: String) -> Unit,
    onNavigateBack: () -> Unit,
    state: MessagesPermissionState,
    uiEvent: Flow<MessagesPermissionUiEvent>,
    onEvent: (event: MessagesPermissionEvent) -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is MessagesPermissionUiEvent.OnNavigateBack -> {
                    onNavigateBack()
                }

                is MessagesPermissionUiEvent.OnShowSnackbar -> {
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
                        text = stringResource(id = R.string.title_messages_permission),
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
                                onEvent(MessagesPermissionEvent.OnSave)
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                    text = stringResource(R.string.text_choose_who_can_send_you_messages),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = MaterialTheme.padding.small)
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (!state.isLoading) {
                                onEvent(
                                    MessagesPermissionEvent.OnSelectPermission(
                                        MessagesPermission.ANYONE
                                    )
                                )
                            }
                        }
                        .padding(
                            start = MaterialTheme.padding.small,
                            end = MaterialTheme.padding.extraSmall,
                            top = MaterialTheme.padding.extraSmall,
                            bottom = MaterialTheme.padding.extraSmall,
                        )
                ) {
                    Text(
                        text = stringResource(id = R.string.text_anyone),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    RadioButton(
                        selected = state.selectedPermission == MessagesPermission.ANYONE,
                        onClick = {
                            if (!state.isLoading) {
                                onEvent(
                                    MessagesPermissionEvent.OnSelectPermission(
                                        MessagesPermission.ANYONE
                                    )
                                )
                            }
                        }
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = MaterialTheme.padding.small),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (!state.isLoading) {
                                onEvent(
                                    MessagesPermissionEvent.OnSelectPermission(
                                        MessagesPermission.CONTACTS_ONLY
                                    )
                                )
                            }
                        }
                        .padding(
                            start = MaterialTheme.padding.small,
                            end = MaterialTheme.padding.extraSmall,
                            top = MaterialTheme.padding.extraSmall,
                            bottom = MaterialTheme.padding.extraSmall,
                        )
                ) {
                    Text(
                        text = stringResource(id = R.string.text_contacts_only),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    RadioButton(
                        selected = state.selectedPermission == MessagesPermission.CONTACTS_ONLY,
                        onClick = {
                            if (!state.isLoading) {
                                onEvent(
                                    MessagesPermissionEvent.OnSelectPermission(
                                        MessagesPermission.CONTACTS_ONLY
                                    )
                                )
                            }
                        }
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