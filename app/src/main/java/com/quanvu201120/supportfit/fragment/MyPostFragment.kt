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



    fun IntentDetail(postModel: PostModel){
        var intent = Intent(requireContext(), PostDetailActivity::class.java)

        intent.putExtra("postId",postModel.postId)

        startActivity(intent)
    }

}