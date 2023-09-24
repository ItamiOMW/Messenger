package com.example.itami_chat.chat_feature.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.model.MyUser
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.example.itami_chat.core.presentation.util.NavigationItem


@Composable
fun NavigationDrawerComponent(
    myUser: MyUser,
    isDarkMode: Boolean,
    imageLoader: ImageLoader,
    items: List<NavigationItem>,
    bottomNavigationItem: NavigationItem,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    headerColor: Color = MaterialTheme.colorScheme.surface,
    onHeaderColor: Color = MaterialTheme.colorScheme.onSurface,
    bodyColor: Color = MaterialTheme.colorScheme.inverseSurface,
    onBodyColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
    onItemClick: (NavigationItem) -> Unit,
    onChangeDarkModeState: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(headerColor)
                .padding(start = MaterialTheme.padding.small),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = myUser.profilePictureUrl,
                contentDescription = stringResource(R.string.desc_profile_picture),
                imageLoader = imageLoader,
                error = painterResource(id = R.drawable.sniper_mask),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(30.dp)),
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Text(
                    text = myUser.fullName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = onHeaderColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = myUser.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = onHeaderColor.copy(0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(bodyColor),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier.padding(top = MaterialTheme.padding.extraSmall)
            ) {
                items(items) { item ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(id = item.titleResId),
                                style = itemTextStyle,
                                color = onBodyColor,
                            )
                        },
                        shape = RoundedCornerShape(0.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent
                        ),
                        selected = false,
                        onClick = {
                            onItemClick(item)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.iconRedId),
                                contentDescription = stringResource(
                                    id = item.descResId
                                ),
                                tint = onBodyColor
                            )
                        }
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
            ) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = MaterialTheme.padding.small, end = MaterialTheme.padding.small),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    NavigationDrawerItem(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        label = {
                            Text(
                                text = stringResource(id = bottomNavigationItem.titleResId),
                                style = itemTextStyle,
                                color = onBodyColor,
                            )
                        },
                        shape = RoundedCornerShape(0.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent
                        ),
                        selected = false,
                        onClick = {
                            onItemClick(bottomNavigationItem)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = bottomNavigationItem.iconRedId),
                                contentDescription = stringResource(
                                    id = bottomNavigationItem.descResId
                                ),
                                tint = onBodyColor
                            )
                        }
                    )
                    IconButton(
                        modifier = Modifier.padding(MaterialTheme.padding.medium),
                        onClick = { onChangeDarkModeState() }
                    ) {
                        Icon(
                            painter = painterResource(id = if (isDarkMode) R.drawable.ic_light_mode else R.drawable.ic_dark_mode),
                            contentDescription = stringResource(id = R.string.change_dark_mode_state_desc),
                            tint = onBodyColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

    }
}
