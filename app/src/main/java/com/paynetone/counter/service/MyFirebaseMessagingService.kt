package com.paynetone.counter.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.paynetone.counter.R
import com.paynetone.counter.main.MainActivity
import com.paynetone.counter.utils.Constants
import com.paynetone.counter.utils.Constants.NOTIFICATION_COUNT
import com.paynetone.counter.utils.SharedPref


class MyFirebaseMessagingService : FirebaseMessagingService() {
//    private var broadcaster : LocalBroadcastManager?=null

    override fun onCreate() {
        super.onCreate()
//        broadcaster = LocalBroadcastManager.getInstance(this)
    }
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            val data = Bundle()
            for ((key, value) in remoteMessage.data) {
                data.putString(key, value)
            }

            val mNotificationManager =
                applicationContext.getSystemService(
                    NOTIFICATION_SERVICE
                ) as NotificationManager
            val channelId = getString(R.string.project_id)
//
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                handleNotificationAndroidO(mNotificationManager, channelId)
            }

            val title = data.getString("title") ?: ""
            val body = data.getString("body") ?: ""

            var currentId: Int =
                SharedPref.getInstance(applicationContext).getInt(NOTIFICATION_COUNT,0)
            currentId++
            SharedPref.getInstance(applicationContext).putInt(NOTIFICATION_COUNT,currentId);
            val context = applicationContext
            val defaultAction = Intent(context, MainActivity::class.java)
                .setAction(Intent.ACTION_DEFAULT)
                .putExtra(Constants.NOTIFICATION_DATA_TRANSFER, data)
            defaultAction.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK);
            val mBuilder = NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.mipmap.ic_app)
//                        .setColor(ContextCompat.getColor(this,R.color.colorMain))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setContentIntent(
                    PendingIntent.getActivity(
                        context,
                        currentId,
                        defaultAction,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
            mNotificationManager.notify(currentId,mBuilder.build())
//                    val intent = Intent("notify-receive-listener");
//                    broadcaster?.sendBroadcast(intent)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleNotificationAndroidO(
        notificationManager: NotificationManager,
        channelId: String
    ) {
        createNotificationChannel(notificationManager, channelId)
        notificationManager
            .getNotificationChannel(channelId)
            ?.canBypassDnd()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        notificationManager: NotificationManager,
        channelId: String
    ) {
        val notificationChannel =
            NotificationChannel(channelId, getString(R.string.app_name), IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

