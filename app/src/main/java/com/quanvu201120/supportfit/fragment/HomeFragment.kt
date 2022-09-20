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
import android.widget.ImageView
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.activity.PostDetailActivity
import com.quanvu201120.supportfit.activity.mPost
import com.quanvu201120.supportfit.adapter.ListViewPostAdapter
import com.quanvu201120.supportfit.model.CmtModel
import com.quanvu201120.supportfit.model.PostModel


class HomeFragment : Fragment() {

    lateinit var searchView: SearchView
    lateinit var listView: ListView
    lateinit var adapter: ListViewPostAdapter
    lateinit var view_notify_home : View
    lateinit var img_notify_home : ImageView
    lateinit var listPost : ArrayList<PostModel>
    lateinit var listCmt : ArrayList<CmtModel>
    lateinit var listTmpSearch : ArrayList<PostModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)


        searchView = view.findViewById(R.id.searchViewHome)
        listView = view.findViewById(R.id.listViewHome)
        view_notify_home = view.findViewById(R.id.view_notify_home)
        img_notify_home = view.findViewById(R.id.img_notify_home)

        listCmt = ArrayList()
        listPost = ArrayList()
        listTmpSearch = ArrayList()
//        fakeData()
        listPost.addAll(mPost)

        listTmpSearch.addAll(listPost)

        ////ASC
        listTmpSearch.sortWith(compareBy<PostModel> {it.yearCreate}
            .thenBy { it.monthCreate }
            .thenBy { it.dayCreate }
            .thenBy { it.hourCreate }
            .thenBy { it.minuteCreate }
            .thenBy { it.secondsCreate }
        )
        listTmpSearch.reverse()

        adapter = ListViewPostAdapter(requireActivity(),listTmpSearch)

        listView.adapter = adapter

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

                var listFilter = ArrayList<PostModel>()
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

        return view
    }

    fun IntentDetail(postModel: PostModel){
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




