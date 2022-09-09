package com.quanvu201120.supportfit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.model.CmtModel
import com.quanvu201120.supportfit.model.NotifyModel
import com.quanvu201120.supportfit.model.PostModel
import com.quanvu201120.supportfit.model.UserModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("abc errorr", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.e("token main", token)
        })


    }
}

