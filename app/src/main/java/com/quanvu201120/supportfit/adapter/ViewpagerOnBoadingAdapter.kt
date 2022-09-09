package com.quanvu201120.supportfit.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.quanvu201120.supportfit.fragment.OnBoardingFragment1
import com.quanvu201120.supportfit.fragment.OnBoardingFragment2
import com.quanvu201120.supportfit.fragment.OnBoardingFragment3

class ViewpagerOnBoadingAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    lateinit var fragment : Fragment

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {fragment = OnBoardingFragment1()}
            1 -> {fragment = OnBoardingFragment2()}
            2 -> {fragment = OnBoardingFragment3()}
        }
        return fragment
    }
}