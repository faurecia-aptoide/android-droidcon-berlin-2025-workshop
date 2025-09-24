package com.forvia.droidcon.developerworkshop.presentation.mainscreen.viewstate

import com.forvia.droidcon.developerworkshop.data.model.SampleApplicationData

data class MainUiState(
    val appList: List<SampleApplicationData> = emptyList()
)
