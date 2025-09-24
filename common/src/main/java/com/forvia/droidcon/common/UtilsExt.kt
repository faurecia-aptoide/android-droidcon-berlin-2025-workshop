package com.forvia.droidcon.common

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun rememberLifecycleEvent(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current): Lifecycle.Event {
    var state by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            state = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    return state
}

@Composable
fun Modifier.hideKeyboardOnEnter(): Modifier {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    return this.onKeyEvent {
        if (it.key == Key.Enter && it.type == KeyEventType.KeyUp) {
            hideKeyboardAndRemoveFocus(keyboardController, focusManager)
            true
        } else {
            false
        }
    }
}

@Composable
fun hideKeyboardOnEnter(): KeyboardActions {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    return KeyboardActions(
        onNext = {
            val moved = focusManager.moveFocus(FocusDirection.Down)
            if (!moved) {
                hideKeyboardAndRemoveFocus(keyboardController, focusManager)
            }
        },
        onSearch = {
            hideKeyboardAndRemoveFocus(keyboardController, focusManager)
        },
        onDone = {
            hideKeyboardAndRemoveFocus(keyboardController, focusManager)
        }
    )
}

fun hideKeyboardAndRemoveFocus(
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager?,
) {
    keyboardController?.hide()
    focusManager?.clearFocus()
}
