package com.quanvu201120.supportfit.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.activity.mUser
import com.quanvu201120.supportfit.model.ItemListNotifi
import com.quanvu201120.supportfit.model.NotifyModel


class ListViewNotifyAdapter(context : Activity, listNotify: ArrayList<NotifyModel>)
    : ArrayAdapter<NotifyModel>(context,R.layout.item_listview_notify,listNotify) {

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_listview_notify,parent,false)
        var notifi = getItem(position)

        var image = view.findViewById<ImageView>(R.id.img_item_notify)
        var tv_title_false = view.findViewById<TextView>(R.id.tv_title_item_notify_false)
        var tv_title_true = view.findViewById<TextView>(R.id.tv_title_item_notify_true)
        var tv_time_item_notify = view.findViewById<TextView>(R.id.tv_time_item_notify)

        var checkMyPost  = mUser.listNotify?.find { it -> it.notifiId == notifi?.notifyId }?.idPost?.isEmpty()


        if (checkMyPost == true){
            image.setImageResource(R.drawable.icon_cmt)
        }
        else{
            image.setImageResource(R.drawable.icon_person)
        }

        if (notifi?.status == true){
            tv_title_false.visibility = View.GONE
            tv_title_true.visibility = View.VISIBLE

            tv_title_true.text = notifi?.content + ": " + notifi?.titlePost
        }
        else{
            tv_title_false.visibility = View.VISIBLE
            tv_title_false.text = notifi?.content + ": " + notifi?.titlePost

            tv_title_true.visibility = View.GONE
        }

        tv_time_item_notify.text = "${notifi?.dayCreate}/${notifi?.monthCreate}/${notifi?.yearCreate}  ${notifi?.hourCreate}:${notifi?.minuteCreate}:${notifi?.secondsCreate}"

        return view
    }
}