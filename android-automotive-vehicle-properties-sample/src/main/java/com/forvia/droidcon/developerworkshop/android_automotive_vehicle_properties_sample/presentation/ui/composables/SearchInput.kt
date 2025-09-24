package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.forvia.droidcon.common.hideKeyboardAndRemoveFocus
import com.forvia.droidcon.common.hideKeyboardOnEnter
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.actions.MainAction


@Composable
fun SearchInput(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onAction: (MainAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        label = {
            Text(text = "Search for property name or id")
        },
        value = searchQuery,
        onValueChange = {
            onAction(MainAction.OnSearchQueryChanged(it))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                Icon(
                    modifier = Modifier.clickable {
                        onAction(MainAction.OnSearchQueryChanged(""))
                        hideKeyboardAndRemoveFocus(
                            keyboardController,
                            focusManager,
                        )
                    },
                    imageVector = Icons.Default.Close, contentDescription = "Search Icon"
                )
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(25.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = hideKeyboardOnEnter(),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .hideKeyboardOnEnter()
    )
}
