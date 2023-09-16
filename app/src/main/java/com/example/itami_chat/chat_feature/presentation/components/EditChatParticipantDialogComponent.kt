package com.example.itami_chat.chat_feature.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant
import com.example.itami_chat.chat_feature.domain.model.ParticipantRole
import com.example.itami_chat.core.presentation.components.IconToTextComponent
import com.example.itami_chat.core.presentation.ui.theme.padding


@Composable
fun EditChatParticipantDialogComponent(
    chatParticipantToEdit: ChatParticipant,
    me: ChatParticipant,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    onAssignAdminRole: () -> Unit,
    onRemoveAdminRole: () -> Unit,
    onKickFromChat: () -> Unit,
    onDismiss: () -> Unit,
) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(),
    ) {
        Card(
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                when (me.role) {
                    ParticipantRole.MEMBER -> {
                        Unit
                    }

                    ParticipantRole.ADMIN -> {
                        if (chatParticipantToEdit.role == ParticipantRole.MEMBER) {
                            IconToTextComponent(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onKickFromChat()
                                    }
                                    .padding(
                                        bottom = MaterialTheme.padding.medium,
                                        top = MaterialTheme.padding.medium
                                    ),
                                text = stringResource(id = R.string.text_kick_user_from_chat),
                                icon = painterResource(id = R.drawable.ic_delete),
                                color = MaterialTheme.colorScheme.error,
                                textStyle = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    ParticipantRole.CREATOR -> {
                        IconToTextComponent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (chatParticipantToEdit.role == ParticipantRole.MEMBER) {
                                        onAssignAdminRole()
                                    } else {
                                        onRemoveAdminRole()
                                    }
                                }
                                .padding(
                                    bottom = MaterialTheme.padding.medium,
                                    top = MaterialTheme.padding.medium
                                ),
                            text = if (chatParticipantToEdit.role == ParticipantRole.ADMIN) {
                                stringResource(id = R.string.text_remove_admin_role)
                            } else {
                                stringResource(id = R.string.text_assign_admin_role)
                            },
                            icon = painterResource(id = R.drawable.ic_admin),
                            color = MaterialTheme.colorScheme.onSurface,
                            textStyle = MaterialTheme.typography.bodyLarge
                        )
                        IconToTextComponent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onKickFromChat()
                                }
                                .padding(
                                    bottom = MaterialTheme.padding.medium,
                                    top = MaterialTheme.padding.medium
                                ),
                            text = stringResource(id = R.string.text_kick_user_from_chat),
                            icon = painterResource(id = R.drawable.ic_delete),
                            color = MaterialTheme.colorScheme.error,
                            textStyle = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}