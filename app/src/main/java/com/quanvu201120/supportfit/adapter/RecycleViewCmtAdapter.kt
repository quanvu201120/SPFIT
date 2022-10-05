package com.quanvu201120.supportfit.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.activity.mUser
import com.quanvu201120.supportfit.model.CmtModel
import java.io.File


interface onClickLikeItem{
    fun onClickLike(cmt : CmtModel)
}

class RecycleViewCmtAdapter(val listCmt : ArrayList<CmtModel>, var listener : onClickLikeItem) :
    RecyclerView.Adapter<RecycleViewCmtAdapter.CmtHoler>() {
    lateinit var storage : FirebaseStorage

    inner class CmtHoler(view: View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener{
        var tv_name : TextView
        var tv_content : TextView
        var tv_count_like_item_cmt : TextView
        var tv_dateCreate : TextView
        var img_item_liss_cmt : ImageView
        var img_like_1_item_cmt : ImageView
        var img_like_0_item_cmt : ImageView

        init {
            storage = FirebaseStorage.getInstance()
            tv_name = view.findViewById<TextView>(R.id.tv_nameUser_item_cmt)
            tv_content = view.findViewById<TextView>(R.id.tv_content_item_cmt)
            tv_dateCreate = view.findViewById<TextView>(R.id.tv_dateCreate_item_cmt)
            img_item_liss_cmt = view.findViewById<ImageView>(R.id.img_item_liss_cmt)
            img_like_1_item_cmt = view.findViewById<ImageView>(R.id.img_like_1_item_cmt)
            img_like_0_item_cmt = view.findViewById<ImageView>(R.id.img_like_0_item_cmt)
            tv_count_like_item_cmt = view.findViewById<TextView>(R.id.tv_count_like_item_cmt)

            img_like_0_item_cmt.setOnClickListener { listener.onClickLike( listCmt[adapterPosition] ) }
            img_like_1_item_cmt.setOnClickListener { listener.onClickLike( listCmt[adapterPosition] ) }

            view.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            p0: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?,
        ) {
            p0?.add(adapterPosition,100,100,"Xóa bình luận")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CmtHoler {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_cmt,parent,false)

        return CmtHoler(view)
    }

    override fun onBindViewHolder(holder: CmtHoler, position: Int) {
        var cmt = listCmt.get(position)
        holder.tv_name.text = cmt?.nameUser
        holder.tv_content.text = cmt?.content
        holder.tv_dateCreate.text = "${cmt?.dayCreate}/${cmt?.monthCreate}/${cmt?.yearCreate}  ${cmt?.hourCreate}:${cmt?.minuteCreate}:${cmt?.secondsCreate}"

        var count = cmt.listLike.size
        var isRecognized = cmt.listLike.find { it == cmt.ownerPostId } //nếu có id chủ posts trong đó là đã được công nhận

        if (count == 0){
            holder.img_like_0_item_cmt.visibility = View.VISIBLE
            holder.img_like_1_item_cmt.visibility = View.GONE
            holder.tv_count_like_item_cmt.text = ""
        }
        else{
            var textCount = if(count == 1 && isRecognized != null){
                                "Chủ bài viết"
                            }
                            else{
                                if (isRecognized != null){
                                   "${count-1} và chủ bài viết"
                                }else{
                                    "${count}"
                                }
                            }
            var checkLiked = cmt.listLike.find { it == mUser.userId }

            if (checkLiked != null){
                holder.img_like_0_item_cmt.visibility = View.GONE
                holder.img_like_1_item_cmt.visibility = View.VISIBLE
            }
            else{
                holder.img_like_0_item_cmt.visibility = View.VISIBLE
                holder.img_like_1_item_cmt.visibility = View.GONE
            }
            holder.tv_count_like_item_cmt.text = textCount

        }

        if (!cmt.image.equals("image")){
            holder.img_item_liss_cmt.visibility = View.VISIBLE

            var image_file : File = File.createTempFile("get_image",".png")

            var storageReference : StorageReference = storage.reference

            //truyền vào tên file
            storageReference.child(cmt.image)
                //đây là get nên truyền vào file nhận ảnh sau khi thực hiện getFile
                .getFile(image_file)
                .addOnSuccessListener {

                    var bitmap : Bitmap = BitmapFactory.decodeFile(image_file.path)
                    holder.img_item_liss_cmt.setImageBitmap(bitmap)
                }
        }
    }

    override fun getItemCount(): Int {
        return listCmt.size
    }
}