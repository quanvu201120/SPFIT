package com.quanvu201120.supportfit.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.adapter.ViewpagerOnBoadingAdapter
import com.quanvu201120.supportfit.fragment.key_shared
import com.quanvu201120.supportfit.fragment.nameSharedPreferences
import me.relex.circleindicator.CircleIndicator
import me.relex.circleindicator.CircleIndicator2
import me.relex.circleindicator.CircleIndicator3

class OnBoardingActivity : AppCompatActivity() {

    lateinit var viewPager2: ViewPager2
    lateinit var indicator: CircleIndicator3
    lateinit var adapter: ViewpagerOnBoadingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        viewPager2 = findViewById(R.id.viewPagerOnbroading)
        indicator = findViewById(R.id.circleIndicator)

        var sharedPreferences = getSharedPreferences(nameSharedPreferences, Context.MODE_PRIVATE)
        var check = sharedPreferences.getBoolean(key_shared, false)

        if (check){
            var intent = Intent(this@OnBoardingActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        adapter = ViewpagerOnBoadingAdapter(supportFragmentManager,lifecycle)
        viewPager2.adapter = adapter

        indicator.setViewPager(viewPager2)



    }
}