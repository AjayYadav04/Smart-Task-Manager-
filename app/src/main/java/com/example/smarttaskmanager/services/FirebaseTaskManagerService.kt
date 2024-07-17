package com.example.smarttaskmanager.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.smarttaskmanager.R
import com.example.smarttaskmanager.ui.activities.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseTaskManagerService : FirebaseMessagingService() {

    val TAG = String::class.java.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage?.from}")
        remoteMessage?.data?.isNotEmpty()?.let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            if (remoteMessage.data.isNotEmpty()) {
                val msg: String = remoteMessage.data.get("message").toString()
                Log.d(TAG, "Message data message: " + remoteMessage.data.get("message").toString())
                sendNotification(msg)
            }
        }

//        remoteMessage?.notification?.let {
//            sendNotification(remoteMessage.notification?.body)
//        }
    }

    private fun sendNotification(messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val channelId = "FCM_CHANNEL_ID"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(messageBody)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }


}
