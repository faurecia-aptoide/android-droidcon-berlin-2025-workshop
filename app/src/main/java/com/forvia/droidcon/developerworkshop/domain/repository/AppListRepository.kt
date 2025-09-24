package com.forvia.droidcon.developerworkshop.domain.repository

import com.forvia.droidcon.developerworkshop.data.model.SampleApplicationData

interface AppListRepository {
    fun getAppList(): List<SampleApplicationData>
}
