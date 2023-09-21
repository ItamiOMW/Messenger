package com.example.itami_chat.settings_feature.presentation.screens.settings

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.itami_chat.R
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing
import com.example.itami_chat.settings_feature.presentation.components.SettingItem
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToAccountSetting: () -> Unit,
    onNavigateToPrivacySetting: () -> Unit,
    onNavigateToAppearanceSetting: () -> Unit,
    onNavigateToNotificationsSetting: () -> Unit,
    onNavigateToLanguageSetting: () -> Unit,
    onNavigateToAboutApplication: () -> Unit,
    imageLoader: ImageLoader,
    state: SettingsState,
    uiEvent: Flow<SettingsUiEvent>,
    onEvent: (event: SettingsEvent) -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                SettingsUiEvent.OnLogout -> {
                    onNavigateToOnboarding()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_settings),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(ScrollState(0))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigateToEditProfile()
                    }
                    .padding(MaterialTheme.padding.small),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = state.myUser?.profilePictureUrl,
                    contentDescription = stringResource(R.string.desc_profile_picture),
                    imageLoader = imageLoader,
                    error = painterResource(id = R.drawable.sniper_mask),
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(30.dp)),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text(
                        text = state.myUser?.fullName ?: "",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = state.myUser?.email ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            SettingItem(
                title = stringResource(R.string.title_account),
                description = stringResource(R.string.account_setting_description),
                icon = painterResource(id = R.drawable.ic_key),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigateToAccountSetting()
                    }
                    .padding(MaterialTheme.padding.small)
            )
            SettingItem(
                title = stringResource(R.string.title_privacy),
                description = stringResource(R.string.privacy_setting_description),
                icon = painterResource(id = R.drawable.ic_lock),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigateToPrivacySetting()
                    }
                    .padding(MaterialTheme.padding.small)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            SettingItem(
                title = stringResource(R.string.title_appearance),
                description = stringResource(R.string.appearance_setting_description),
                icon = painterResource(id = R.drawable.ic_appearance_settings),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigateToAppearanceSetting()
                    }
                    .padding(MaterialTheme.padding.small)
            )
            SettingItem(
                title = stringResource(R.string.title_notifications),
                description = stringResource(R.string.notifications_setting_description),
                icon = painterResource(id = R.drawable.icon_notifications),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigateToNotificationsSetting()
                    }
                    .padding(MaterialTheme.padding.small)
            )
            SettingItem(
                title = stringResource(R.string.title_language),
                description = stringResource(R.string.language_setting_description),
                icon = painterResource(id = R.drawable.ic_language),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigateToLanguageSetting()
                    }
                    .padding(MaterialTheme.padding.small)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            SettingItem(
                title = stringResource(R.string.title_about_app),
                description = stringResource(R.string.about_app_setting_description),
                icon = painterResource(id = R.drawable.ic_info),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigateToAboutApplication()
                    }
                    .padding(MaterialTheme.padding.small)
            )
            SettingItem(
                title = stringResource(R.string.title_logout),
                description = stringResource(R.string.logout_setting_description),
                icon = painterResource(id = R.drawable.ic_leave),
                titleColor = MaterialTheme.colorScheme.error,
                descriptionColor = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onEvent(SettingsEvent.OnLogout)
                    }
                    .padding(MaterialTheme.padding.small)
            )
        }
    }
}