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
import com.quanvu201120.supportfit.model.PostsModel
import com.quanvu201120.supportfit.model.UserModel


class ListViewAdminAdapter(context : Activity, listUser: ArrayList<UserModel>)
    : ArrayAdapter<UserModel>(context,R.layout.item_listview_admin,listUser) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_listview_admin,parent,false)
        var user = getItem(position)

        var tv_name_item_admin = view.findViewById<TextView>(R.id.tv_name_item_admin)
        var tv_email_item_admin = view.findViewById<TextView>(R.id.tv_email_item_admin)
        var tv_role_item_admin = view.findViewById<TextView>(R.id.tv_role_item_admin)

        tv_name_item_admin.text = user?.name
        tv_email_item_admin.text = user?.email
        tv_role_item_admin.text = if(user?.admin == true){"Quản trị viên"}else{"Người dùng"}


        return view
    }
}