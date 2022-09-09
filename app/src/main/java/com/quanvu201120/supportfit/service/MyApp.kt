package com.quanvu201120.supportfit.service

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.quanvu201120.supportfit.R

val CHANNEL_SPFIT_ID = "com.quanvu201120.supportfit"

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var channel_name : String = getString(R.string.channelName)
            var channel_description : String = getString(R.string.channelDescription)

            var channel_importance : Int = NotificationManager.IMPORTANCE_HIGH

            var channel = NotificationChannel(CHANNEL_SPFIT_ID, channel_name, channel_importance)
            channel.description = channel_description

            var notificationManagerCompat = NotificationManagerCompat.from(this)
            notificationManagerCompat.createNotificationChannel(channel)
        }
    }
}