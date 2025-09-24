package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.forvia.droidcon.common.hideKeyboardAndRemoveFocus
import com.forvia.droidcon.common.hideKeyboardOnEnter

@Composable
fun RoundedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    singleLine: Boolean = true,
    trailingIcon: @Composable (() -> ImageVector)
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier
            .fillMaxWidth()
            .hideKeyboardOnEnter(),
        shape = RoundedCornerShape(12.dp), // Rounded corners
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3,
        trailingIcon = {
            if (value.isNotEmpty()) {
                Icon(
                    imageVector = trailingIcon.invoke(),
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        onValueChange("")
                        hideKeyboardAndRemoveFocus(
                            keyboardController,
                            focusManager,
                        )
                    }
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = hideKeyboardOnEnter(),
    )
}
