package com.quanvu201120.supportfit.activity

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.model.CmtModel
import com.quanvu201120.supportfit.model.PostModel
import java.util.ArrayList
import kotlin.math.log

class PostDetailActivity : AppCompatActivity() {

    lateinit var image_post_detail : ImageView
    lateinit var tv_dateCreate_post_detail : TextView
    lateinit var tv_nameUser_post_detail : TextView
    lateinit var tv_title_post_detail : TextView
    lateinit var tv_description_post_detail : TextView
    lateinit var listViewCmt_post_detail : ListView
    lateinit var edt_cmt_post_detail : EditText
    lateinit var img_send_post_detail : ImageView

    lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        image_post_detail = findViewById(R.id.image_post_detail)
        tv_dateCreate_post_detail = findViewById(R.id.tv_dateCreate_post_detail)
        tv_nameUser_post_detail = findViewById(R.id.tv_nameUser_post_detail)
        tv_title_post_detail = findViewById(R.id.tv_title_post_detail)
        tv_description_post_detail = findViewById(R.id.tv_description_post_detail)
        listViewCmt_post_detail = findViewById(R.id.listViewCmt_post_detail)
        edt_cmt_post_detail = findViewById(R.id.edt_cmt_post_detail)
        img_send_post_detail = findViewById(R.id.img_send_post_detail)

        var post = getIntentToListPost()


        tv_dateCreate_post_detail.text = "${post?.dayCreate}/${post?.monthCreate}/${post?.yearCreate}  ${post?.hourCreate}:${post?.minuteCreate}:${post?.secondsCreate}"
        tv_nameUser_post_detail.text = post.nameUser
        tv_title_post_detail.text = post.title
        tv_description_post_detail.text = post.description
        image_post_detail.setImageResource(R.drawable.logo)

    }

    fun getIntentToListPost() : PostModel{
        var postId : String? = intent.getStringExtra("postId")
        var userId : String? = intent.getStringExtra("userId")
        var nameUser : String? = intent.getStringExtra("nameUser")
        var title : String? = intent.getStringExtra("title")
        var description : String? = intent.getStringExtra("description")
        var yearCreate : Int = intent.getIntExtra("yearCreate",0)
        var monthCreate : Int = intent.getIntExtra("monthCreate",0)
        var dayCreate : Int = intent.getIntExtra("dayCreate",0)
        var hourCreate : Int = intent.getIntExtra("hourCreate",0)
        var minuteCreate : Int = intent.getIntExtra("minuteCreate",0)
        var secondsCreate : Int = intent.getIntExtra("secondsCreate",0)
        var listCmt : ArrayList<CmtModel>? = intent.getParcelableArrayListExtra<CmtModel>("listCmt")
        var listUserFollow : ArrayList<String>? = intent.getStringArrayListExtra("listUserFollow")

        var post = PostModel(
            postId = postId!!, userId = userId!!, nameUser = nameUser!!, title = title!!,
            description = description!!, yearCreate = yearCreate!!, monthCreate = monthCreate!!,
            dayCreate = dayCreate!!, hourCreate = hourCreate!!, minuteCreate = minuteCreate!!,
            secondsCreate = secondsCreate!!, listCmt = listCmt!!, listUserFollow = listUserFollow!!
        )
        return post
    }

}