@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.VerticalDragHandle
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model.NotificationType
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model.PushNotificationDataModel
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.actions.MainAction
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.actions.MainEvent
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.composables.DetailContent
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.composables.MainContent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    snackBarHostState: SnackbarHostState,
) {
    var selectedNotificationTypeIndex: Int? by rememberSaveable { mutableStateOf(null) }
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>(
        scaffoldDirective = PaneScaffoldDirective.Default.copy(
            maxHorizontalPartitions = 2,
            horizontalPartitionSpacerSize = 16.dp,
            defaultPanePreferredWidth = (LocalWindowInfo.current.containerSize.width / 2).dp,
        ),
        adaptStrategies = ListDetailPaneScaffoldDefaults.adaptStrategies()
    )

    val scope = rememberCoroutineScope()

    val isListAndDetailVisible =
        navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded && navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

    val uiState = viewModel.mainUiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.onAction(MainAction.OnPageLoaded(context))
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is MainEvent.ShowMessage -> {
                    snackBarHostState.showSnackbar(event.text)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        navigator.navigateTo(ListDetailPaneScaffoldRole.List)
    }

    BackHandler(enabled = navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    SharedTransitionLayout {
        AnimatedContent(targetState = isListAndDetailVisible, label = "simple sample") { state ->
            ListDetailPaneScaffold(
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                listPane = {
                    val currentSelectedWordIndex = selectedNotificationTypeIndex
                    val isDetailVisible = selectedNotificationTypeIndex != null
                    AnimatedPane {
                        MainContent(
                            state = uiState,
                            selectionState = if (isDetailVisible && currentSelectedWordIndex != null) {
                                SelectionVisibilityState.ShowSelection(currentSelectedWordIndex)
                            } else {
                                SelectionVisibilityState.NoSelection
                            },
                            onIndexClick = { index ->
                                selectedNotificationTypeIndex = index
                                scope.launch {
                                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                                }
                            },
                            isListAndDetailVisible = isListAndDetailVisible,
                            isListVisible = !isDetailVisible,
                            animatedVisibilityScope = this@AnimatedPane,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            onAction = { action ->
                                viewModel.onAction(action)
                            }
                        )
                    }
                },
                detailPane = {
                    val isDetailVisible =
                        selectedNotificationTypeIndex != null
                    val (notificationType, notificationList) = if (selectedNotificationTypeIndex == 0) {
                        NotificationType.NOTIFICATION to uiState.pushNotificationList.filterIsInstance<PushNotificationDataModel.Notification>()
                    } else {
                        NotificationType.PAYLOAD_ONLY to uiState.pushNotificationList.filterIsInstance<PushNotificationDataModel.PayloadOnly>()
                    }
                    if (isDetailVisible) {
                        AnimatedPane {
                            DetailContent(
                                notificationType = notificationType,
                                notificationList = notificationList,
                            )
                        }
                    }
                },
                paneExpansionState = rememberPaneExpansionState(navigator.scaffoldValue),
                paneExpansionDragHandle = { state ->
                    val interactionSource =
                        remember { MutableInteractionSource() }
                    VerticalDragHandle(
                        modifier =
                            Modifier
                                .paneExpansionDraggable(
                                    state,
                                    LocalMinimumInteractiveComponentSize.current,
                                    interactionSource
                                )
                                .clickable {
                                    selectedNotificationTypeIndex = null
                                },
                        interactionSource = interactionSource,
                    )
                }
            )
        }
    }
}

/**
 * The description of the selection state for the [MainContent]
 */
sealed interface SelectionVisibilityState {

    /**
     * No selection should be shown, and each item should be clickable.
     */
    object NoSelection : SelectionVisibilityState

    /**
     * Selection state should be shown, and each item should be selectable.
     */
    data class ShowSelection(
        /**
         * The index of the word that is selected.
         */
        val selectedNotificationTypeIndex: Int
    ) : SelectionVisibilityState
}
