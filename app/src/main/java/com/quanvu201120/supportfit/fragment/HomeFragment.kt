package com.quanvu201120.supportfit.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.adapter.ListViewPostAdapter
import com.quanvu201120.supportfit.model.CmtModel
import com.quanvu201120.supportfit.model.PostModel


class HomeFragment : Fragment() {

    lateinit var searchView: androidx.appcompat.widget.SearchView
    lateinit var listView: ListView
    lateinit var adapter: ListViewPostAdapter
    lateinit var listPost : ArrayList<PostModel>
    lateinit var listCmt : ArrayList<CmtModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)


        searchView = view.findViewById(R.id.searchViewHome)
        listView = view.findViewById(R.id.listViewHome)

        listCmt = ArrayList()
        listPost = ArrayList()
        fakeData()

        adapter = ListViewPostAdapter(requireActivity(),listPost)

        listView.adapter = adapter

        return view
    }

fun fakeData(){
    var dateCreateList = ArrayList<Int>()
    dateCreateList.add(19)
    dateCreateList.add(9)
    dateCreateList.add(2022)
    dateCreateList.add(21)
    dateCreateList.add(12)
    dateCreateList.add(44)

    var cmtModel = CmtModel(
        cmtId = "1",
        userId = "2",
        postId = "3",
        content = "cmt ne",
        dateCreate = dateCreateList,
        nameUser = "Vu The Quan"
    )
    listCmt.add(cmtModel)
    listCmt.add(cmtModel)
    listCmt.add(cmtModel)

    var post = PostModel(
        postId = "postidquanvu201120",
        userId = "useridquanvu201120",
        dateCreate = dateCreateList,
        title = "Test title 3",
        description = "Test description 2",
        image = "/image",
        nameUser = "Nguyen van a"
    )
    listPost.add(post)
    listPost.add(post)
    listPost.add(post)
    listPost.add(post)
}


}




//    val GET_FROM_GALLERY = 3;
//    lateinit var URI_IMAGE : Uri
//    lateinit var imageView : ImageView


//        imageView = view.findViewById(R.id.imageView4)
//        imageView.setOnClickListener {
//            startActivityForResult(
//                Intent(
//                    Intent.ACTION_PICK,
//                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
//                ),
//                GET_FROM_GALLERY
//            )
//        }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
//            URI_IMAGE = data?.data!!
//            imageView.setImageURI(URI_IMAGE)
//        }
//    }