package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.composables

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.SelectionVisibilityState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ItemCardWithCounter(
    index: Int,
    title: String,
    counter: Int,
    selectionState: SelectionVisibilityState,
    onIndexClick: (index: Int) -> Unit,
    isListAndDetailVisible: Boolean,
    isListVisible: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val interactionModifier = when (selectionState) {
        SelectionVisibilityState.NoSelection -> {
            Modifier.clickable(
                onClick = { onIndexClick(index) }
            )
        }

        is SelectionVisibilityState.ShowSelection -> {
            Modifier.selectable(
                selected = index == selectionState.selectedNotificationTypeIndex,
                onClick = { onIndexClick(index) }
            )
        }
    }

    val containerColor = when (selectionState) {
        SelectionVisibilityState.NoSelection -> MaterialTheme.colorScheme.surface
        is SelectionVisibilityState.ShowSelection ->
            if (index == selectionState.selectedNotificationTypeIndex) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surface
            }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .then(interactionModifier)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val imageModifier = Modifier.padding(horizontal = 8.dp)
            if (!isListAndDetailVisible && isListVisible) {
                with(sharedTransitionScope) {
                    val state = rememberSharedContentState(key = title)
                    imageModifier.then(
                        Modifier.sharedElement(
                            state,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                }
            }
            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Text(
                text = counter.toString(),
                modifier = Modifier
                    .padding(8.dp)
            )
        }

    }
}
