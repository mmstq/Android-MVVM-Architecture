package com.mmstq.mduarchive.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mmstq.mduarchive.R

class FCM : FirebaseMessagingService() {


    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("Data", message.data.toString())
        Log.d("Notification", message.notification?.body.orEmpty())
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(message.data["link"]))
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val action =
            NotificationCompat.Action.Builder(R.drawable.message, "View", pendingIntent).build()

        val notification = NotificationCompat.Builder(this, R.string.default_notification_channel_id.toString())
            .setSmallIcon(R.drawable.message)
            .setContentText(message.notification?.body.orEmpty())
            .setContentTitle(message.notification?.title)
            .setContentIntent(pendingIntent)
            .addAction(action)
            .setAutoCancel(true)
            .build()

        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(R.string.default_notification_channel_id.toString(), "notice", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(1, notification)
    }

    override fun onNewToken(p0: String) {
        Log.d("Token", p0)
        super.onNewToken(p0)
    }
}