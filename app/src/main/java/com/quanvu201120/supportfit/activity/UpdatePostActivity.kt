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
import kotlin.math.log

class UpdatePostActivity : AppCompatActivity() {

    lateinit var btn_submit_update_post : Button
    lateinit var btn_cancel_update_post : Button
    lateinit var img_update_post : ImageView
    lateinit var edt_title_update_post : EditText
    lateinit var edt_description_update_post : EditText
    lateinit var progressBar_update_post : ProgressBar
    lateinit var checkbox_complete_update : CheckBox
    lateinit var checkbox_blockCmt_update : CheckBox
    lateinit var spinner_update : Spinner

    lateinit var listSpinner : ArrayList<String>
    lateinit var arrayAdapter: ArrayAdapter<String>



    val GET_FROM_GALLERY = 3;
    var URI_IMAGE : Uri? = null

    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var firebaseStorage: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_post)

        spinner_update = findViewById(R.id.spinner_update)
        checkbox_complete_update = findViewById(R.id.checkbox_complete_update)
        checkbox_blockCmt_update = findViewById(R.id.checkbox_blockCmt_update)
        img_update_post = findViewById(R.id.img_update_post)
        btn_submit_update_post = findViewById(R.id.btn_submit_update_post)
        btn_cancel_update_post = findViewById(R.id.btn_cancel_update_post)
        edt_title_update_post = findViewById(R.id.edt_title_update_post)
        edt_description_update_post = findViewById(R.id.edt_description_update_post)
        progressBar_update_post = findViewById(R.id.progressBar_update_post)

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        listSpinner = ArrayList()
        listSpinner.add("Vật dụng thất lạc")
        listSpinner.add("Thuê vật dụng")
        listSpinner.add("Tài liệu học tập")
        listSpinner.add("Khác")






        var postIdIntent = intent.getStringExtra("postId")
        var post : PostsModel? = mPost.find { item -> item.postId == postIdIntent }

//set data
        var categorySpinner = post!!.category
        var index = listSpinner.indexOf(post?.category)
        if (index > 0){
            var tmp = listSpinner[0]
            listSpinner.set(0,post!!.category)
            listSpinner.set(index,tmp)
        }


        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listSpinner)
        spinner_update.adapter = arrayAdapter

        checkbox_complete_update.isChecked = if (post?.isComplete == true){true}else{false}
        checkbox_blockCmt_update.isChecked = if (post?.isDisableCmt == true){true}else{false}

        if (post?.image.equals("image")){
            img_update_post.setImageResource(R.drawable.icon_add)
        }
        else{
            var image_file : File = File.createTempFile("get_image",".png")

            var storageReference : StorageReference = firebaseStorage.reference

            //truyền vào tên file
            storageReference.child(post!!.image)
                //đây là get nên truyền vào file nhận ảnh sau khi thực hiện getFile
                .getFile(image_file)
                .addOnSuccessListener {

                    var bitmap : Bitmap = BitmapFactory.decodeFile(image_file.path)
                    img_update_post.setImageBitmap(bitmap)

                }
        }

        edt_title_update_post.text = convertEditable(post!!.title)
        edt_description_update_post.text = convertEditable(post!!.description)

// end set data

        spinner_update.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                categorySpinner = listSpinner[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        img_update_post.setOnClickListener {
            startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                ),
                GET_FROM_GALLERY
            )
        }

        btn_cancel_update_post.setOnClickListener {
           resultCancel()
        }

        btn_submit_update_post.setOnClickListener {

            var checkEmpty = false

            var title = edt_title_update_post.text.toString().trim()
            var description = edt_description_update_post.text.toString().trim()
            var complete = checkbox_complete_update.isChecked
            var blockCmt = checkbox_blockCmt_update.isChecked

            if (title.isEmpty()){
                checkEmpty = true
                edt_title_update_post.setError("Vui lòng nhập tiêu đề")
            }
            if (description.isEmpty()){
                checkEmpty = true
                edt_description_update_post.setError("Vui lòng nhập nội dung")
            }

            if (checkEmpty){return@setOnClickListener}

            if (URI_IMAGE != null){

            }

            var checkChangeComplete = if(complete != post.isComplete){true}else{false}
            var checkDisableCmt = if(blockCmt != post.isDisableCmt){true}else{false}
            var checkCategory = if(categorySpinner != post.category){true}else{false}

            //kiểm tra có sự thay đổi
            if (!post.title.equals(title) || !post.description.equals(description) || URI_IMAGE != null || checkChangeComplete  || checkDisableCmt == true || checkCategory){

                progressBar_update_post.visibility = View.VISIBLE
                btn_cancel_update_post.visibility = View.INVISIBLE
                btn_submit_update_post.visibility = View.INVISIBLE

                if (URI_IMAGE != null){
                    updateImage(title,description,post,complete,blockCmt,categorySpinner)
                }
                else{
                    updateContent(title,description,post,complete,blockCmt,categorySpinner)
                }

            }
            else{
                resultCancel()
            }









        }


    }

    fun updateContent(titleUpdate : String, desciptionUpdate : String, prePost : PostsModel,complete:Boolean,blockCmt:Boolean,categorySpinner:String){
        var postUpdate = prePost
        postUpdate.title = titleUpdate
        postUpdate.description = desciptionUpdate
        postUpdate.isComplete = complete
        postUpdate.isDisableCmt = blockCmt
        postUpdate.category = categorySpinner

        firebaseFirestore.collection(C_POSTS).document(postUpdate.postId).set(postUpdate)
            .addOnSuccessListener {
                resultOk()
            }
    }

    fun updateImage(titleUpdate : String, desciptionUpdate : String, prePost : PostsModel,complete:Boolean,blockCmt:Boolean,categorySpinner : String){

        var imageName = if(!prePost?.image.equals("image")){prePost.image}else{prePost.postId + ".png"}

        var storageReference : StorageReference = firebaseStorage.reference.child(imageName)
        storageReference.putFile(URI_IMAGE!!)
            .addOnSuccessListener {

                //nếu chưa có image mà update add image
                if(prePost?.image.equals("image")){
                    var postUpdate = prePost
                    postUpdate.image = imageName
                    updateContent(titleUpdate,desciptionUpdate,postUpdate,complete,blockCmt,categorySpinner)
                }
                else{
                    if (!prePost.title.equals(titleUpdate) || !prePost.description.equals(desciptionUpdate)){
                        updateContent(titleUpdate,desciptionUpdate,prePost,complete,blockCmt,categorySpinner)
                    }
                    else{
                        resultOk()
                    }
                }



            }
            .addOnFailureListener {
                progressBar_update_post.visibility = View.VISIBLE
                btn_cancel_update_post.visibility = View.INVISIBLE
                btn_submit_update_post.visibility = View.INVISIBLE
            }
    }

    fun  resultCancel(){
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    fun resultOk(){
        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            URI_IMAGE = data?.data!!
            img_update_post.setImageURI(URI_IMAGE)
        }
    }

}