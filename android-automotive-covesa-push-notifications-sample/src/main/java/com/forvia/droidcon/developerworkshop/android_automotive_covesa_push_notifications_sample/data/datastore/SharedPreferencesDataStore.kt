package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.sharedPreferencesDataStore by preferencesDataStore(name = SAHRED_PREFS_DATA_STORE_FILE)

const val SAHRED_PREFS_DATA_STORE_FILE = "sharedPreferencesDataStore"
