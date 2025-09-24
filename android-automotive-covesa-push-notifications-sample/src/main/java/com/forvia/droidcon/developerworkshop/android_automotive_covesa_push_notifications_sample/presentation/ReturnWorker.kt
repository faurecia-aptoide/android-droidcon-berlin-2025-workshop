package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.forvia.droidcon.common.R

/**
 * A [CoroutineWorker] that creates and displays a notification to allow the user to return to the app.
 *
 * This worker is typically scheduled to run after a certain period of inactivity or when a specific event occurs,
 * prompting the user to re-engage with the application.
 *
 * @param appContext The application [Context].
 * @param params [WorkerParameters] for configuring the worker.
 */
class ReturnWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    /**
     * This method is called to do the actual work.
     * It creates a notification channel (if it doesn't exist) and then builds and displays a notification
     * that, when tapped, will bring the user back to the [com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample_app.presentation.PushNotificationsSampleActivity].
     *
     * @return [Result.success] if the notification was successfully displayed, otherwise another [Result] indicating failure.
     */
    override suspend fun doWork(): Result {
        val channelId = "return_channel"
        val nm = applicationContext.getSystemService(NotificationManager::class.java)

        if (nm.getNotificationChannel(channelId) == null) {
            nm.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "Return to App",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }

        val contentPi = launcherPendingIntent(context = applicationContext)

        val n = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // your icon
            .setContentTitle("Back to the workshop app")
            .setContentText("Tap to return")
            .setContentIntent(contentPi)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        nm.notify(42, n)
        return Result.success()
    }

    private fun launcherPendingIntent(context: Context): PendingIntent? {
        // Try the package's declared launcher activity
        val launch = context.packageManager.getLaunchIntentForPackage(context.packageName)

        // Fallback: generic MAIN/LAUNCHER within this package
        val fallback = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            setPackage(context.packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val intent = launch ?: fallback

        // Ensure there is something to resolve
        if (intent.resolveActivity(context.packageManager) == null) return null

        return PendingIntent.getActivity(
            context,
            /* requestCode = */ 2001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
