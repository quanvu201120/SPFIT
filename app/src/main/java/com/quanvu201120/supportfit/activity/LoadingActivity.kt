package com.quanvu201120.supportfit.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.model.CmtModel
import com.quanvu201120.supportfit.model.NotifyModel
import com.quanvu201120.supportfit.model.PostModel
import com.quanvu201120.supportfit.model.UserModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*
import kotlin.collections.ArrayList

var mUser = UserModel()
var mPost = ArrayList<PostModel>()
var mCmt = ArrayList<CmtModel>()
var mNotify = ArrayList<NotifyModel>()

class LoadingActivity : AppCompatActivity() {

    lateinit var firestore:FirebaseFirestore
    lateinit var auth: FirebaseAuth

    var TAG = "ABC"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        var firebaseUser = auth.currentUser

//
//                        var post = PostModel(
//                    postId = GenerateId(),
//                    userId = mUser.userId,
//                    dateCreate = GetCurrentTimeFirebase(),
//                    title = "Test title 3",
//                    description = "Test description 2"
//                )
//
//                firestore.collection(C_POST).document(post.postId)
//                    .set(post)
//                    .addOnSuccessListener {
//                        Log.e("ABC","create post success")
//                    }

        startActivity(Intent(this, MainActivity::class.java))

//        get_mUser(firebaseUser!!)

//        GetDataRealtime(C_POST)


    }




    fun get_mNotify(){
        firestore.collection(C_NOTIFY).get()
            .addOnSuccessListener {
                mNotify = it.toObjects(NotifyModel::class.java) as ArrayList<NotifyModel>
                Log.e("ABC get notify", mNotify.toString() )
                startActivity(Intent(this, MainActivity::class.java))
            }
    }

    fun get_mPost(){
        firestore.collection(C_POST).get()
            .addOnSuccessListener {
                mPost = it.toObjects(PostModel::class.java) as ArrayList<PostModel>
                Log.e("ABC get post", mPost.toString() )
//                get_mNotify()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }

    fun get_mUser(firebaseUser : FirebaseUser){
        firestore.collection(C_USER).document(firebaseUser!!.uid).get()
            .addOnSuccessListener {
                mUser = it.toObject(UserModel::class.java)!!
                get_mPost()
            }
    }

    fun GetDataRealtime(coll : String){
        firestore.collection(coll).addSnapshotListener { value, error ->
            value?.documentChanges?.map {
                var doc : DocumentSnapshot = it.document

                when(coll){
///// POST //////////////////////////////////////////////////////////
                    C_POST -> {
                        var tmp = doc.toObject(PostModel::class.java)!!
                        when(it.type){

                            DocumentChange.Type.ADDED -> {
                                mPost.add(tmp)
                                Log.e("abc realtime add", mPost.toString() )
                            }

                            DocumentChange.Type.MODIFIED -> {
//                                var index = mPost.indexOf(mPost.filter { it.postId == tmp.postId }[0])
                                var index = mPost.indexOf(mPost.find { it.postId == tmp.postId })

                                if (index != -1){
                                    mPost.set(index, tmp)
                                }else{}
                                Log.e("abc realtime update", mPost.toString() )
                            }

                            else -> {
                                mPost.remove(tmp)
                                 Log.e("abc realtime delete", mPost.toString() )
                            }

                        }
                    }
///// CMT //////////////////////////////////////////////////////////
                    C_CMT -> {
                        var tmp = doc.toObject(CmtModel::class.java)!!

                        var check : PostModel? = mPost.find { it.postId == tmp.postId }

                        if (check != null){
                            var index = mPost.indexOf(check)

                            when(it.type){

                                DocumentChange.Type.ADDED -> {

                                    mPost[index].listCmt.add(tmp)
                                    Log.e("abc realtime add", mCmt.toString() )
                                }

                                DocumentChange.Type.MODIFIED -> {
                                }

                                else -> {
                                    var del = mPost[index].listCmt.find { it.cmtId == tmp.cmtId }
                                    mPost[index].listCmt.remove(del)
                                    Log.e("abc realtime delete", mCmt.toString() )
                                }

                            }


                        }else{}

                    }
////// NOTIFY /////////////////////////////////////////////////////////
                    else -> {
                        var tmp = doc.toObject(NotifyModel::class.java)!!

                        var check  = mPost.find { it.postId == tmp.postId }

                        when(it.type){

                            DocumentChange.Type.ADDED -> {
                                mNotify.add(tmp)
                                Log.e("abc realtime add", mNotify.toString() )
                            }

                            DocumentChange.Type.MODIFIED -> {
                                var index = mNotify.indexOf(mNotify.filter { it.notifyId == tmp.notifyId }[0])

                                if (index != -1){
                                    mNotify.set(index, tmp)
                                }else{}
                                Log.e("abc realtime update", mNotify.toString() )
                            }

                            else -> {
                                mNotify.remove(tmp)
                                Log.e("abc realtime delete", mNotify.toString() )
                            }

                        }
                    }
///////////////////////////////////////////////////////////////
                }

            }
        }
    }


//Comment GetDataRealtimePost
//    fun GetDataRealtimePost(){
//        firestore.collection(C_POST).addSnapshotListener { value, error ->
//            value?.documentChanges?.map {
//                var doc : DocumentSnapshot = it.document
//                var tmp = doc.toObject(PostModel::class.java)!!
//                when(it.type){
//
//                    DocumentChange.Type.ADDED -> {
//                        mPost.add(tmp)
//                        Log.e("abc realtime add", mPost.toString() )
//                    }
//
//                    DocumentChange.Type.MODIFIED -> {
//                        var index = mPost.indexOf(mPost.filter { it.postId == tmp.postId }[0])
//
//                        if (index != -1){
//                            mPost.set(index, tmp)
//                        }else{}
//                            Log.e("abc realtime update", mPost.toString() )
//                    }
//
//                    else -> {
//                        mPost.remove(tmp)
//                        Log.e("abc realtime delete", mPost.toString() )
//                    }
//
//                }
//            }
//        }
//    }


}