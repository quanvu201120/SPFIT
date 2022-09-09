package com.quanvu201120.supportfit.service

import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.quanvu201120.supportfit.R

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        var mapString : Map<String,String> = message.data

        var title = mapString.get("title")
        var strMessage = mapString.get("content")
        sendnotification(title,strMessage)

    }
    private fun sendnotification(title: String?, strMessage: String?) {
        var notification  = NotificationCompat.Builder(this, CHANNEL_SPFIT_ID)
            .setContentTitle(title)
            .setContentText(strMessage)
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

        var notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(1,notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("token service", token)
    }

}