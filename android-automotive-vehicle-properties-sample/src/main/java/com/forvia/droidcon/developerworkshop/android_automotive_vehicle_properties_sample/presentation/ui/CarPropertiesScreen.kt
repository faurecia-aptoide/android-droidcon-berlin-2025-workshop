@file:OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSharedTransitionApi::class)

package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.actions.MainAction
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CarPropertiesScreen(
    viewModel: CarPropertiesViewModel = koinViewModel(),
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>(
        scaffoldDirective = PaneScaffoldDirective.Default.copy(
            maxHorizontalPartitions = 2,
            horizontalPartitionSpacerSize = 16.dp,
            defaultPanePreferredWidth = (LocalWindowInfo.current.containerSize.width / 2).dp,
        ),
        adaptStrategies = ListDetailPaneScaffoldDefaults.adaptStrategies()
    )
    val scope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val isListAndDetailVisible =
        navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded && navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

    LaunchedEffect(Unit) {
        navigator.navigateTo(ListDetailPaneScaffoldRole.List)
    }

    BackHandler(enabled = navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        SharedTransitionLayout(
            modifier = Modifier.padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = isListAndDetailVisible,
                label = "simple sample"
            ) { state ->
                ListDetailPaneScaffold(
                    directive = navigator.scaffoldDirective,
                    value = navigator.scaffoldValue,
                    listPane = {
                        AnimatedPane {
                            ListScreen(
                                viewState = uiState,
                                onAction = viewModel::onAction,
                            )
                        }
                    },
                    detailPane = {
                        uiState.selectedCarProperty?.let {
                            AnimatedPane {
                                DetailsScreen(
                                    carPropertiesDataModel = it
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
                                        viewModel.onAction(
                                            MainAction.OnCarPropertySelected(null)
                                        )
                                    },
                            interactionSource = interactionSource,
                        )
                    }
                )
            }
        }
    }
}
