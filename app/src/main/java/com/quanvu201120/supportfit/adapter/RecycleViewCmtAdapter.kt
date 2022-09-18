package com.quanvu201120.supportfit.adapter

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.model.CmtModel




class RecycleViewCmtAdapter(val listCmt : ArrayList<CmtModel>) :
    RecyclerView.Adapter<RecycleViewCmtAdapter.CmtHoler>() {

    inner class CmtHoler(view: View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener{
        var tv_name : TextView
        var tv_content : TextView
        var tv_dateCreate : TextView

        init {

            tv_name = view.findViewById<TextView>(R.id.tv_nameUser_item_cmt)
            tv_content = view.findViewById<TextView>(R.id.tv_content_item_cmt)
            tv_dateCreate = view.findViewById<TextView>(R.id.tv_dateCreate_item_cmt)

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

    }

    override fun getItemCount(): Int {
        return listCmt.size
    }
}