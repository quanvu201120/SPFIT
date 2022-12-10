package com.quanvu201120.supportfit.activity

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.model.UserModel

class CreateSingleAccountActivity : AppCompatActivity() {

    lateinit var edt_email_create_single : EditText
    lateinit var edt_name_create_single : EditText
    lateinit var btn_dangky_create_single : Button
    lateinit var btn_cancel_create_single : Button
    lateinit var progressBar_create_single: ProgressBar

    lateinit var auth : FirebaseAuth
    lateinit var fireStore : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_single_account)

        edt_email_create_single = findViewById(R.id.edt_email_create_single)
        edt_name_create_single = findViewById(R.id.edt_name_create_single)
        btn_dangky_create_single = findViewById(R.id.btn_create_create_single)
        progressBar_create_single = findViewById(R.id.progress_create_single)
        btn_cancel_create_single = findViewById(R.id.btn_cancel_create_single)

        auth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()

        btn_cancel_create_single.setOnClickListener {
            finish()
        }

        btn_dangky_create_single.setOnClickListener {

            var email = edt_email_create_single.text.toString().trim()
            var name = edt_name_create_single.text.toString().trim()
//            var pass1 = edt_pass_dangky.text.toString().trim()
//            var pass2 = edt_nhaplaipass_dangky.text.toString().trim()

            if (email.isEmpty()) {
                edt_email_create_single.setError("Vui lòng nhập email")
            }
            if (name.isEmpty()) {
                edt_name_create_single.setError("Vui lòng nhập email")
            }



            if (!email.isEmpty() && !name.isEmpty()) {

                var sharedPreferences : SharedPreferences = getSharedPreferences("ACCOUNT", Activity.MODE_PRIVATE)

                val getEmail : String? = sharedPreferences.getString(STR_EMAIL,null)
                val getPass : String? = sharedPreferences.getString(STR_PASS,null)
                val save : Boolean = sharedPreferences.getBoolean(BL_SAVE,false)


//                var  editor = sharedPreferences.edit()
//                editor.clear().commit()

                btn_dangky_create_single.visibility = View.GONE
                btn_cancel_create_single.visibility = View.GONE
                progressBar_create_single.visibility = View.VISIBLE

                auth.createUserWithEmailAndPassword(email, PASS_DEFAULT)
                    .addOnSuccessListener {

                        val uid = auth.currentUser!!.uid
                        val user = UserModel(userId = uid, name = name, email = email)
                        fireStore.collection(C_USER).document(uid)
                            .set(user)
                            .addOnSuccessListener {
                                auth.currentUser!!.sendEmailVerification()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            auth.signInWithEmailAndPassword(getEmail!!, getPass!!)
                                                .addOnSuccessListener {
                                                    Toast.makeText(this,"Tạo tài khoản thành công", Toast.LENGTH_SHORT).show()
                                                    finish()
                                                }
                                        }
                                    }

                            }
                    }
                    .addOnFailureListener {

                        btn_dangky_create_single.visibility = View.VISIBLE
                        btn_cancel_create_single.visibility = View.VISIBLE
                        progressBar_create_single.visibility = View.GONE

                        val e_format = "The email address is badly formatted."
                        val e_exists =
                            "The email address is already in use by another account."

                        if (it.message == e_exists) {
                            Toast.makeText(this,
                                "Email đã được sử dụng",
                                Toast.LENGTH_SHORT).show()
                        } else if (it.message == e_format) {
                            Toast.makeText(this, "Email sai định dạng", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }



            }

        }

    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

}