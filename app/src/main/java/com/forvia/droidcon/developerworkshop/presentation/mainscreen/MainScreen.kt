package com.forvia.droidcon.developerworkshop.presentation.mainscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.forvia.droidcon.common.composables.ShowDialogData
import com.forvia.droidcon.developerworkshop.data.model.AppName
import com.forvia.droidcon.developerworkshop.presentation.mainscreen.actions.MainUiAction
import com.forvia.droidcon.developerworkshop.presentation.mainscreen.actions.MainUiEvent
import com.forvia.droidcon.developerworkshop.presentation.navigation.routes.MainNavigationRoutes
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    navigationController: NavHostController,
    viewModel: MainViewModel = koinViewModel(),
    showDialogState: MutableState<ShowDialogData>
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is MainUiEvent.ShowDialog -> {
                    showDialogState.value = ShowDialogData(
                        showDialog = true,
                        title = event.title,
                        text = event.message
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
    ) {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxSize()
        ) {
            items(uiState.appList) { app ->
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .clickable {
                            when (app.name) {
                                AppName.VEHICLE_PROPERTIES -> {
                                    MainNavigationRoutes.CarPropertiesScreen.route
                                }

                                AppName.AUDIO_WHILE_DRIVING -> {
                                    MainNavigationRoutes.AudioWhileDrivingSampleGraph.route
                                }

                                AppName.POI -> {
                                    null
                                }

                                AppName.PUSH_NOTIFICATIONS -> {
                                    MainNavigationRoutes.PushNotificationsNavigationSampleGraph.route
                                }
                            }?.let {
                                navigationController.navigate(it)
                            } ?: run {
                                viewModel.onAction(
                                    MainUiAction.NavigateToApp(
                                        context,
                                        app
                                    )
                                )
                            }
                        },
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(app.name.simpleName)
                    }
                }
            }
        }
    }
}
