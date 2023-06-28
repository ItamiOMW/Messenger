package com.example.itami_chat.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import com.example.itami_chat.core.presentation.ui.theme.Itami_ChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Itami_ChatTheme {
                MaterialTheme
            }
        }
    }
}

