package com.quanvu201120.supportfit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.model.UserModel
import kotlin.random.Random

val C_USER = "USER"
val C_POSTS = "POSTS"
val C_CMT = "CMT"
val C_NOTIFY = "NOTIFY"

val PASS_DEFAULT = "verification"

class RegisterActivity : AppCompatActivity() {


    lateinit var edt_code_dangky : EditText
    lateinit var tv_getCode_dangky : TextView
    lateinit var edt_email_dangky : EditText
    lateinit var edt_name_dangky : EditText
    lateinit var btn_dangky_dangky : Button
    lateinit var progressBar: ProgressBar

    lateinit var auth : FirebaseAuth
    lateinit var fireStore : FirebaseFirestore

    var str_Ma = "qwerEFtyuioRTYUISDpasdxZXCVBcvbnmQWGHJKfg12345OPA67890hjklzLNM"
    var maXacThuc = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edt_code_dangky = findViewById(R.id.edt_code_dangky)
        tv_getCode_dangky = findViewById(R.id.tv_getCode_dangky)
        edt_email_dangky = findViewById(R.id.edt_email_dangky)
        edt_name_dangky = findViewById(R.id.edt_name_dangky)
        btn_dangky_dangky = findViewById(R.id.btn_dangky_dangky)
        progressBar = findViewById(R.id.progress_dangky)

        auth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()


        btn_dangky_dangky.setOnClickListener {

            var email = edt_email_dangky.text.toString().trim()
            var name = edt_name_dangky.text.toString().trim()
            var code = edt_code_dangky.text.toString().trim()
//            var pass1 = edt_pass_dangky.text.toString().trim()
//            var pass2 = edt_nhaplaipass_dangky.text.toString().trim()

            var check = false

            if (email.isEmpty()) {
                edt_email_dangky.setError("Vui lòng nhập email")
            }
            if (name.isEmpty()) {
                edt_name_dangky.setError("Vui lòng nhập email")
            }
            if (code.isEmpty()) {
                edt_code_dangky.setError("Vui lòng nhập")
            }
            else{
                if(code.equals(maXacThuc)){
                    check = true
                }
                else{
                    edt_code_dangky.setError("Mã sai")
                }
            }




            if (!email.isEmpty() && !name.isEmpty() && check == true) {



                btn_dangky_dangky.visibility = View.GONE
                progressBar.visibility = View.VISIBLE

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
                                            Toast.makeText(this,
                                                "Đăng ký thành công\nVui lòng vào email kích hoạt tài khoản",
                                                Toast.LENGTH_LONG).show()
                                            auth.signOut()
                                            finish()
                                        }
                                    }

                            }
                    }
                    .addOnFailureListener {

                        btn_dangky_dangky.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE

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
        GetMaXacThuc()
    }

    fun GetMaXacThuc(){

        var length = str_Ma.length
        var textCode = ""
        for ( i in 1..6){
            var index = Random.nextInt(0, length)
            maXacThuc += str_Ma[index]
            textCode += "${str_Ma[index]} "
        }

        tv_getCode_dangky.text = textCode

    }

}
