package com.quanvu201120.supportfit.adapter

import android.app.Activity
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.model.PostModel


class ListViewPostAdapter(context : Activity, listPost: ArrayList<PostModel>)
    : ArrayAdapter<PostModel>(context,R.layout.item_list_post,listPost) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_post,parent,false)
        var post = getItem(position)

        var image = view.findViewById<TextView>(R.id.image_item_post)
        var tv_title = view.findViewById<TextView>(R.id.tv_title_item_post)
        var tv_dateCreate = view.findViewById<TextView>(R.id.tv_dateCreate_item_post)
        var layout_item_post = view.findViewById<ConstraintLayout>(R.id.layout_item_post)

        image.text = post?.title!![0].toString()
        tv_title.text = post?.title
        tv_dateCreate.text = "${post?.dayCreate}/${post?.monthCreate}/${post?.yearCreate}  ${post?.hourCreate}:${post?.minuteCreate}:${post?.secondsCreate}"

        if (position % 2 == 0){
            layout_item_post.setBackgroundColor(context.resources.getColor(R.color.blue10))
        }


        return view
    }
}