package com.example.itami_chat.chat_feature.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.itami_chat.R


@Composable
fun ImagesComponent(
    imageUrls: List<String>,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    onImageClicked: (imageUrl: String) -> Unit,
) {

    if (imageUrls.size == 1) {
        AsyncImage(
            modifier = Modifier.size(width = 150.dp, height = 330.dp),
            model = imageUrls.first(),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.desc_image)
        )
    } else {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(3),
        ) {
            items(imageUrls, key = { it }) { url ->
                AsyncImage(
                    modifier = Modifier
                        .size(60.dp, 120.dp)
                        .clickable {
                            onImageClicked(url)
                        },
                    model = url,
                    contentDescription = stringResource(R.string.desc_image),
                    imageLoader = imageLoader
                )
            }
        }
    }

}