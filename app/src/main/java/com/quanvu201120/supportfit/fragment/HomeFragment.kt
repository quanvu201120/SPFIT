package com.quanvu201120.supportfit.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.activity.*
import com.quanvu201120.supportfit.adapter.ListViewPostAdapter
import com.quanvu201120.supportfit.model.CmtModel
import com.quanvu201120.supportfit.model.NotifyModel
import com.quanvu201120.supportfit.model.PostsModel


class HomeFragment : Fragment() {

    lateinit var searchView: SearchView
    lateinit var listView: ListView
    lateinit var adapter: ListViewPostAdapter
    lateinit var view_notify_home : View
    lateinit var img_notify_home : ImageView
    lateinit var img_reload_home : ImageView
    lateinit var tv_no_item_home : TextView
    lateinit var listPost : ArrayList<PostsModel>
    lateinit var listCmt : ArrayList<CmtModel>
    lateinit var listTmpSearch : ArrayList<PostsModel>

    lateinit var firestore : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)


        img_reload_home = view.findViewById(R.id.img_reload_home)
        tv_no_item_home = view.findViewById(R.id.tv_no_item_home)
        searchView = view.findViewById(R.id.searchViewHome)
        listView = view.findViewById(R.id.listViewHome)
        view_notify_home = view.findViewById(R.id.view_notify_home)
        img_notify_home = view.findViewById(R.id.img_notify_home)

        firestore = FirebaseFirestore.getInstance()

        listCmt = ArrayList()
        listPost = ArrayList()
        listTmpSearch = ArrayList()
//        fakeData()
        listPost.addAll(mPost)

        listTmpSearch.addAll(listPost)

        ////ASC
        listTmpSearch.sortWith(compareBy<PostsModel> {it.yearCreate}
            .thenBy { it.monthCreate }
            .thenBy { it.dayCreate }
            .thenBy { it.hourCreate }
            .thenBy { it.minuteCreate }
            .thenBy { it.secondsCreate }
        )
        listTmpSearch.reverse()

        adapter = ListViewPostAdapter(requireActivity(),listTmpSearch)

        listView.adapter = adapter

        var isNotifi = true
        for ( i in mNotify){
            if (i.status == false){
                isNotifi = false
                break
            }
        }
        if (isNotifi == false){
            view_notify_home.visibility = View.VISIBLE
        }

        else{
            view_notify_home.visibility = View.GONE
        }
        Log.e("abc",""+isNotifi)
        listView.setOnItemClickListener { adapterView, view, i, l ->
            IntentDetail(listTmpSearch[i])
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                listTmpSearch.clear()
                listTmpSearch.addAll(listPost)

                var listFilter = ArrayList<PostsModel>()
                for (item in listTmpSearch) {
                    if (item.title!!.lowercase().contains(p0!!.lowercase())) {
                        listFilter.add(item)
                    }
                }

                listTmpSearch.clear()
                listTmpSearch.addAll(listFilter)
                adapter.notifyDataSetChanged()

                return false
            }
        })

        img_reload_home.setOnClickListener {
            listPost.clear()
            listTmpSearch.clear()
            listPost.addAll(mPost)
            listTmpSearch.addAll(listPost)

            ////ASC
            listTmpSearch.sortWith(compareBy<PostsModel> {it.yearCreate}
                .thenBy { it.monthCreate }
                .thenBy { it.dayCreate }
                .thenBy { it.hourCreate }
                .thenBy { it.minuteCreate }
                .thenBy { it.secondsCreate }
            )
            listTmpSearch.reverse()

            adapter = ListViewPostAdapter(requireActivity(),listTmpSearch)

            listView.adapter = adapter
            Toast.makeText(requireContext(), "Đã cập nhật", Toast.LENGTH_SHORT).show()
        }

        img_notify_home.setOnClickListener {
            startActivity(Intent(requireContext(),NotifyActivity::class.java))
        }
        check_no_item()
        GetDataRealtimeNotify()
        return view
    }

    fun check_no_item(){
        if (listTmpSearch.isEmpty()){
            tv_no_item_home.visibility = View.VISIBLE
            listView.visibility = View.INVISIBLE
        }
        else{
            tv_no_item_home.visibility = View.GONE
            listView.visibility = View.VISIBLE
        }
    }

    fun IntentDetail(postModel: PostsModel){

        var checkDelete = mPost.find { it -> it.postId == postModel.postId }
        if (checkDelete == null){
            Toast.makeText(requireContext(), "Bài viết đã bị xóa", Toast.LENGTH_SHORT).show()
        }
        else{
            var intent = Intent(requireContext(),PostDetailActivity::class.java)

            intent.putExtra("postId",postModel.postId)
//        intent.putExtra("image",postModel.image)
//        intent.putExtra("userId",postModel.userId)
//        intent.putExtra("nameUser",postModel.nameUser)
//        intent.putExtra("yearCreate",postModel.yearCreate)
//        intent.putExtra("monthCreate",postModel.monthCreate)
//        intent.putExtra("dayCreate",postModel.dayCreate)
//        intent.putExtra("hourCreate",postModel.hourCreate)
//        intent.putExtra("minuteCreate",postModel.minuteCreate)
//        intent.putExtra("secondsCreate",postModel.secondsCreate)
//        intent.putExtra("title",postModel.title)
//        intent.putExtra("description",postModel.description)
//        intent.putParcelableArrayListExtra("listCmt",postModel.listCmt)
//        intent.putStringArrayListExtra("listUserFollow",postModel.listUserFollow)
//        intent.putStringArrayListExtra("listTokenFollow",postModel.listTokenFollow)
//        intent.putExtra("image",postModel.image)

            startActivity(intent)
        }


    }
    fun GetDataRealtimeNotify( ){
        firestore.collection(C_NOTIFY).addSnapshotListener { value, error ->
            value?.documentChanges?.map {
                var doc: DocumentSnapshot = it.document

                var tmp = doc.toObject(NotifyModel::class.java)!!

                var check = tmp.userId == mUser.userId

                var isNull = true

                if (check){
                    for ( i in mNotify){
                        if (i.status == false){
                            isNull = false
                            break
                        }
                    }
                    if (isNull){
                        view_notify_home.visibility = View.GONE
                    }
                    else{
                        view_notify_home.visibility = View.VISIBLE
                    }
                }


            }

        }
    }
}




