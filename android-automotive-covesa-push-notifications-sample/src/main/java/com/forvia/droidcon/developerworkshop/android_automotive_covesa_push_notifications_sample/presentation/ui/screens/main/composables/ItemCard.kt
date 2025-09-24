package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.composables

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model.PushNotificationDataModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ItemCard(
    pushNotificationModel: PushNotificationDataModel,
    modifier: Modifier = Modifier
) {
    val containerColor = MaterialTheme.colorScheme.surfaceVariant

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            when (pushNotificationModel) {
                is PushNotificationDataModel.Notification -> {
                    Text(
                        text = "Title: ${pushNotificationModel.title}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                    )
                    Text(
                        text = "Body: ${pushNotificationModel.body}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                    )
                    Text(
                        text = "Payload: ${pushNotificationModel.payload}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                    )
                }

                is PushNotificationDataModel.PayloadOnly -> {
                    Text(
                        text = "Payload: ${pushNotificationModel.payload}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                    )
                }
            }

        }

    }
}
