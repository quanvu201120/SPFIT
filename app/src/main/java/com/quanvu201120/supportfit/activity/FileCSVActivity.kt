package com.quanvu201120.supportfit.activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.adapter.ListViewUserCSV
import com.quanvu201120.supportfit.model.ItemUserCSV
import com.quanvu201120.supportfit.model.UserModel
import java.io.*


class FileCSVActivity : AppCompatActivity() {

    lateinit var button_select_file_csv : Button
    lateinit var button_back_csv : Button
    lateinit var button_create_file_csv : Button
    lateinit var tv_title_error_csv : TextView
    lateinit var listview_user_csv : ListView
    lateinit var tv_title_loadingg_csv : TextView
    lateinit var tv_title_create_csv : TextView
    lateinit var tv_title_result_csv : TextView
    lateinit var progressBar_CSV : ProgressBar

    lateinit var adapter : ListViewUserCSV
    lateinit var userCSVList: ArrayList<ItemUserCSV>
    lateinit var userError: ArrayList<ItemUserCSV>

    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_csvactivity)

        button_select_file_csv = findViewById(R.id.button_select_file_csv)
        button_back_csv = findViewById(R.id.button_back_csv)
        button_back_csv = findViewById(R.id.button_back_csv)
        tv_title_error_csv = findViewById(R.id.tv_title_result_csv)
        tv_title_result_csv = findViewById(R.id.tv_title_result_csv)
        button_create_file_csv = findViewById(R.id.button_create_file_csv)
        listview_user_csv = findViewById(R.id.listview_user_csv)
        tv_title_loadingg_csv = findViewById(R.id.tv_title_loadingg_csv)
        tv_title_create_csv = findViewById(R.id.tv_title_create_csv)
        progressBar_CSV = findViewById(R.id.progressBar_CSV)


        userCSVList = ArrayList()
        userError = ArrayList()

        adapter = ListViewUserCSV(this,userCSVList)
        listview_user_csv.adapter = adapter

        button_select_file_csv.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "text/*"
            startActivityForResult(intent, 1000)
        }

        button_back_csv.setOnClickListener {
            finish()
        }

        button_create_file_csv.setOnClickListener {
            var sharedPreferences : SharedPreferences = getSharedPreferences("ACCOUNT", Activity.MODE_PRIVATE)
            val getEmail : String? = sharedPreferences.getString(STR_EMAIL,null)
            val getPass : String? = sharedPreferences.getString(STR_PASS,null)


            button_back_csv.visibility = View.GONE
            button_select_file_csv.visibility = View.GONE
            listview_user_csv.visibility = View.GONE
            button_create_file_csv.visibility = View.GONE

            tv_title_create_csv.visibility = View.VISIBLE
            progressBar_CSV.visibility = View.VISIBLE
            tv_title_loadingg_csv.visibility = View.VISIBLE


            createMultiple(0,userCSVList.size, userCSVList, getEmail!!, getPass!!)


        }

    }


    fun createMultiple(i : Int, length: Int, listTmp:ArrayList<ItemUserCSV>,email:String, pass:String){

        if(i == length){
            Firebase.auth.signInWithEmailAndPassword(email,pass)
                .addOnSuccessListener {

                    button_back_csv.visibility = View.VISIBLE
                    button_select_file_csv.visibility = View.VISIBLE
                    listview_user_csv.visibility = View.VISIBLE
                    button_create_file_csv.visibility = View.GONE

                    tv_title_create_csv.visibility = View.GONE
                    progressBar_CSV.visibility = View.GONE
                    tv_title_loadingg_csv.visibility = View.GONE

                    tv_title_result_csv.visibility = View.VISIBLE

                    adapter = ListViewUserCSV(this,userError)
                    listview_user_csv.adapter = adapter
                    adapter.notifyDataSetChanged()

                }
        }
        else{
            tv_title_loadingg_csv.text = "${listTmp[count].email}\n${count}/${userCSVList.size}"
            Firebase.auth.createUserWithEmailAndPassword(listTmp[i].email, PASS_DEFAULT)
                .addOnSuccessListener {

                    val uid = Firebase.auth.currentUser!!.uid
                    val user = UserModel(userId = uid, name = listTmp[i].name, email = listTmp[i].email)

                    Firebase.firestore.collection(C_USER).document(uid)
                        .set(user)
                        .addOnSuccessListener {
                            Firebase.auth.currentUser!!.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        count++

                                        createMultiple(i+1,length,listTmp,email, pass)
                                    }
                                }

                        }

                    }
                .addOnFailureListener {
                    userError.add(listTmp[i])
                    count++
                    createMultiple(i+1,length,listTmp,email, pass)
                }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            var uriFile = data!!.data
            readCSV(uriFile!!)
        }
    }

    fun readCSV(uriFile:Uri){
        try {
            userCSVList.clear()
            userError.clear()
            val inputStream: InputStream? = contentResolver.openInputStream(uriFile!!)
            var isr : InputStreamReader = InputStreamReader(inputStream)
            var br : BufferedReader = BufferedReader(isr)
            var line = br.readLine()

            while (line != null){
                var strings = line.split(",")
                if (strings.size == 2){
                    var itemUserCSV = ItemUserCSV(strings[0],strings[1])
                    userCSVList.add(itemUserCSV)
                }

                line = br.readLine()
            }

            adapter = ListViewUserCSV(this,userCSVList)
            listview_user_csv.adapter = adapter

            listview_user_csv.visibility = View.VISIBLE

            if (userCSVList.size > 0){
                button_create_file_csv.visibility = View.VISIBLE
            }
            else{
                button_create_file_csv.visibility = View.INVISIBLE
            }

            br.close()
            isr.close()

        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {

    }

}