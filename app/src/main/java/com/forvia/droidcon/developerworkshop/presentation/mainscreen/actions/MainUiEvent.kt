package com.forvia.droidcon.developerworkshop.presentation.mainscreen.actions

interface MainUiEvent {
    data class ShowDialog(val title: String, val message: String) : MainUiEvent
}
