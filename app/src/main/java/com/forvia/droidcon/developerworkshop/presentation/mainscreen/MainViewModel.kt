package com.forvia.droidcon.developerworkshop.presentation.mainscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import com.forvia.droidcon.developerworkshop.data.model.SampleApplicationData
import com.forvia.droidcon.developerworkshop.domain.usecases.GetAppListUseCase
import com.forvia.droidcon.developerworkshop.presentation.launchPackageWithComponent
import com.forvia.droidcon.developerworkshop.presentation.mainscreen.actions.MainUiAction
import com.forvia.droidcon.developerworkshop.presentation.mainscreen.actions.MainUiEvent
import com.forvia.droidcon.developerworkshop.presentation.mainscreen.viewstate.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the main screen, responsible for managing the UI state and handling user actions.
 *
 * @property getAppListUseCase The use case for retrieving the list of sample applications.
 */
class MainViewModel(
    private val getAppListUseCase: GetAppListUseCase
) : ViewModel() {

    /**
     * The current UI state of the main screen.
     */
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    /**
     * A flow of UI events that can be observed by the UI.
     */
    private val _events = MutableStateFlow<MainUiEvent?>(null)
    val events: StateFlow<MainUiEvent?> = _events

    /**
     * Initializes the ViewModel by fetching the list of sample applications and updating the UI state.
     */
    init {
        _uiState.update {
            it.copy(
                appList = getAppListUseCase()
            )
        }
    }

    /**
     * Handles user actions from the main screen.
     *
     * @param mainUiAction The user action to handle.
     */
    fun onAction(mainUiAction: MainUiAction) {
        when (mainUiAction) {
            is MainUiAction.NavigateToApp -> {
                navigateToApp(mainUiAction.context, mainUiAction.sampleApplicationData)
            }
        }
    }

    /**
     * Navigates to the selected sample application.
     *
     * @param context The application context.
     * @param sampleApplicationData The data of the sample application to navigate to.
     */
    private fun navigateToApp(context: Context, sampleApplicationData: SampleApplicationData) {
        context.launchPackageWithComponent(
            sampleApplicationData.packageName,
        ) {
            _events.update {
                MainUiEvent.ShowDialog(
                    title = "Error launching app: missing package",
                    message = "In order to install POI Sample App, run the following command: \n \n ./gradlew :app:installPoiSampleApp"
                )
            }
        }
    }
}
