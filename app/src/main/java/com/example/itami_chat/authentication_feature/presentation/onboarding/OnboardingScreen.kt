package com.example.itami_chat.authentication_feature.presentation.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.itami_chat.R
import com.example.itami_chat.authentication_feature.presentation.onboarding.components.OnboardingContentComponent
import com.example.itami_chat.authentication_feature.presentation.onboarding.util.OnboardingData
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.presentation.components.ButtonComponent
import com.example.itami_chat.core.presentation.components.OutlinedButtonComponent
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    state: OnboardingState,
    onEvent: (OnboardingEvent) -> Unit,
) {

    val isDarkMode = state.isDarkTheme
    val theme = state.theme

    val onboardingItems = remember {
        listOf(
            OnboardingData(
                imageId = when (theme) {
                    Theme.Default -> {
                        if (isDarkMode) R.drawable.handshake_default_dark else R.drawable.handshake_default_light
                    }

                    Theme.Blue -> {
                        if (isDarkMode) R.drawable.handshake_dark_blue else R.drawable.handshake_light_blue
                    }
                },
                titleId = R.string.title_welcome,
                descId = R.string.welcome_onboarding_desc
            ),
            OnboardingData(
                imageId = when (theme) {
                    Theme.Default -> {
                        if (isDarkMode) R.drawable.online_wishes_default_dark else R.drawable.online_wishes_default_light
                    }

                    Theme.Blue -> {
                        if (isDarkMode) R.drawable.online_wishes_dark_blue else R.drawable.online_wishes_light_blue
                    }
                },
                titleId = R.string.title_group_chats,
                descId = R.string.group_chats_onboarding_desc
            ),
            OnboardingData(
                imageId = when (theme) {
                    Theme.Default -> {
                        if (isDarkMode) R.drawable.mobile_inbox_default_dark else R.drawable.mobile_inbox_default_light
                    }

                    Theme.Blue -> {
                        if (isDarkMode) R.drawable.mobile_inbox_dark_blue else R.drawable.mobile_inbox_light_blue
                    }
                },
                titleId = R.string.title_interface,
                descId = R.string.interface_onboarding_desc
            ),
        )
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        onboardingItems.size
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
            IconButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = MaterialTheme.padding.extraMedium),
                onClick = { onEvent(OnboardingEvent.ChangeDarkModeState) }
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isDarkMode) R.drawable.ic_light_mode else R.drawable.ic_dark_mode
                    ),
                    contentDescription = stringResource(R.string.change_dark_mode_state_desc),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            HorizontalPager(state = pagerState) { page ->
                OnboardingContentComponent(
                    onboardingData = onboardingItems[page],
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
            Row(
                Modifier.height(50.dp),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            ) {
                repeat(onboardingItems.size) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onBackground
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color)
                            .size(10.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            ButtonComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MaterialTheme.padding.large,
                        end = MaterialTheme.padding.large
                    ),
                text = stringResource(id = R.string.title_log_in),
                enabled = { true },
                onButtonClick = onNavigateToLogin,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            OutlinedButtonComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MaterialTheme.padding.large,
                        end = MaterialTheme.padding.large
                    ),
                text = stringResource(id = R.string.title_create_new_account),
                enabled = { true },
                onButtonClick = onNavigateToRegister
            )
        }
    }
}


@Preview
@Composable
fun OnboardingScreenPreview() {
    ItamiChatTheme(theme = Theme.Default, isDarkMode = false) {
        OnboardingScreen(
            onNavigateToLogin = { },
            onNavigateToRegister = {},
            state = OnboardingState(),
            onEvent = {}
        )
    }
}