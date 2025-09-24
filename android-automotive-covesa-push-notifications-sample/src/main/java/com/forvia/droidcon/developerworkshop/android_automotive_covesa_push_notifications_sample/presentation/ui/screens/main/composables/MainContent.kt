package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.composables

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model.NotificationType
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model.PushNotificationDataModel
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.composables.RoundedTextField
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.SelectionVisibilityState
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.actions.MainAction
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.viewstate.MainUiState

/**
 * The content for the list pane.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainContent(
    state: MainUiState,
    onAction: (MainAction) -> Unit,
    selectionState: SelectionVisibilityState,
    onIndexClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    isListAndDetailVisible: Boolean,
    isListVisible: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val title = state.pushTitle
    val content = state.pushContent
    val payload = state.pushPayload
    val focus = LocalFocusManager.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .then(
                    when (selectionState) {
                        SelectionVisibilityState.NoSelection -> Modifier
                        is SelectionVisibilityState.ShowSelection -> Modifier.selectableGroup()
                    }
                )
                .widthIn(max = 800.dp)
        ) {
            stickyHeader {
                Text(
                    text = "Push Notifications Sample",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.animateContentSize()
                ) {
                    if (state.notificationTypeState == NotificationType.NOTIFICATION) {
                        RoundedTextField(
                            value = title,
                            onValueChange = {
                                onAction(MainAction.OnPushTitleUpdate(it))
                            },
                            placeholder = "Title",
                            singleLine = true,
                            trailingIcon = {
                                Icons.Default.Clear
                            }
                        )
                        RoundedTextField(
                            value = content,
                            onValueChange = {
                                onAction(MainAction.OnPushContentUpdate(it))
                            },
                            placeholder = "Content",
                            singleLine = false,
                            trailingIcon = {
                                Icons.Default.Clear
                            }
                        )
                    }

                    RoundedTextField(
                        value = payload,
                        onValueChange = {
                            onAction(MainAction.OnPushPayloadUpdate(it))
                        },
                        placeholder = "Payload",
                        singleLine = false,
                        trailingIcon = {
                            Icons.Default.Clear
                        },
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        ElevatedButton(
                            modifier = Modifier.weight(0.5f),
                            elevation = ButtonDefaults.buttonElevation(6.dp),
                            colors = ButtonDefaults.elevatedButtonColors().copy(
                                contentColor = Color.White
                            ),
                            enabled = state.buttonsEnabled,
                            onClick = {
                                if (state.notificationTypeState == NotificationType.NOTIFICATION) {
                                    onAction(
                                        MainAction.SendNotification(
                                            title = title,
                                            content = content,
                                            payload = payload
                                        )
                                    )
                                } else {
                                    onAction(MainAction.SendPayloadOnly(payload = payload))
                                }
                            }
                        ) {
                            Text(
                                if (state.notificationTypeState == NotificationType.NOTIFICATION) {
                                    "Send Notification"
                                } else {
                                    "Send Payload Only"
                                }
                            )
                        }

                        ElevatedButton(
                            modifier = Modifier.weight(0.5f),
                            elevation = ButtonDefaults.buttonElevation(6.dp),
                            enabled = state.buttonsEnabled,
                            colors = ButtonDefaults.elevatedButtonColors().copy(
                                contentColor = Color.White
                            ),
                            onClick = {
                                if (state.notificationTypeState == NotificationType.NOTIFICATION) {
                                    onAction(
                                        MainAction.GenerateCURLNotification(
                                            title = title,
                                            content = content,
                                            payload = payload,
                                        )
                                    )
                                } else {
                                    onAction(MainAction.GenerateCURLPayloadOnly(payload = payload))
                                }
                            }
                        ) {
                            Text(
                                if (state.notificationTypeState == NotificationType.NOTIFICATION) {
                                    "Generate Notification cURL"
                                } else {
                                    "Generate Payload cURL"
                                }
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Notification")
                            Switch(
                                checked = state.notificationTypeState == NotificationType.PAYLOAD_ONLY,
                                onCheckedChange = {
                                    onAction(MainAction.TogglePushNotificationType)
                                }
                            )
                            Text("Payload")
                        }
                    }
                }
            }

            item {
                ItemCardWithCounter(
                    index = 0,
                    title = "Notifications",
                    counter = state.pushNotificationList.filterIsInstance<PushNotificationDataModel.Notification>()
                        .count(),
                    selectionState = selectionState,
                    onIndexClick = onIndexClick,
                    isListAndDetailVisible = isListAndDetailVisible,
                    isListVisible = isListVisible,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            }

            item {
                ItemCardWithCounter(
                    index = 1,
                    title = "Payload Only",
                    counter = state.pushNotificationList.filterIsInstance<PushNotificationDataModel.PayloadOnly>()
                        .count(),
                    selectionState = selectionState,
                    onIndexClick = onIndexClick,
                    isListAndDetailVisible = isListAndDetailVisible,
                    isListVisible = isListVisible,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            }
        }
    }
}
