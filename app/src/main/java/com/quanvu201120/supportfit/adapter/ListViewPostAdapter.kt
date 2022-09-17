package com.quanvu201120.supportfit.adapter

import android.app.Activity
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.model.PostModel


class ListViewPostAdapter(context : Activity, listPost: ArrayList<PostModel>)
    : ArrayAdapter<PostModel>(context,R.layout.item_list_post,listPost) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_post,parent,false)
        var post = getItem(position)

        var image = view.findViewById<ImageView>(R.id.image_item_post)
        var tv_title = view.findViewById<TextView>(R.id.tv_title_item_post)
        var tv_dateCreate = view.findViewById<TextView>(R.id.tv_dateCreate_item_post)

        image.setImageResource(R.drawable.logo)
        tv_title.text = post?.title
        tv_dateCreate.text = " ${post?.dateCreate?.get(0)}/${post?.dateCreate?.get(1)}/${post?.dateCreate?.get(2)}  ${post?.dateCreate?.get(3)}:${post?.dateCreate?.get(4)}:${post?.dateCreate?.get(5)}"


        return view
    }
}