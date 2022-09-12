package com.quanvu201120.supportfit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.model.*
import com.quanvu201120.supportfit.service.ICallApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getTokenMessage()

    }
}

fun getTokenMessage()  {
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.e("token error", "Fetching FCM registration token failed" + task.exception)
            return@OnCompleteListener
        }
        Log.e("token success", task.result)
    })
}


fun SendNotificationAPI(){
    val retrofit = Retrofit.Builder()
        .baseUrl("https://fcm.googleapis.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var apiService = retrofit.create(ICallApi::class.java)

    var token = ArrayList<String>()
    token.add("fYBst1xQTESOiKYDhKgGyC:APA91bE1VmjxX-3i38BYXYVePH3UgVpD8fc14qK9xbK05Kbw-mMZS-WtM9nRjwUpseed3oJnqMl37dkCD3wiV7GVtUnfWi7vJbcSCv_aV1fQEQX1rl_Ttp2i_usL7XzUHfrgom-uVvpK")
    var body = BodyApi(registration_ids = token)

    apiService.sendNotifyApi(body)
        .enqueue(object : Callback<ResultApiModel> {
            override fun onResponse(call: Call<ResultApiModel>, response: Response<ResultApiModel>) {
                if (response.code() == 200){
                    var resultApiModel : ResultApiModel = response.body()!!

                    Log.e("result", resultApiModel.toString() )

                }
                Log.e("status", "" + response.code() )
            }

            override fun onFailure(call: Call<ResultApiModel>, t: Throwable) {

            }
        })
}
