package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.composables

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model.NotificationType
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model.PushNotificationDataModel

/**
 * The content for the detail pane.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailContent(
    notificationType: NotificationType,
    notificationList: List<PushNotificationDataModel>?,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            Text(
                text = notificationType.title,
                style = MaterialTheme.typography.titleLarge
            )
        }
        items(notificationList ?: emptyList()) { item ->
            ItemCard(item)
        }
    }
}
