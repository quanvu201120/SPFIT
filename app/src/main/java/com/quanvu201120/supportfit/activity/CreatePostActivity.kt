package com.quanvu201120.supportfit.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.model.PostsModel
import java.io.File

class CreatePostActivity : AppCompatActivity() {

    lateinit var btn_submit_create_post : Button
    lateinit var btn_cancel_create_post : Button
    lateinit var img_create_post : ImageView
    lateinit var edt_title_create_post : EditText
    lateinit var edt_description_create_post : EditText
    lateinit var progressBar_create_post : ProgressBar

    val GET_FROM_GALLERY = 3;
    var URI_IMAGE : Uri? = null

    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var firebaseStorage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_create_post)

        img_create_post = findViewById(R.id.img_create_post)
        btn_submit_create_post = findViewById(R.id.btn_submit_create_post)
        btn_cancel_create_post = findViewById(R.id.btn_cancel_create_post)
        edt_title_create_post = findViewById(R.id.edt_title_create_post)
        edt_description_create_post = findViewById(R.id.edt_description_create_post)
        progressBar_create_post = findViewById(R.id.progressBar_create_post)

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        img_create_post.setOnClickListener {
            startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                ),
                GET_FROM_GALLERY
            )
        }

        btn_cancel_create_post.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        btn_submit_create_post.setOnClickListener {

            var check = false

           var title = edt_title_create_post.text.toString().trim()
           var description = edt_description_create_post.text.toString().trim()

            if (title.isEmpty()){
                check = true
                edt_title_create_post.setError("Vui lòng nhập tiêu đề")
            }
            if (description.isEmpty()){
                check = true
                edt_description_create_post.setError("Vui lòng nhập nội dung")
            }
            


            if (check){return@setOnClickListener}

            progressBar_create_post.visibility = View.VISIBLE
            btn_cancel_create_post.visibility = View.INVISIBLE
            btn_submit_create_post.visibility = View.INVISIBLE

            var idPost_imageName = GenerateId() //vừa là tên image vừa là id post
            var time = GetCurrentTimeFirebase()
            var post = PostsModel(
                postId = idPost_imageName,
                userId = mUser.userId,
                yearCreate = time[0],
                monthCreate = time[1],
                dayCreate = time[2],
                hourCreate = time[3],
                minuteCreate = time[4],
                secondsCreate = time[5],
                title = title,
                description = description,
                image = if(URI_IMAGE == null){"image"}else{idPost_imageName+".png"},
                nameUser = mUser.name,
                isComplete = false,
                isDisableCmt = false
            )

            if (URI_IMAGE == null){
                createPost(idPost_imageName,post)
            }
            else{
                createPostImage(idPost_imageName,post)
            }

        }

    }

    fun createPostImage(idPost_imageName : String, post : PostsModel){
        var storageReference : StorageReference = firebaseStorage.reference.child(idPost_imageName+".png")
        storageReference.putFile(URI_IMAGE!!)
            .addOnSuccessListener {
                createPost(idPost_imageName,post)
            }
            .addOnFailureListener {
                progressBar_create_post.visibility = View.VISIBLE
                btn_cancel_create_post.visibility = View.INVISIBLE
                btn_submit_create_post.visibility = View.INVISIBLE
            }
    }

    fun createPost(idPost_imageName : String, post : PostsModel){
        firebaseFirestore.collection(C_POSTS).document(idPost_imageName)
            .set(post)
            .addOnSuccessListener {

                mUser.listPost.add(idPost_imageName)
                firebaseFirestore.collection(C_USER).document(mUser.userId).set(mUser)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Tạo bài viết thành công", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    .addOnFailureListener {
                        progressBar_create_post.visibility = View.VISIBLE
                        btn_cancel_create_post.visibility = View.INVISIBLE
                        btn_submit_create_post.visibility = View.INVISIBLE
                    }

            }
            .addOnFailureListener {
                progressBar_create_post.visibility = View.VISIBLE
                btn_cancel_create_post.visibility = View.INVISIBLE
                btn_submit_create_post.visibility = View.INVISIBLE
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            URI_IMAGE = data?.data!!
            img_create_post.setImageURI(URI_IMAGE)
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}

