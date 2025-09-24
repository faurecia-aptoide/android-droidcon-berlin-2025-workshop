package com.forvia.droidcon.common.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp

@Composable
fun InfoDialog(
    showDialogState: MutableState<ShowDialogData>,
) {
    val dismissRequest = { showDialogState.value = ShowDialogData() }
    with(showDialogState.value) {
        if (showDialog) {
            AlertDialog(
                tonalElevation = 16.dp,
                onDismissRequest = dismissRequest,
                title = {
                    Text(text = title)
                },
                text = {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    Button(
                        onClick = dismissRequest
                    ) {
                        Text("Ok")
                    }
                },
            )
        }
    }
}

data class ShowDialogData(
    val showDialog: Boolean = false,
    val title: String = "",
    val text: String = "",
)
