package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.navigation

import androidx.lifecycle.ViewModel
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases.GetSetupStateUseCase

class NavigationViewModel(
    getSetupStateUseCase: GetSetupStateUseCase
) : ViewModel() {
    val isSetupComplete = getSetupStateUseCase()
}
