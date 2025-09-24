package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.forvia.droidcon.common.rememberLifecycleEvent
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.model.CarConnectState
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.actions.MainAction
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.composables.CarPropertyCard
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.composables.SearchInput
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.viewstate.CarPropertiesViewState

@Composable
fun ListScreen(
//    innerPaddingValues: PaddingValues,
    viewState: CarPropertiesViewState,
    onAction: (MainAction) -> Unit,
) {
    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_DESTROY) {
            onAction(MainAction.OnDisconnectCar)
        }
    }

    LaunchedEffect(viewState.carConnectionStatus) {
        if (viewState.carConnectionStatus == CarConnectState.CONNECTED) {
            onAction(MainAction.OnGetCarProperties)
        } else {
            onAction(MainAction.OnDisconnectCar)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(45.dp))
            Button(
                onClick = {
                    if (viewState.carConnectionStatus == CarConnectState.CONNECTED) {
                        onAction(MainAction.OnDisconnectCar)
                    } else {
                        onAction(MainAction.OnConnectCar)
                    }
                },
                enabled = viewState.carConnectionStatus != CarConnectState.NOT_AVAILABLE
            ) {
                Text(
                    text =
                        if (viewState.carConnectionStatus == CarConnectState.CONNECTED)
                            "Disconnect"
                        else
                            "Connect"
                )
            }
            Spacer(modifier = Modifier.width(32.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Connection status:"
                )
                Icon(
                    imageVector = Icons.Filled.Circle,
                    contentDescription = "Connected",
                    tint = when (viewState.carConnectionStatus) {
                        CarConnectState.CONNECTED -> Color.Green
                        CarConnectState.CONNECTING -> Color.Yellow
                        CarConnectState.DISCONNECTED -> Color.Red
                        CarConnectState.NOT_AVAILABLE -> Color.Black
                    }
                )

            }
        }

        if (viewState.carConnectionStatus == CarConnectState.CONNECTED) {
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                modifier = Modifier.padding(horizontal = 45.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SearchInput(
                    modifier = Modifier.weight(1f),
                    searchQuery = viewState.queryString,
                    onAction = onAction
                )
                Spacer(modifier = Modifier.width(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Switch(
                        checked = viewState.showOnlyImplemented,
                        onCheckedChange = { onAction(MainAction.OnShowOnlyImplementedToggle(it)) }
                    )
                    Text(text = "Only implemented")
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                items(
                    items = viewState.carProperties,
                    key = { item -> item.propertyIdentifier },
                    itemContent = { item ->
                        CarPropertyCard(
                            onAction = onAction,
                            carPropertiesDataModel = item,
                            isSelected = viewState.selectedCarProperty?.propertyIdentifier == item.propertyIdentifier
                        )
                    })
            }
        }
    }
}
