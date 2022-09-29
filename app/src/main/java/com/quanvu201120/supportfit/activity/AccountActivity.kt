package com.quanvu201120.supportfit.activity

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.adapter.ListViewPostAdapter
import com.quanvu201120.supportfit.model.NotifyModel
import com.quanvu201120.supportfit.model.PostModel
import com.quanvu201120.supportfit.model.UserModel

class AccountActivity : AppCompatActivity() {

    lateinit var tv_name_account : TextView
    lateinit var tv_email_account : TextView
    lateinit var tv_change_password_account : TextView
    lateinit var tv_logout_account : TextView
    lateinit var img_edit_name_account : ImageView
    lateinit var img_edit_email_account : ImageView
    lateinit var tv_no_item_account : TextView
    lateinit var listview_follow_account : ListView

    lateinit var adapter : ListViewPostAdapter
    lateinit var listPostFollow : ArrayList<PostModel>

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        tv_name_account = findViewById(R.id.tv_name_account)
        tv_email_account = findViewById(R.id.tv_email_account)
        tv_change_password_account = findViewById(R.id.tv_change_password_account)
        tv_logout_account = findViewById(R.id.tv_logout_account)
        img_edit_name_account = findViewById(R.id.img_edit_name_account)
        img_edit_email_account = findViewById(R.id.img_edit_email_account)
        listview_follow_account = findViewById(R.id.listview_follow_account)
        tv_no_item_account = findViewById(R.id.tv_no_item_account)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        listPostFollow = ArrayList()

        renderListItem()

        tv_name_account.text = mUser.name
        tv_email_account.text = mUser.email

        listview_follow_account.setOnItemClickListener { adapterView, view, i, l ->
            IntentDetail(listPostFollow[i])
        }

        tv_logout_account.setOnClickListener {
            var dialog = AlertDialog.Builder(this)

            dialog.setMessage("Xác nhận đăng xuất")

            dialog.setPositiveButton("Đồng ý",
                object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                        auth.signOut()
                        startActivity(Intent(this@AccountActivity,LoginActivity::class.java))
                        finish()

                    }
                })
            dialog.setNegativeButton("Hủy bỏ",object  : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }
            })

            dialog.show()

        }

        tv_change_password_account.setOnClickListener {
            ChangePassword()

        }

        img_edit_name_account.setOnClickListener {
            ChangeName()
        }

        img_edit_email_account.setOnClickListener {
            ChangeEmail()
        }

        GetDataRealtime()

    }
    //////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////
    private fun ChangeName() {
        var customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_name)

        var edt_dialog_name = customDialog.findViewById<EditText>(R.id.edt_dialog_name)
        var btn_dialog_name = customDialog.findViewById<Button>(R.id.btn_dialog_name)
        var progressBar_dialog_name = customDialog.findViewById<ProgressBar>(R.id.progressBar_dialog_name)

        edt_dialog_name.text = convertEditable(mUser.name)

        btn_dialog_name.setOnClickListener {
            var nameUpdate = edt_dialog_name.text.toString().trim()

            if (nameUpdate.isEmpty()){
                edt_dialog_name.setError("Vui lòng nhập tên")
            }
            else{

                if (mUser.name.equals(nameUpdate)){
                    customDialog.dismiss()
                }
                else{
                    progressBar_dialog_name.visibility = View.VISIBLE
                    btn_dialog_name.visibility = View.INVISIBLE

                    mUser.name = nameUpdate
                    firestore.collection(C_USER).document(mUser.userId).update("name",nameUpdate)
                        .addOnSuccessListener {
                            tv_name_account.text = nameUpdate
                            Toast.makeText(this, "Đã đổi tên", Toast.LENGTH_SHORT).show()

                            progressBar_dialog_name.visibility = View.GONE
                            btn_dialog_name.visibility = View.VISIBLE

                            customDialog.dismiss()
                        }

                }
            }
        }

        customDialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        customDialog.show()
    }

    private fun ChangeEmail() {
        var customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_email)

        var edt_dialog_email = customDialog.findViewById<EditText>(R.id.edt_dialog_email)
        var btn_dialog_email = customDialog.findViewById<Button>(R.id.btn_dialog_email)
        var progressBar_dialog_email = customDialog.findViewById<ProgressBar>(R.id.progressBar_dialog_email)

        edt_dialog_email.text = convertEditable(mUser.email)

        btn_dialog_email.setOnClickListener {
            var emailUpdate = edt_dialog_email.text.toString().trim()

            if (emailUpdate.isEmpty()){
                edt_dialog_email.setError("Vui lòng nhập email")
            }
            else{

                if (mUser.email.equals(emailUpdate)){
                    customDialog.dismiss()
                }
                else{
                    progressBar_dialog_email.visibility = View.VISIBLE
                    btn_dialog_email.visibility = View.INVISIBLE

                    auth.currentUser!!.updateEmail(emailUpdate)
                        .addOnSuccessListener {

                            mUser.email = emailUpdate
                            firestore.collection(C_USER).document(mUser.userId).update("email",emailUpdate)
                            tv_email_account.text = emailUpdate
                            Toast.makeText(this, "Đã đổi email", Toast.LENGTH_SHORT).show()
                            customDialog.dismiss()

                        }
                        .addOnFailureListener {

                            var linked_another_account = "The email address is already in use by another account."
                            var wrong_format = "The email address is badly formatted."
                            var re_login = "This operation is sensitive and requires recent authentication. Log in again before retrying this request."

                            progressBar_dialog_email.visibility = View.GONE
                            btn_dialog_email.visibility = View.VISIBLE

                            when(it.message){
                                linked_another_account -> {
                                    Toast.makeText(this, "Email đã liên kết tài khoản khác", Toast.LENGTH_LONG).show()
                                }

                                wrong_format -> {
                                    Toast.makeText(this, "Email sai định dạng", Toast.LENGTH_LONG).show()
                                }

                                re_login -> {
                                    Toast.makeText(this, "Vì lí do bảo mật, vui lòng đăng nhập lại trước khi yêu cầu thao tác này", Toast.LENGTH_LONG).show()
                                    customDialog.dismiss()
                                }
                            }

                        }




                }
            }
        }

        customDialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        customDialog.show()
    }

    private fun ChangePassword(){
        var customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_password)

        var edt_currentPass_dialog_password = customDialog.findViewById<EditText>(R.id.edt_currentPass_dialog_password)
        var edt_newPass_dialog_password = customDialog.findViewById<EditText>(R.id.edt_newPass_dialog_password)
        var edt_confirmtPass_dialog_password = customDialog.findViewById<EditText>(R.id.edt_confirmtPass_dialog_password)
        var btn_dialog_password = customDialog.findViewById<Button>(R.id.btn_dialog_password)
        var progressBar_dialog_password = customDialog.findViewById<ProgressBar>(R.id.progressBar_dialog_password)

        btn_dialog_password.setOnClickListener {

            var currentPass = edt_currentPass_dialog_password.text.toString().trim()
            var newPass = edt_newPass_dialog_password.text.toString().trim()
            var confirmPass = edt_confirmtPass_dialog_password.text.toString().trim()

            var checkEmpty = false

            if (currentPass.isEmpty()){
                edt_currentPass_dialog_password.setError("Nhập mật khẩu cũ")
                checkEmpty = true
            }
            if (newPass.isEmpty()){
                edt_newPass_dialog_password.setError("Nhập mật khẩu mới")
                checkEmpty = true
            }
            if (confirmPass.isEmpty()){
                edt_confirmtPass_dialog_password.setError("Xác nhận mật khẩu mới")
                checkEmpty = true
            }

            if (!checkEmpty){


                if (newPass.equals(confirmPass)){

                    if (newPass.length < 6){
                        edt_newPass_dialog_password.setError("Tối thiểu 6 kí tự")
                        edt_confirmtPass_dialog_password.setError("Tối thiểu 6 kí tự")
                    }
                    else{
                        progressBar_dialog_password.visibility = View.VISIBLE
                        btn_dialog_password.visibility = View.INVISIBLE

                        val user = auth.currentUser!!
                        val credential = EmailAuthProvider
                            .getCredential(mUser.email, currentPass)

                        user.reauthenticate(credential)
                            .addOnSuccessListener {

                                user.updatePassword(newPass)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                                        customDialog.dismiss()
                                    }

                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Mật khẩu cũ không chính xác", Toast.LENGTH_SHORT).show()
                                progressBar_dialog_password.visibility = View.GONE
                                btn_dialog_password.visibility = View.VISIBLE
                            }
                    }

                }
                else{
                    edt_confirmtPass_dialog_password.setError("Mật khẩu không khớp")
                }

            }

        }


        customDialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        customDialog.show()
    }

    fun renderListItem(){

        if (mUser.listFollow.isEmpty()){
            tv_no_item_account.visibility = View.VISIBLE
            listview_follow_account.visibility = View.INVISIBLE
        }
        else{
            tv_no_item_account.visibility = View.GONE
            listview_follow_account.visibility = View.VISIBLE
            listPostFollow.clear()
            mUser.listFollow.map { item ->
                var tmp = mPost.find { it -> it.postId == item.idPost }

                tmp?.let {
                    listPostFollow.add(it)
                }
            }

            adapter = ListViewPostAdapter(this, listPostFollow)
            listview_follow_account.adapter = adapter
        }
    }

    fun IntentDetail(postModel: PostModel){
        var intent = Intent(this,PostDetailActivity::class.java)

        intent.putExtra("postId",postModel.postId)

        startActivity(intent)
    }




    fun GetDataRealtime( ){
        firestore.collection(C_USER).addSnapshotListener { value, error ->
            value?.documentChanges?.map {
                var doc: DocumentSnapshot = it.document

                var tmp = doc.toObject(UserModel::class.java)!!

                var check = tmp.userId == mUser.userId

                var isNull = true

                if (check){
                    renderListItem()
                }


            }

        }
    }
}