package com.quanvu201120.supportfit.service

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.activity.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        var mapString : Map<String,String> = message.data

        var title = mapString.get("title")
        var strMessage = mapString.get("content")
        sendnotification(title,strMessage)

    }
    private fun sendnotification(title: String?, strMessage: String?) {

        var bitmap = resources.getDrawable(R.drawable.logo).toBitmap(50,50)

        var intent = Intent(this, MainActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_IMMUTABLE )

        var notification  = NotificationCompat.Builder(this, CHANNEL_SPFIT_ID)
            .setContentTitle(title)
            .setContentText(strMessage)
            .setSmallIcon(R.drawable.notifi_icon)
            .setColor(ContextCompat.getColor(this, R.color.red))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        var notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(1,notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
//        Log.e("token service", token)
    }

}