package com.quanvu201120.supportfit.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.createBitmap
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.adapter.RecycleViewCmtAdapter
import com.quanvu201120.supportfit.adapter.onClickLikeItem
import com.quanvu201120.supportfit.model.*
import java.io.File
import kotlin.math.log

class PostDetailActivity : AppCompatActivity(),  onClickLikeItem{

    lateinit var layout_comment_post_detail : ConstraintLayout
    lateinit var imgImageCmt_post_detail : ImageView
    lateinit var imgDeleteImageCmt_post_detail : ImageView
    lateinit var img_camera_post_detail : ImageView
    lateinit var img_disable_comment : ImageView
    lateinit var tv_complete_post_detail : TextView
    lateinit var image_post_detail : ImageView
    lateinit var tv_dateCreate_post_detail : TextView
    lateinit var tv_nameUser_post_detail : TextView
    lateinit var tv_follow_post_detail : TextView
    lateinit var tv_title_post_detail : TextView
    lateinit var tv_description_post_detail : TextView
    lateinit var tv_no_item_post_detail : TextView
    lateinit var recycleViewCmt_post_detail : RecyclerView
    lateinit var edt_cmt_post_detail : EditText
    lateinit var img_send_post_detail : ImageView
    lateinit var progressBarCmt : ProgressBar

    lateinit var recycleViewCmtAdapter: RecycleViewCmtAdapter
    lateinit var storage: FirebaseStorage
    lateinit var firebaseFirestore: FirebaseFirestore

    var post : PostsModel = PostsModel()
    lateinit var listUserFollow : ArrayList<UserModel>

    var isDelete = false

    val GET_FROM_GALLERY = 3;
    var URI_IMAGE : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        tv_no_item_post_detail = findViewById(R.id.tv_no_item_post_detail)
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

        imgImageCmt_post_detail = findViewById(R.id.imgImageCmt_post_detail)
        imgDeleteImageCmt_post_detail = findViewById(R.id.imgDeleteImageCmt_post_detail)
        img_camera_post_detail = findViewById(R.id.img_camera_post_detail)
        img_disable_comment = findViewById(R.id.img_disable_comment)
        tv_complete_post_detail = findViewById(R.id.tv_complete_post_detail)
        layout_comment_post_detail = findViewById(R.id.layout_comment_post_detail)

        storage = FirebaseStorage.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        listUserFollow = ArrayList()

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
            image_post_detail.setImageResource(R.drawable.logo2)
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

        if (mUser.listFollow.find { i -> i.idPost == post.postId } == null){
            tv_follow_post_detail.text = "Theo dõi"
        }
        else{
            tv_follow_post_detail.text = "Bỏ theo dõi"
        }

//        recycleViewCmtAdapter = RecycleViewCmtAdapter(listCmt = post.listCmt)
//        recycleViewCmt_post_detail.adapter = recycleViewCmtAdapter
//        recycleViewCmt_post_detail.layoutManager = LinearLayoutManager(this@PostDetailActivity)
//        Log.e("abc fff", post.listUserFollow.toString() )
        img_send_post_detail.setOnClickListener {
            if (isDelete){
                Toast.makeText(this, "Bài viết đã bị xóa", Toast.LENGTH_SHORT).show()
            }
            else{
                var str_cmt = edt_cmt_post_detail.text.toString()


                if (str_cmt.isEmpty() && URI_IMAGE == null){return@setOnClickListener}

                var time = GetCurrentTimeFirebase()

                img_send_post_detail.visibility = View.INVISIBLE
                progressBarCmt.visibility = View.VISIBLE
                img_camera_post_detail.visibility = View.INVISIBLE
                imgDeleteImageCmt_post_detail.visibility = View.GONE

                var generateCmtId = GenerateId()
                var cmt = CmtModel(
                    cmtId = generateCmtId,
                    userId = mUser.userId,
                    postId = post.postId,
                    content = str_cmt,
                    yearCreate = time[0],
                    monthCreate = time[1],
                    dayCreate = time[2],
                    hourCreate = time[3],
                    minuteCreate = time[4],
                    secondsCreate = time[5],
                    nameUser = mUser.name,
                    image = if(URI_IMAGE == null){"image"}else{generateCmtId + ".png"},
                )

                val view = this.currentFocus
                if (view != null) {
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }

                post.listCmt.add(cmt)

                if (URI_IMAGE != null){
                    var storageReference : StorageReference = storage.reference.child( generateCmtId+".png")
                    storageReference.putFile(URI_IMAGE!!)
                        .addOnSuccessListener {
                            firebaseFirestore.collection(C_POSTS).document(post.postId).set(post)
                                .addOnSuccessListener {
                                    edt_cmt_post_detail.text = convertEditable("")
                                    img_send_post_detail.visibility = View.VISIBLE
                                    progressBarCmt.visibility = View.GONE
                                    img_camera_post_detail.visibility = View.VISIBLE
                                    resetImageCmt()
                                    Toast.makeText(this, "Đã đăng", Toast.LENGTH_SHORT).show()
                                    SendNotificationAPI(post.listTokenFollow)
                                }
                                .addOnFailureListener {
                                    img_send_post_detail.visibility = View.VISIBLE
                                    progressBarCmt.visibility = View.GONE
                                }
                        }
                }
                else{
                    firebaseFirestore.collection(C_POSTS).document(post.postId).set(post)
                        .addOnSuccessListener {
                            edt_cmt_post_detail.text = convertEditable("")
                            img_send_post_detail.visibility = View.VISIBLE
                            progressBarCmt.visibility = View.GONE
                            img_camera_post_detail.visibility = View.VISIBLE
                            resetImageCmt()
                            Toast.makeText(this, "Đã đăng", Toast.LENGTH_SHORT).show()
                            SendNotificationAPI(post.listTokenFollow)
                        }
                        .addOnFailureListener {
                            img_send_post_detail.visibility = View.VISIBLE
                            progressBarCmt.visibility = View.GONE
                        }
                }



                if (post.userId != mUser.userId){


                    //notifi chủ bài viết -> check listNotify có idPost và idNotify.
                    firebaseFirestore.collection(C_USER).document(post.userId).get().addOnSuccessListener {
                        var ownerPost = it.toObject(UserModel::class.java)
                        //tìm item mới idPost == post.postId để xác định có notify gửi cho chủ tus hay chưa
                        //idNotify dùng để update status
                        var itemListNotifi = ownerPost!!.listNotify.find { it -> it.idPost == post.postId }

                        if (itemListNotifi == null){
                            var notifyModel = NotifyModel(
                                notifyId = GenerateId(),
                                postId = post.postId,
                                userId = ownerPost.userId,
                                yearCreate = time[0],
                                monthCreate = time[1],
                                dayCreate = time[2],
                                hourCreate = time[3],
                                minuteCreate = time[4],
                                secondsCreate = time[5],
                                status = false,
                                content = "Ai đó đã bình luận vào bài viết của bạn",
                                titlePost = post.title
                            )
                            firebaseFirestore.collection(C_NOTIFY).document(notifyModel.notifyId).set(notifyModel)
                                .addOnSuccessListener {
//                                Log.e("abc","create notify owne" )
                                }
                            ownerPost.listNotify.add(ItemListNotifi(notifiId = notifyModel.notifyId, idPost = post.postId))
                            firebaseFirestore.collection(C_USER).document(ownerPost.userId).update("listNotify",ownerPost.listNotify)
                                .addOnSuccessListener {
//                                Log.e("abc","add notify owne" )
                                }

                        }
                        else{

                            var notifyUpdate = NotifyModel(
                                notifyId = itemListNotifi.notifiId,
                                postId = post.postId,
                                userId = ownerPost.userId,
                                yearCreate = time[0],
                                monthCreate = time[1],
                                dayCreate = time[2],
                                hourCreate = time[3],
                                minuteCreate = time[4],
                                secondsCreate = time[5],
                                status = false,
                                content = "Ai đó đã bình luận vào bài viết của bạn",
                                titlePost = post.title
                            )
                            firebaseFirestore.collection(C_NOTIFY).document(notifyUpdate.notifyId)
                                .set(notifyUpdate)
                                .addOnSuccessListener {
//                                Log.e("abc","update false notify owne" )
                                }
                        }

                    }
                }

                //notify người theo dõi -> check list follow
                // id post xác định post follow
                //idNotify == empty hoặc có giá trị xác định đã có thông báo về bài viết đó chưa
                //và idNotify dùng update status
                if (!post.listUserFollow.isEmpty()){
                    firebaseFirestore.collection(C_USER).whereIn("userId",post.listUserFollow).get()
                        .addOnSuccessListener {
                            listUserFollow.clear()
                            listUserFollow.addAll(it.toObjects(UserModel::class.java))

                            listUserFollow.map { item ->

                                if (item.userId != mUser.userId){

                                    var itemlistfl = item.listFollow.find { it.idPost == post.postId}

                                    if (itemlistfl!!.notifiId.isEmpty()){
                                        var notifyModel = NotifyModel(
                                            notifyId = GenerateId(),
                                            postId = post.postId,
                                            userId = item.userId,
                                            yearCreate = time[0],
                                            monthCreate = time[1],
                                            dayCreate = time[2],
                                            hourCreate = time[3],
                                            minuteCreate = time[4],
                                            secondsCreate = time[5],
                                            status = false,
                                            content = "Bình luận mới về bài viết bạn đang theo dõi",
                                            titlePost = post.title
                                        )
                                        firebaseFirestore.collection(C_NOTIFY).document(notifyModel.notifyId).set(notifyModel)
                                            .addOnSuccessListener {
                                                Log.e("abc", "create notify cmt" )
                                            }

                                        item.listNotify.add(ItemListNotifi(notifiId = notifyModel.notifyId))

                                        var index = item.listFollow.indexOf(itemlistfl)
                                        itemlistfl.notifiId = notifyModel.notifyId
                                        item.listFollow.set(index,itemlistfl)

                                        firebaseFirestore.collection(C_USER).document(item.userId).set(item)
                                            .addOnSuccessListener {
//                                            Log.e("abc", "add notificmt listfollow user - list notifi user" )
                                            }

                                    }
                                    else{

                                        var notifyUpdate = NotifyModel(
                                            notifyId = itemlistfl.notifiId,
                                            postId = post.postId,
                                            userId = item.userId,
                                            yearCreate = time[0],
                                            monthCreate = time[1],
                                            dayCreate = time[2],
                                            hourCreate = time[3],
                                            minuteCreate = time[4],
                                            secondsCreate = time[5],
                                            status = false,
                                            content = "Bình luận mới về bài viết bạn đang theo dõi",
                                            titlePost = post.title
                                        )

                                        firebaseFirestore.collection(C_NOTIFY).document(notifyUpdate.notifyId)
                                            .set(notifyUpdate)
                                            .addOnSuccessListener {
//                                            Log.e("abc", "update notify cmt user" )
                                            }

                                    }
                                }



                            }

                        }
                }


            }
        }

        tv_follow_post_detail.setOnClickListener {
            if (isDelete){
                Toast.makeText(this, "Bài viết đã bị xóa", Toast.LENGTH_SHORT).show()
            }
            else{
                tv_follow_post_detail.isVisible = false
                if (mUser.listFollow.find { i -> i.idPost == post.postId } == null){
                    mUser.listFollow.add(ItemListFollow(idPost = post.postId))
                    post.listUserFollow.add(mUser.userId)
                    post.listTokenFollow.add(myTokenNotifi)

                    firebaseFirestore.collection(C_USER).document(mUser.userId).update("listFollow",mUser.listFollow)
                        .addOnSuccessListener {
                            firebaseFirestore.collection(C_POSTS).document(post.postId).set(post)
                                .addOnSuccessListener {
                                    tv_follow_post_detail.setText("Bỏ theo dõi")
                                    tv_follow_post_detail.isVisible = true
                                }
                        }

                }
                else{

                    //xử lí xóa notify

                    mUser.listFollow.remove(mUser.listFollow.find { i -> i.idPost == post.postId })
                    post.listUserFollow.remove(mUser.userId)
                    post.listTokenFollow.remove(myTokenNotifi)

                    firebaseFirestore.collection(C_USER).document(mUser.userId).update("listFollow",mUser.listFollow)
                        .addOnSuccessListener {
                            firebaseFirestore.collection(C_POSTS).document(post.postId).set(post)
                                .addOnSuccessListener {
                                    tv_follow_post_detail.setText("Theo dõi")
                                    tv_follow_post_detail.isVisible = true
                                }
                        }
                }
            }
        }

        img_camera_post_detail.setOnClickListener {
            startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                ),
                GET_FROM_GALLERY
            )
        }

        imgDeleteImageCmt_post_detail.setOnClickListener {
            resetImageCmt()
        }

        GetDataRealtimePost()
    }

    fun resetImageCmt(){
        imgImageCmt_post_detail.visibility = View.GONE
        imgDeleteImageCmt_post_detail.visibility = View.GONE
        URI_IMAGE = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            URI_IMAGE = data?.data!!
            imgImageCmt_post_detail.setImageURI(URI_IMAGE)

            imgImageCmt_post_detail.visibility = View.VISIBLE
            imgDeleteImageCmt_post_detail.visibility = View.VISIBLE
        }
    }

    fun deleteCmt(cmt_delete : CmtModel){
        var dialog = AlertDialog.Builder(this)

        var title = if(mUser.userId == post.userId || cmt_delete.userId == mUser.userId){"Xác nhận xóa bình luận"}else{"Xóa bình luận với quyền quản trị viên"}

        dialog.setMessage(title)

        dialog.setPositiveButton("Đồng ý",
        object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                post.listCmt.remove(cmt_delete)

                if (!cmt_delete.equals("image")){
                    storage.reference.child(cmt_delete.image).delete()
                }

                firebaseFirestore.collection(C_POSTS).document(post.postId).set(post)
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

            if (post.userId == mUser.userId || mUser.admin){
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

        recycleViewCmtAdapter = RecycleViewCmtAdapter(listCmt = post.listCmt, this)
        recycleViewCmt_post_detail.adapter = recycleViewCmtAdapter
        recycleViewCmt_post_detail.layoutManager = LinearLayoutManager(this@PostDetailActivity)
        recycleViewCmt_post_detail.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

        if (post.isComplete == true){
            tv_complete_post_detail.text = "Đã hoàn thành"
        }
        else{
            tv_complete_post_detail.text = "Chưa hoàn thành"
        }

        if (post.isDisableCmt == true){
            img_disable_comment.visibility = View.VISIBLE
            layout_comment_post_detail.isVisible = false
        }
        else{
            img_disable_comment.visibility = View.GONE
            layout_comment_post_detail.isVisible = true
        }

        if (post.listCmt.isEmpty()){
            tv_no_item_post_detail.visibility = View.VISIBLE
            recycleViewCmt_post_detail.visibility = View.GONE
        }
        else{
            tv_no_item_post_detail.visibility = View.GONE
            recycleViewCmt_post_detail.visibility = View.VISIBLE
        }

    }

    @SuppressLint("LongLogTag")
    fun GetDataRealtimePost(){

        var firestore = FirebaseFirestore.getInstance()

        firestore.collection(C_POSTS).addSnapshotListener { value, error ->
            value?.documentChanges?.map {
                var doc : DocumentSnapshot = it.document

                var tmp = doc.toObject(PostsModel::class.java)!!
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
                        if (tmp.postId == post.postId){
                            isDelete = true
                        }
                    }

                }

            }
        }
    }

    override fun onClickLike(cmt: CmtModel) {

        if (isDelete){
            Toast.makeText(this, "Bài viết đã bị xóa", Toast.LENGTH_SHORT).show()
        }
        else{
            var index = post.listCmt.indexOf(cmt)
            var checkLiked = cmt.listLike.find { it == mUser.userId }
            var cmtUpdate = cmt

            if (checkLiked == null){
                cmtUpdate.listLike.add(mUser.userId)
                post.listCmt.set(index, cmtUpdate)
                firebaseFirestore.collection(C_POSTS).document(post.postId).update("listCmt", post.listCmt)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Đã thích", Toast.LENGTH_SHORT).show()
                    }

            }
            else{
                cmtUpdate.listLike.remove(mUser.userId)
                post.listCmt.set(index, cmtUpdate)
                firebaseFirestore.collection(C_POSTS).document(post.postId).update("listCmt", post.listCmt)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Bỏ thích", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

}