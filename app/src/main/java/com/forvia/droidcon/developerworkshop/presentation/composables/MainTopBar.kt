package com.forvia.droidcon.developerworkshop.presentation.composables

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.forvia.droidcon.common.composables.ShowDialogData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    showDialogState: MutableState<ShowDialogData>,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        title = {

        },
        actions = {
            Button(onClick = {
                showDialogState.value = ShowDialogData(
                    showDialog = true,
                    title = "Install all apps",
                    text = "In order to install all apps, run the following gradle task: \n\n ./gradlew :app:installAllApps",
                )
            }) {
                Text(
                    text = "Install standalone apps"
                )
            }
        }
    )
}
