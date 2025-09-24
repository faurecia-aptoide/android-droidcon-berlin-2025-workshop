package com.forvia.droidcon.developerworkshop.domain.repository

import com.forvia.droidcon.developerworkshop.data.model.AppName
import com.forvia.droidcon.developerworkshop.data.model.SampleApplicationData

/**
 * Implementation of [AppListRepository] that provides a static list of sample applications.
 */
class AppListRepositoryImpl : AppListRepository {
    /**
     * Retrieves a list of sample applications.
     *
     * @return A list of [SampleApplicationData] objects.
     */
    override fun getAppList(): List<SampleApplicationData> =
        listOf(
            SampleApplicationData(
                AppName.VEHICLE_PROPERTIES,
                "com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample",
            ),
            SampleApplicationData(
                AppName.AUDIO_WHILE_DRIVING,
                "com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample",
            ),
            SampleApplicationData(
                AppName.POI,
                "com.forvia.droidcon.developerworkshop.android_automotive_poi_sample",
            ),
            SampleApplicationData(
                AppName.PUSH_NOTIFICATIONS,
                "com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample",
            )
        )
}
