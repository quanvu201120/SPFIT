package com.quanvu201120.supportfit.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.activity.LoginActivity
import com.quanvu201120.supportfit.activity.MainActivity

val nameSharedPreferences = "SPFIT_Shared"
val key_shared = "OnBoarding"

class OnBoardingFragment3 : Fragment() {

    lateinit var button: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_on_boarding3, container, false)

        button = view.findViewById(R.id.btnStartOnboarding)




        button.setOnClickListener {
            var sharedPreferences =
                requireContext().getSharedPreferences(nameSharedPreferences, Context.MODE_PRIVATE)
            var edit = sharedPreferences.edit()
            edit.putBoolean(key_shared,true)
            edit.commit()

            var intent = Intent(requireContext(),LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view;
    }


}
