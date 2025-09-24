package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample_app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.forvia.droidcon.common.theme.DeveloperWorkshopTheme
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.navigation.PushNotificationsNavigationGraph

class PushNotificationsSampleActivity : ComponentActivity() {

    companion object PNSA {
        const val TAG = "PushNotificationsSampleActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeveloperWorkshopTheme {
                PushNotificationsNavigationGraph()
            }
        }
    }
}
