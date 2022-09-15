package com.quanvu201120.supportfit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import com.google.firebase.messaging.FirebaseMessaging
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.fragment.HomeFragment
import com.quanvu201120.supportfit.fragment.MyPostFragment
import com.quanvu201120.supportfit.model.*
import com.quanvu201120.supportfit.service.ICallApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNavigationView)

        showFragment(R.id.home_item_bottom_nav)
        bottomNav.setOnItemSelectedListener {
            showFragment(it.itemId)
            true
        }



    }
/////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////
    fun showFragment(itemId : Int){

        var fragment  = Fragment()

        when(itemId){
            R.id.home_item_bottom_nav -> fragment = HomeFragment()
            R.id.myPost_item_bottom_nav -> fragment = MyPostFragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutMain,fragment)
            .commit()

    }
    override fun onBackPressed() {
        //super.onBackPressed()
        finish()
    }

}

//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////
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

fun GetCurrentTimeFirebase() : ArrayList<Int>{
    var timestamp = Timestamp.now().toDate()
    val dateTimeStamp = Date(timestamp.time)
    val sdf = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
    sdf.timeZone = TimeZone.getTimeZone("GMT+07:00")
    val formattedDate = sdf.format(dateTimeStamp)
    val result = formattedDate.split("-").map { it.toInt() }
    Log.e("ABC time", " " + timestamp + " , " + result.toString())
    return result as ArrayList<Int>
}

fun GenerateId() : String {
    var id = mUser.userId + Date().time
    return id
}

fun sortDateCreate(){

}