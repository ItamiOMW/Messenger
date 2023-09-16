package com.example.itami_chat.chat_feature.presentation.util

import android.content.Context
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.Message
import com.example.itami_chat.chat_feature.domain.model.MessageType


fun Message.getText(context: Context, myUserId: Int): String {
    return when (this.type) {
        MessageType.MESSAGE -> {
            if (!this.pictureUrls.isNullOrEmpty()) {
                context.getString(
                    R.string.text_pictures_count,
                    this.pictureUrls.count()
                )
            } else {
                this.text ?: ""
            }
        }

        MessageType.CHAT_CREATED -> {
            if (myUserId == this.authorId) {
                context.getString(
                    R.string.text_you_created_group_chat,
                )
            } else {
                context.getString(
                    R.string.text_user_created_group_chat,
                    this.authorFullName,
                )
            }

        }

        MessageType.CHAT_NAME_UPDATED -> {
            if (myUserId == this.authorId) {
                context.getString(
                    R.string.text_you_updated_chat_name,
                    this.text
                )
            } else {
                context.getString(
                    R.string.text_user_updated_chat_name,
                    this.authorFullName,
                    this.text,
                )
            }
        }

        MessageType.CHAT_PICTURE_UPDATED -> {
            if (myUserId == this.authorId) {
                context.getString(
                    R.string.text_you_updated_chat_picture
                )
            } else {
                context.getString(
                    R.string.text_user_updated_chat_picture,
                    this.authorFullName,
                )
            }

        }

        MessageType.INVITED -> {
            context.getString(
                R.string.text_user_was_invited,
                this.authorFullName,
            )
        }

        MessageType.LEFT -> {
            context.getString(
                R.string.text_user_left,
                this.authorFullName,
            )
        }

        MessageType.KICKED -> {
            context.getString(
                R.string.text_user_kicked,
                this.authorFullName,
            )
        }

        MessageType.ADMIN_ROLE_ASSIGNED -> {
            context.getString(
                R.string.text_admin_role_assigned,
                this.authorFullName,
            )
        }

        MessageType.ADMIN_ROLE_REMOVED -> {
            context.getString(
                R.string.text_admin_role_removed,
                this.authorFullName,
            )
        }
    }
}