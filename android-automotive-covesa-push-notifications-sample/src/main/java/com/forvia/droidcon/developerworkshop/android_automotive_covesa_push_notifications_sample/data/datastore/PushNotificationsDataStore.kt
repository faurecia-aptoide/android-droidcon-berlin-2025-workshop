package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.pushNotificationsDataStore by preferencesDataStore(name = DATA_STORE_FILE)

const val DATA_STORE_FILE = "pushNotificationsDataStore"
