package com.forvia.droidcon.developerworkshop.presentation.mainscreen.actions

import android.content.Context
import com.forvia.droidcon.developerworkshop.data.model.SampleApplicationData

interface MainUiAction {
    data class NavigateToApp(
        val context: Context,
        val sampleApplicationData: SampleApplicationData
    ) : MainUiAction
}
