package com.quanvu201120.supportfit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.model.UserModel

val C_USER = "USER"
val C_POST = "POST"
val C_CMT = "CMT"
val C_NOTIFY = "NOTIFY"

class RegisterActivity : AppCompatActivity() {


    lateinit var edt_email_dangky : EditText
    lateinit var edt_pass_dangky : EditText
    lateinit var edt_name_dangky : EditText
    lateinit var edt_nhaplaipass_dangky : EditText
    lateinit var btn_dangky_dangky : Button
    lateinit var progressBar: ProgressBar

    lateinit var auth : FirebaseAuth
    lateinit var fireStore : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edt_email_dangky = findViewById(R.id.edt_email_dangky)
        edt_name_dangky = findViewById(R.id.edt_name_dangky)
        edt_pass_dangky = findViewById(R.id.edt_pass_dangky)
        edt_nhaplaipass_dangky = findViewById(R.id.edt_nhaplaipass_dangky)
        btn_dangky_dangky = findViewById(R.id.btn_dangky_dangky)
        progressBar = findViewById(R.id.progress_dangky)

        auth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()


        btn_dangky_dangky.setOnClickListener {

            var email = edt_email_dangky.text.toString().trim()
            var name = edt_name_dangky.text.toString().trim()
            var pass1 = edt_pass_dangky.text.toString().trim()
            var pass2 = edt_nhaplaipass_dangky.text.toString().trim()

            if (email.isEmpty()){
                edt_email_dangky.setError("Vui lòng nhập email")
            }
            if (name.isEmpty()){
                edt_name_dangky.setError("Vui lòng nhập email")
            }

            if (pass1.isEmpty()){
                edt_pass_dangky.setError("Vui lòng nhập mật khẩu")
            }
            if (pass2.isEmpty()){
                edt_nhaplaipass_dangky.setError("Vui lòng nhập lại mật khẩu")
            }

            if (!email.isEmpty() && !name.isEmpty() &&  !pass1.isEmpty() && !pass2.isEmpty()){

                if (pass1 != pass2){
                    edt_nhaplaipass_dangky.setError("Mật khẩu không khớp")
                }
                else{

                    btn_dangky_dangky.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE

                    auth.createUserWithEmailAndPassword(email,pass1)
                        .addOnSuccessListener {

                            val uid = auth.currentUser!!.uid
                            val user = UserModel(userId = uid, name = name, email = email)

                            fireStore.collection(C_USER).document(uid)
                                .set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_LONG).show()
                                    auth.signOut()
                                    finish()
                                }

                        }
                        .addOnFailureListener{

                            btn_dangky_dangky.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE

                            val e_format = "The email address is badly formatted."
                            val e_exists = "The email address is already in use by another account."

                            if (it.message == e_exists){
                                Toast.makeText(this, "Email đã được sử dụng", Toast.LENGTH_SHORT).show()
                            }
                            else if (it.message == e_format){
                                Toast.makeText(this, "Email sai định dạng", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                edt_pass_dangky.setError("Mật khẩu tối thiểu 6 kí tự")
                                edt_nhaplaipass_dangky.setError("Mật khẩu tối thiểu 6 kí tự")
//                                Toast.makeText(this, "Mật khẩu tối thiểu 6 kí tự", Toast.LENGTH_SHORT).show()
                            }


                        }
                }

            }


        }

    }
}