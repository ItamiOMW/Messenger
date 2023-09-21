package com.example.itami_chat.settings_feature.presentation.screens.appearance_setting

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.components.IconToTextComponent
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSettingScreen(
    onNavigateBack: () -> Unit,
    state: AppearanceSettingState,
    onEvent: (event: AppearanceSettingEvent) -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_appearance),
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
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Text(
                text = stringResource(R.string.text_change_application_theme),
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
                        onEvent(AppearanceSettingEvent.OnChangeTheme(Theme.Default))
                    }
                    .padding(
                        start = MaterialTheme.padding.small,
                        end = MaterialTheme.padding.extraSmall,
                        top = MaterialTheme.padding.extraSmall,
                        bottom = MaterialTheme.padding.extraSmall,
                    )
            ) {
                Text(
                    text = stringResource(id = R.string.text_default_theme),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                RadioButton(
                    selected = state.theme == Theme.Default,
                    onClick = {
                        onEvent(AppearanceSettingEvent.OnChangeTheme(Theme.Default))
                    }
                )
            }
            Divider(
                modifier = Modifier.fillMaxWidth().padding(start = MaterialTheme.padding.small),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onEvent(AppearanceSettingEvent.OnChangeTheme(Theme.Blue))
                    }
                    .padding(
                        start = MaterialTheme.padding.small,
                        end = MaterialTheme.padding.extraSmall,
                        top = MaterialTheme.padding.extraSmall,
                        bottom = MaterialTheme.padding.extraSmall,
                    )
            ) {
                Text(
                    text = stringResource(id = R.string.text_blue_theme),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                RadioButton(
                    selected = state.theme == Theme.Blue,
                    onClick = {
                        onEvent(AppearanceSettingEvent.OnChangeTheme(Theme.Blue))
                    }
                )
            }
            Divider(
                modifier = Modifier.fillMaxWidth().padding(start = MaterialTheme.padding.small),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            IconToTextComponent(
                text = if (state.isDarkMode) stringResource(R.string.text_change_to_light_mode)
                else stringResource(R.string.text_change_to_dark_mode),
                icon = painterResource(
                    id = if (state.isDarkMode) R.drawable.ic_light_mode else R.drawable.ic_dark_mode
                ),
                color = MaterialTheme.colorScheme.primary,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onEvent(AppearanceSettingEvent.OnChangeDarkModeState)
                    }
                    .padding(
                        bottom = MaterialTheme.padding.medium,
                        top = MaterialTheme.padding.medium
                    ),
            )
        }
    }

}