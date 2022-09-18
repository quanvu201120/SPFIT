package com.quanvu201120.supportfit.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.activity.*
import com.quanvu201120.supportfit.adapter.ListViewPostAdapter
import com.quanvu201120.supportfit.model.CmtModel
import com.quanvu201120.supportfit.model.PostModel


class MyPostFragment : Fragment() {

    lateinit var searchView_MyPost : SearchView
    lateinit var img_add_MyPost : ImageView
    lateinit var img_account_MyPost : ImageView
    lateinit var listViewMyPost : ListView
    lateinit var adapter : ListViewPostAdapter

    lateinit var listPost : ArrayList<PostModel>
    lateinit var listCmt : ArrayList<CmtModel>
    lateinit var listTmpSearch : ArrayList<PostModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_my_post, container, false)

        searchView_MyPost = view.findViewById(R.id.searchView_MyPost)
        img_add_MyPost = view.findViewById(R.id.img_add_MyPost)
        img_account_MyPost = view.findViewById(R.id.img_account_MyPost)
        img_account_MyPost = view.findViewById(R.id.img_account_MyPost)
        listViewMyPost = view.findViewById(R.id.listViewMyPost)


        listCmt = ArrayList()
        listPost = ArrayList()
        listTmpSearch = ArrayList()
//        fakeData()

        for (item in mPost) {
            if (item.userId == mUser.userId) {
                listPost.add(item)
            }
        }

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

        listViewMyPost.adapter = adapter

        listViewMyPost.setOnItemClickListener { adapterView, view, i, l ->
            IntentDetail(listTmpSearch[i])
        }

        registerForContextMenu(listViewMyPost)

        searchView_MyPost.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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


        img_add_MyPost.setOnClickListener { startActivity(Intent(requireContext(),CreatePostActivity::class.java)) }
        img_account_MyPost.setOnClickListener { startActivity(Intent(requireContext(),AccountActivity::class.java)) }

        return view
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        requireActivity().menuInflater.inflate(R.menu.context_menu_my_post,menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        var info : AdapterView.AdapterContextMenuInfo = item.menuInfo as AdapterView.AdapterContextMenuInfo
        var position = info.position

        when(item.itemId){
            R.id.item_update_my_post -> Toast.makeText(requireContext(), "update " + listTmpSearch[position].title, Toast.LENGTH_SHORT).show()
            R.id.item_delete_my_post -> Toast.makeText(requireContext(), "delete " + listTmpSearch[position].title, Toast.LENGTH_SHORT).show()
        }

        return super.onContextItemSelected(item)
    }

    fun fakeData(){


        var cmtModel1 = CmtModel(
            cmtId = "1",
            userId = "2",
            postId = "3",
            content = "cmt ne",
            yearCreate = 2022,
            monthCreate = 9,
            dayCreate = 9,
            hourCreate = 21,
            minuteCreate = 20,
            secondsCreate = 19,
            nameUser = "Vu The Quan"
        )
        var cmtModel2 = CmtModel(
            cmtId = "12",
            userId = "22",
            postId = "32",
            content = "cmt ne2",
            yearCreate = 2022,
            monthCreate = 9,
            dayCreate = 9,
            hourCreate = 21,
            minuteCreate = 15,
            secondsCreate = 15,
            nameUser = "Vu The Quan2"
        )
        var cmtModel3 = CmtModel(
            cmtId = "13",
            userId = "23",
            postId = "33",
            content = "cmt ne3",
            yearCreate = 2022,
            monthCreate = 9,
            dayCreate = 9,
            hourCreate = 22,
            minuteCreate = 22,
            secondsCreate = 19,
            nameUser = "Vu The Quan3"
        )
        var cmtModel4 = CmtModel(
            cmtId = "14",
            userId = "24",
            postId = "34",
            content = "cmt ne4",
            yearCreate = 2022,
            monthCreate = 11,
            dayCreate = 9,
            hourCreate = 11,
            minuteCreate = 20,
            secondsCreate = 22,
            nameUser = "Vu The Quan4"
        )


        listCmt.add(cmtModel1)
        listCmt.add(cmtModel2)
        listCmt.add(cmtModel3)
        listCmt.add(cmtModel4)
        listCmt.add(cmtModel1)
        listCmt.add(cmtModel2)
        listCmt.add(cmtModel3)
        listCmt.add(cmtModel4)

        var post1 = PostModel(
            postId = "postidquanvu201120",
            userId = "useridquanvu201120",
            yearCreate = 2022,
            monthCreate = 9,
            dayCreate = 9,
            hourCreate = 21,
            minuteCreate = 20,
            secondsCreate = 19,
            title = "Test title 1",
            description = "Test description 1",
            image = "/image",
            nameUser = "Nguyen van a",
            listCmt = listCmt

        )
        var post2 = PostModel(
            postId = "postidquanvu201120",
            userId = "useridquanvu201120",
            yearCreate = 2022,
            monthCreate = 9,
            dayCreate = 9,
            hourCreate = 21,
            minuteCreate = 15,
            secondsCreate = 15,
            title = "Test title 2",
            description = "Test description 2",
            image = "/image",
            nameUser = "Nguyen van a",
            listCmt = listCmt
        )
        var post3 = PostModel(
            postId = "postidquanvu201120",
            userId = "useridquanvu201120",
            yearCreate = 2022,
            monthCreate = 9,
            dayCreate = 9,
            hourCreate = 22,
            minuteCreate = 22,
            secondsCreate = 19,
            title = "Test title 3",
            description = "Test description 3",
            image = "/image",
            nameUser = "Nguyen van a",
            listCmt = listCmt
        )
        var post4 = PostModel(
            postId = "postidquanvu201120",
            userId = "useridquanvu201120",
            yearCreate = 2022,
            monthCreate = 11,
            dayCreate = 9,
            hourCreate = 11,
            minuteCreate = 20,
            secondsCreate = 22,
            title = "Test title 4",
            description = "Test description 4",
            image = "/image",
            nameUser = "Nguyen van a",
            listCmt = listCmt
        )

        listPost.add(post3)
        listPost.add(post2)
        listPost.add(post1)
        listPost.add(post4)
        listPost.add(post3)
        listPost.add(post2)
        listPost.add(post1)
        listPost.add(post4)
        listPost.add(post3)
        listPost.add(post2)
        listPost.add(post1)
        listPost.add(post4)

    }

    fun IntentDetail(postModel: PostModel){
        var intent = Intent(requireContext(), PostDetailActivity::class.java)

        intent.putExtra("postId",postModel.postId)
        intent.putExtra("userId",postModel.userId)
        intent.putExtra("nameUser",postModel.nameUser)
        intent.putExtra("yearCreate",postModel.yearCreate)
        intent.putExtra("monthCreate",postModel.monthCreate)
        intent.putExtra("dayCreate",postModel.dayCreate)
        intent.putExtra("hourCreate",postModel.hourCreate)
        intent.putExtra("minuteCreate",postModel.minuteCreate)
        intent.putExtra("secondsCreate",postModel.secondsCreate)
        intent.putExtra("title",postModel.title)
        intent.putExtra("description",postModel.description)
        intent.putParcelableArrayListExtra("listCmt",postModel.listCmt)
        intent.putStringArrayListExtra("listUserFollow",postModel.listUserFollow)
        intent.putStringArrayListExtra("listTokenFollow",postModel.listTokenFollow)

        intent.putExtra("image",postModel.image)

        startActivity(intent)
    }

}