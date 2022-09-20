package com.quanvu201120.supportfit.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.adapter.RecycleViewCmtAdapter
import com.quanvu201120.supportfit.model.CmtModel
import com.quanvu201120.supportfit.model.PostModel
import java.io.File
import java.util.ArrayList

class PostDetailActivity : AppCompatActivity() {

    lateinit var image_post_detail : ImageView
    lateinit var tv_dateCreate_post_detail : TextView
    lateinit var tv_nameUser_post_detail : TextView
    lateinit var tv_follow_post_detail : TextView
    lateinit var tv_title_post_detail : TextView
    lateinit var tv_description_post_detail : TextView
    lateinit var recycleViewCmt_post_detail : RecyclerView
    lateinit var edt_cmt_post_detail : EditText
    lateinit var img_send_post_detail : ImageView
    lateinit var progressBarCmt : ProgressBar

    lateinit var recycleViewCmtAdapter: RecycleViewCmtAdapter
    lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        image_post_detail = findViewById(R.id.image_post_detail)
        tv_dateCreate_post_detail = findViewById(R.id.tv_dateCreate_post_detail)
        tv_nameUser_post_detail = findViewById(R.id.tv_nameUser_post_detail)
        tv_follow_post_detail = findViewById(R.id.tv_follow_post_detail)
        tv_title_post_detail = findViewById(R.id.tv_title_post_detail)
        tv_description_post_detail = findViewById(R.id.tv_description_post_detail)
        recycleViewCmt_post_detail = findViewById(R.id.recycleViewCmt_post_detail)
        edt_cmt_post_detail = findViewById(R.id.edt_cmt_post_detail)
        img_send_post_detail = findViewById(R.id.img_send_post_detail)
        progressBarCmt = findViewById(R.id.progressBarCmt)

        storage = FirebaseStorage.getInstance()

        var postId : String? = intent.getStringExtra("postId")

        var post = mPost.find { i -> i.postId == postId }

        tv_dateCreate_post_detail.text = "${post?.dayCreate}/${post?.monthCreate}/${post?.yearCreate}  ${post?.hourCreate}:${post?.minuteCreate}:${post?.secondsCreate}"
        tv_nameUser_post_detail.text = if(post!!.userId == mUser.userId){"Bạn"}else{post!!.nameUser}
        tv_title_post_detail.text = post!!.title
        tv_description_post_detail.text = post!!.description

        post.listCmt.sortWith(compareBy<CmtModel> {it.yearCreate}
            .thenBy { it.monthCreate }
            .thenBy { it.dayCreate }
            .thenBy { it.hourCreate }
            .thenBy { it.minuteCreate }
            .thenBy { it.secondsCreate }
        )

        post.listCmt.reverse()

        if (post.listCmt.isEmpty()){
            recycleViewCmt_post_detail.visibility = View.GONE
        }

        if (post.image.equals("image")){
            image_post_detail.setImageResource(R.drawable.logo)
        }
        else{
            var image_file : File = File.createTempFile("get_image",".png")

            var storageReference : StorageReference = storage.reference

            //truyền vào tên file
            storageReference.child(post.image)
                //đây là get nên truyền vào file nhận ảnh sau khi thực hiện getFile
                .getFile(image_file)
                .addOnSuccessListener {

                    var bitmap : Bitmap = BitmapFactory.decodeFile(image_file.path)
                    image_post_detail.setImageBitmap(bitmap)

                }
       }

        if (post.userId == mUser.userId){
            tv_follow_post_detail.visibility = View.GONE
        }

        if (mUser.listFollow.indexOf(post.postId) == -1){
            tv_follow_post_detail.text = "Theo dõi"
        }
        else{
            tv_follow_post_detail.text = "Bỏ theo dõi"
        }

        recycleViewCmtAdapter = RecycleViewCmtAdapter(listCmt = post.listCmt)
        recycleViewCmt_post_detail.adapter = recycleViewCmtAdapter
        recycleViewCmt_post_detail.layoutManager = LinearLayoutManager(this@PostDetailActivity)

    }


}