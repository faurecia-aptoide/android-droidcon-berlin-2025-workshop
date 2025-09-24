package com.forvia.droidcon.common.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.compose.runtime.mutableStateOf
import androidx.core.view.WindowInsetsControllerCompat

class ImmersiveModeController {
    private val _immersiveModeEnabled = mutableStateOf(false)
    val immersiveModeEnabled: Boolean
        get() = _immersiveModeEnabled.value

    @SuppressLint("WrongConstant")
    fun toggleImmersiveMode(activity: Activity?, view: View) {
        activity?.window?.let { window ->
            _immersiveModeEnabled.value = !immersiveModeEnabled
            val barsToHide: Int
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                barsToHide = WindowInsets.Type.systemBars()
                val controller = WindowInsetsControllerCompat(window, view)
                if (immersiveModeEnabled) {
                    controller.hide(barsToHide)
                    controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                } else {
                    controller.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars() or WindowInsets.Type.systemBars())
                }
            } else {
                val decorView = window.decorView
                barsToHide = View.SYSTEM_UI_FLAG_FULLSCREEN
                if (immersiveModeEnabled) {
                    decorView.systemUiVisibility = (barsToHide)
                } else {
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                }
            }
        }
    }
}