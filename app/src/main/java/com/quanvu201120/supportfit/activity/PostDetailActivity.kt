package com.quanvu201120.supportfit.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.adapter.RecycleViewCmtAdapter
import com.quanvu201120.supportfit.model.CmtModel
import com.quanvu201120.supportfit.model.PostModel
import java.io.File

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
    lateinit var firebaseFirestore: FirebaseFirestore

    var post : PostModel = PostModel()

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
        firebaseFirestore = FirebaseFirestore.getInstance()

        var postId : String? = intent.getStringExtra("postId")

        post = mPost.find { i -> i.postId == postId }!!

//        tv_dateCreate_post_detail.text = "${post?.dayCreate}/${post?.monthCreate}/${post?.yearCreate}  ${post?.hourCreate}:${post?.minuteCreate}:${post?.secondsCreate}"
//        tv_nameUser_post_detail.text = if(post!!.userId == mUser.userId){"Bạn"}else{post!!.nameUser}
//        tv_title_post_detail.text = post!!.title
//        tv_description_post_detail.text = post!!.description
//
//        post.listCmt.sortWith(compareBy<CmtModel> {it.yearCreate}
//            .thenBy { it.monthCreate }
//            .thenBy { it.dayCreate }
//            .thenBy { it.hourCreate }
//            .thenBy { it.minuteCreate }
//            .thenBy { it.secondsCreate }
//        )
//
//        post.listCmt.reverse()

        LoadDataToUI()

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

//        recycleViewCmtAdapter = RecycleViewCmtAdapter(listCmt = post.listCmt)
//        recycleViewCmt_post_detail.adapter = recycleViewCmtAdapter
//        recycleViewCmt_post_detail.layoutManager = LinearLayoutManager(this@PostDetailActivity)

        img_send_post_detail.setOnClickListener {
            var str_cmt = edt_cmt_post_detail.text.toString()

            img_send_post_detail.visibility = View.INVISIBLE
            progressBarCmt.visibility = View.VISIBLE

            if (str_cmt.isEmpty()){return@setOnClickListener}
            var time = GetCurrentTimeFirebase()

            var cmt = CmtModel(
                cmtId = GenerateId(),
                userId = mUser.userId,
                postId = post.postId,
                content = str_cmt,
                yearCreate = time[0],
                monthCreate = time[1],
                dayCreate = time[2],
                hourCreate = time[3],
                minuteCreate = time[4],
                secondsCreate = time[5],
                nameUser = mUser.name
            )

            val view = this.currentFocus
            if (view != null) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

            post.listCmt.add(cmt)

            firebaseFirestore.collection(C_POST).document(post.postId).set(post)
                .addOnSuccessListener {
                    edt_cmt_post_detail.text = convertEditable("")
                    img_send_post_detail.visibility = View.VISIBLE
                    progressBarCmt.visibility = View.GONE
                    Toast.makeText(this, "Đã đăng", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    img_send_post_detail.visibility = View.VISIBLE
                    progressBarCmt.visibility = View.GONE
                }

        }

        GetDataRealtimePost()
    }

    fun deleteCmt(cmt_delete : CmtModel){
        var dialog = AlertDialog.Builder(this)

        dialog.setMessage("Xác nhận xóa bình luận")

        dialog.setPositiveButton("Đồng ý",
        object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                post.listCmt.remove(cmt_delete)
                firebaseFirestore.collection(C_POST).document(post.postId).set(post)
                    .addOnSuccessListener {
                        Toast.makeText(this@PostDetailActivity, "Đã xóa bình luận", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {

                    }
            }
        })
        dialog.setNegativeButton("Hủy bỏ",object  : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {

            }
        })

        dialog.show()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {


        if (item.itemId == 100){

            var cmt_delete = post.listCmt.get(item.groupId)

            if (post.userId == mUser.userId){
                deleteCmt(cmt_delete)
            }
            else{
                if (cmt_delete.userId != mUser.userId){
                    Toast.makeText(this, "Bạn không thể xóa", Toast.LENGTH_SHORT).show()
                }
                else{
                    deleteCmt(cmt_delete)
                }
            }



        }

        return super.onContextItemSelected(item)
    }

    fun LoadDataToUI(){
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

        recycleViewCmtAdapter = RecycleViewCmtAdapter(listCmt = post.listCmt)
        recycleViewCmt_post_detail.adapter = recycleViewCmtAdapter
        recycleViewCmt_post_detail.layoutManager = LinearLayoutManager(this@PostDetailActivity)
    }

    @SuppressLint("LongLogTag")
    fun GetDataRealtimePost(){

        var firestore = FirebaseFirestore.getInstance()

        firestore.collection(C_POST).addSnapshotListener { value, error ->
            value?.documentChanges?.map {
                var doc : DocumentSnapshot = it.document

                var tmp = doc.toObject(PostModel::class.java)!!
                when(it.type){

                    DocumentChange.Type.ADDED -> {

                    }

                    DocumentChange.Type.MODIFIED -> {
//
                        if (tmp.postId == post.postId){
                            post = tmp

                            LoadDataToUI()
                            if (!post.listCmt.isEmpty()){
                                recycleViewCmt_post_detail.visibility = View.VISIBLE
                            }
//                            Log.e("abc realtime update detail", mPost.toString() )

                        }else{}
                    }

                    else -> {

                    }

                }

            }
        }
    }

}