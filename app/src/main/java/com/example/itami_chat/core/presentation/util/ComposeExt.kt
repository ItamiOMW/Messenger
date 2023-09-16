package com.example.itami_chat.core.presentation.util

import androidx.compose.foundation.lazy.LazyListState


val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0