package com.example.itami_chat.authentication_feature.presentation.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.itami_chat.R
import com.example.itami_chat.authentication_feature.presentation.onboarding.util.OnboardingData
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.ui.theme.spacing


@Composable
fun OnboardingContentComponent(
    onboardingData: OnboardingData,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = onboardingData.imageId),
            contentDescription = stringResource(R.string.content_illustration_desc),
            modifier = Modifier.size(200.dp, 185.dp)
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Text(
            text = stringResource(id = onboardingData.titleId),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
        Text(
            text = stringResource(id = onboardingData.descId),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MaterialTheme.padding.extraMedium,
                    end = MaterialTheme.padding.extraMedium
                ),
            textAlign = TextAlign.Center
        )
    }
}