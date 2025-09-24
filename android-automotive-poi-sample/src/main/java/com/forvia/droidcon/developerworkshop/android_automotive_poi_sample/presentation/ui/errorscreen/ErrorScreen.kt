package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.ui.errorscreen

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.Template

class ErrorScreen(
    carContext: CarContext,
    private val message: String,
    private val title: String,
    private val actionTitle: String? = null,
    private val onAction: ((Screen) -> Unit)?,
) : Screen(carContext) {
    override fun onGetTemplate(): Template {
        return MessageTemplate.Builder(message)
            .setTitle(title)
            .setHeaderAction(Action.BACK)
            .addAction(
                Action.Builder()
                    .setTitle(actionTitle ?: "Go back")
                    .setOnClickListener {
                        onAction?.invoke(this) ?: run {
                            Action.BACK
                        }
                    }
                    .build()
            )
            .build()
    }
}
