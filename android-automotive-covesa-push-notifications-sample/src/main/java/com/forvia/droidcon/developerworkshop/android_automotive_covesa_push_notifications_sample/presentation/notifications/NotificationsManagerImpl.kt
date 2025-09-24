package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.RemoteException
import com.forvia.droidcon.common.R
import java.util.concurrent.ThreadLocalRandom

/**
 * Implementation of [NotificationsManager] for displaying notifications.
 *
 * @property context The application context.
 */
class NotificationsManagerImpl(
    private val context: Context
) : NotificationsManager {


    private val channelId = context.packageName
    private val nManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    /**
     * Creates a notification channel if it doesn't already exist.
     */
    override fun createNotificationChannel() {
        val name = context.packageName
        val descriptionText = "Show test notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        try {
            val notificationChannel = nManager.getNotificationChannel(channelId)
            if (notificationChannel == null) {
                nManager.createNotificationChannel(channel)
            }
        } catch (e: RemoteException) {
            nManager.createNotificationChannel(channel)
        }
    }

    /**
     * Shows a notification with the given title and message.
     *
     * @param title The title of the notification.
     * @param message The message body of the notification.
     */
    override fun showNotification(title: String?, message: String?) {
        createNotificationChannel()
        val notificationBuilder = Notification.Builder(context, channelId)
        val notification =
            notificationBuilder
                .setChannelId(channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build()

        val notificationId = ThreadLocalRandom.current().nextInt()
        nManager.notify(notificationId, notification)
    }
}
