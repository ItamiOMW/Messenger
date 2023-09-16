package com.example.itami_chat.core.utils

import android.content.Context
import com.example.itami_chat.R
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale


object DateTimeUtil {

    fun formatUtcDateTimeForMessage(
        timestamp: Long,
        locale: Locale = Locale.getDefault(),
    ): String {
        val currentDateTime = LocalDateTime.now()
        val utcDateTime = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC)
        val timestampDateTime = utcDateTime.atZone(ZoneId.systemDefault())
        val formatter = if (currentDateTime.toLocalDate() == timestampDateTime.toLocalDate()) {
            DateTimeFormatter.ofPattern("HH:mm", locale)
        } else {
            val dayOffset = java.time.Duration.between(currentDateTime, timestampDateTime).toDays()
            if (dayOffset == 1L) {
                DateTimeFormatter.ofPattern("EEE", locale)
            } else {
                DateTimeFormatter.ofPattern("EEE d", locale)
            }
        }
        return timestampDateTime.format(formatter)
    }

    fun formatUtcDateTimeForLastSeen(
        timestamp: Long,
        locale: Locale = Locale.getDefault(),
        context: Context,
    ): String {
        val currentDateTime = LocalDateTime.now()
        val utcDateTime = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC)
        val timestampDateTime = utcDateTime.atZone(ZoneId.systemDefault())
        return if (currentDateTime.toLocalDate() == timestampDateTime.toLocalDate()) {
            val time = timestampDateTime.format(DateTimeFormatter.ofPattern("HH:mm", locale))
            context.getString(R.string.text_last_seen_today, time)
        } else {
            val dayOffset = java.time.Duration.between(currentDateTime, timestampDateTime).toDays()
            if (dayOffset == 1L) {
                val time = timestampDateTime.format(DateTimeFormatter.ofPattern("HH:mm", locale))
                context.getString(R.string.text_last_seen_yesterday, time)
            } else {
                val month = timestampDateTime.format(DateTimeFormatter.ofPattern("EEE d", locale))
                val time = timestampDateTime.format(DateTimeFormatter.ofPattern("HH:mm", locale))
                context.getString(R.string.text_last_seen_month, month, time)
            }
        }
    }

}

