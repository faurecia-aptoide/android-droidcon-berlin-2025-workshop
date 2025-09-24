package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation

sealed interface PlayerAction {
    object PlayPause : PlayerAction
    object ShowControls : PlayerAction
    object HideControls : PlayerAction
    data class SeekTo(val millis: Long) : PlayerAction
    object Close : PlayerAction
    object SeekForward : PlayerAction
    object SeekBackward : PlayerAction
}
