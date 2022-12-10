package com.quanvu201120.supportfit.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.model.ItemUserCSV
import com.quanvu201120.supportfit.model.NotifyModel
import org.w3c.dom.Text

class ListViewUserCSV(context : Activity, listNotify: ArrayList<ItemUserCSV>)
    : ArrayAdapter<ItemUserCSV>(context, R.layout.item_listview_notify,listNotify)  {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_listview_user_csv,parent,false)
        var user = getItem(position)

        var tv_name_item_listview_csv = view.findViewById<TextView>(R.id.tv_name_item_listview_csv)
        var tv_email_item_listview_csv = view.findViewById<TextView>(R.id.tv_email_item_listview_csv)

        tv_name_item_listview_csv.text = user?.name
        tv_email_item_listview_csv.text = user?.email

        return  view
    }
}