package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.ui.listscreen.actions

sealed interface MainEvent {
    data class ShowMessage(val title: String, val message: String) : MainEvent
}
