package com.quanvu201120.supportfit.activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.adapter.ListViewNotifyAdapter
import com.quanvu201120.supportfit.model.NotifyModel
import com.quanvu201120.supportfit.model.PostModel

class NotifyActivity : AppCompatActivity() {

    lateinit var listview : ListView
    lateinit var adapter: ListViewNotifyAdapter
    lateinit var layout_loading: LinearLayout
    lateinit var tv_no_item_notify : TextView

    lateinit var listNotifi : ArrayList<NotifyModel>

    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)

        firestore = FirebaseFirestore.getInstance()

        listview = findViewById(R.id.listViewNofiti)
        layout_loading = findViewById(R.id.layout_loading)
        tv_no_item_notify = findViewById(R.id.tv_no_item_notify)

        listNotifi = ArrayList()
        listNotifi.addAll(mNotify)

        listNotifi.sortWith(compareBy<NotifyModel> {it.yearCreate}
            .thenBy { it.monthCreate }
            .thenBy { it.dayCreate }
            .thenBy { it.hourCreate }
            .thenBy { it.minuteCreate }
            .thenBy { it.secondsCreate }
        )
        listNotifi.reverse()

        adapter = ListViewNotifyAdapter(this, listNotifi)
        listview.adapter = adapter

        listview.setOnItemClickListener { adapterView, view, i, l ->

            var item = listNotifi[i]

            var check = mPost.find { it -> it.postId == item.postId }
            if (check == null){
                Toast.makeText(this, "Bài viết không còn nữa", Toast.LENGTH_SHORT).show()
            }
            else{
                Log.e("ABC", "" + i + "----" +listNotifi[i].toString() )
                if (item.status == false){
                    layout_loading.visibility = View.VISIBLE
                    firestore.collection(C_NOTIFY).document(item.notifyId).update("status",true)
                        .addOnSuccessListener {
                            IntentDetail(item.postId)
                        }
                }
                else{
                    IntentDetail(item.postId)
                }
            }


        }
        check_no_item()
        registerForContextMenu(listview)

    }

    fun check_no_item(){
        if (listNotifi.isEmpty()){
            tv_no_item_notify.visibility = View.VISIBLE
            listview.visibility = View.INVISIBLE
        }
        else{
            tv_no_item_notify.visibility = View.GONE
            listview.visibility = View.VISIBLE
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu_notify,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var info : AdapterView.AdapterContextMenuInfo = item.menuInfo as AdapterView.AdapterContextMenuInfo
        var position : Int = info.position



        if (item.itemId == R.id.item_delete_notify){
            deleteNotify(position)
        }
        if (item.itemId == R.id.item_status_notify){
            setStatusNotifi(position)
        }

        return super.onContextItemSelected(item)
    }

    fun setStatusNotifi(position:Int){
        layout_loading.visibility = View.VISIBLE
        var tmp = listNotifi.get(position)

        firestore.collection(C_NOTIFY).document(tmp.notifyId).update("status",false)
            .addOnSuccessListener {
                tmp.status = false
                listNotifi.set(position,tmp)
                adapter.notifyDataSetChanged()
                layout_loading.visibility = View.GONE
            }
    }

    fun deleteNotify(position: Int){

        var dialog = AlertDialog.Builder(this)

        dialog.setMessage("Xác nhận xóa thông báo")

        dialog.setPositiveButton("Đồng ý",
            object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    layout_loading.visibility = View.VISIBLE
                    var tmp = listNotifi.get(position)

                    firestore.collection(C_NOTIFY).document(tmp.notifyId).delete()
                        .addOnSuccessListener {

                            var notify_delete = mUser.listNotify.find { i -> i.notifiId == tmp.notifyId }
                            mUser.listNotify.remove(notify_delete)

                            firestore.collection(C_USER).document(mUser.userId).update("listNotify", mUser.listNotify)
                                .addOnSuccessListener {

                                    listNotifi.removeAt(position)
                                    adapter.notifyDataSetChanged()
                                    layout_loading.visibility = View.GONE
                                    Toast.makeText(this@NotifyActivity, "Đã xóa", Toast.LENGTH_SHORT).show()
                                    check_no_item()
                                }

                        }
                }
            })
        dialog.setNegativeButton("Hủy bỏ",object  : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {

            }
        })

        dialog.show()



    }

    fun IntentDetail(id: String){
        var intent = Intent(this,PostDetailActivity::class.java)
        intent.putExtra("postId",id)
        startActivity(intent)
        finish()
    }
}