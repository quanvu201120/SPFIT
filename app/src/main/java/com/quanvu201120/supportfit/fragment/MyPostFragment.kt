package com.quanvu201120.supportfit.fragment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.activity.*
import com.quanvu201120.supportfit.adapter.ListViewPostAdapter
import com.quanvu201120.supportfit.model.CmtModel
import com.quanvu201120.supportfit.model.PostsModel


class MyPostFragment : Fragment() {

    lateinit var searchView_MyPost : SearchView
    lateinit var img_add_MyPost : ImageView
    lateinit var img_account_MyPost : ImageView
    lateinit var listViewMyPost : ListView
    lateinit var tv_no_item_mypost : TextView
    lateinit var adapter : ListViewPostAdapter

    lateinit var listPost : ArrayList<PostsModel>
    lateinit var listCmt : ArrayList<CmtModel>
    lateinit var listTmpSearch : ArrayList<PostsModel>

    lateinit var firebaseFirestore : FirebaseFirestore
    lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_my_post, container, false)

        tv_no_item_mypost = view.findViewById(R.id.tv_no_item_mypost)
        searchView_MyPost = view.findViewById(R.id.searchView_MyPost)
        img_add_MyPost = view.findViewById(R.id.img_add_MyPost)
        img_account_MyPost = view.findViewById(R.id.img_account_MyPost)
        img_account_MyPost = view.findViewById(R.id.img_account_MyPost)
        listViewMyPost = view.findViewById(R.id.listViewMyPost)

        firebaseFirestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

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
        listTmpSearch.sortWith(compareBy<PostsModel> {it.yearCreate}
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


        img_add_MyPost.setOnClickListener {
            startActivityForResult(Intent(requireContext(),CreatePostActivity::class.java),1000) }
        img_account_MyPost.setOnClickListener { startActivity(Intent(requireContext(),AccountActivity::class.java)) }

        check_no_item()

        return view
    }

    fun check_no_item(){
        if (listTmpSearch.isEmpty()){
            tv_no_item_mypost.visibility = View.VISIBLE
            listViewMyPost.visibility = View.INVISIBLE
        }
        else{
            tv_no_item_mypost.visibility = View.GONE
            listViewMyPost.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == 1000 || requestCode == 2000) && resultCode == Activity.RESULT_OK){


            listPost.clear()
            for (item in mPost) {
                if (item.userId == mUser.userId) {
                    listPost.add(item)
                }
            }

            listTmpSearch.clear()
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

            listViewMyPost.adapter = adapter

            check_no_item()
        }

    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        requireActivity().menuInflater.inflate(R.menu.context_menu_my_post,menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    fun deletePost(post : PostsModel){
        var dialog = AlertDialog.Builder(requireContext())

        dialog.setMessage("Xác nhận xóa bài viết")

        dialog.setPositiveButton("Đồng ý",
            object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {

                    post.listCmt.map { item ->
                        if (!item.image.equals("image")){
                            storage.reference.child(item.image).delete()
                        }
                    }

                    if (!post.image.equals("image")){
                        storage.reference.child(post.image).delete()
                            .addOnSuccessListener {
                                firebaseFirestore.collection(C_POSTS).document(post.postId).delete()
                                    .addOnSuccessListener {
                                        removePostmUser(post)
                                    }

                            }
                    }
                    else{
                        firebaseFirestore.collection(C_POSTS).document(post.postId).delete()
                            .addOnSuccessListener {
                                removePostmUser(post)
                            }
                    }
                    listTmpSearch.remove(post)
                    adapter.notifyDataSetChanged()


                }
            })
        dialog.setNegativeButton("Hủy bỏ",object  : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {

            }
        })

        dialog.show()
    }

    fun removePostmUser(post : PostsModel){
        mUser.listPost.remove(post.postId)
        firebaseFirestore.collection(C_USER).document(mUser.userId).update("listPost", mUser.listPost)
            .addOnSuccessListener {
                listTmpSearch.remove(post)
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Đã xóa bài viết", Toast.LENGTH_SHORT).show()
                check_no_item()
            }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        var info : AdapterView.AdapterContextMenuInfo = item.menuInfo as AdapterView.AdapterContextMenuInfo
        var position = info.position

        when(item.itemId){
            R.id.item_update_my_post -> IntentUpdate(position)
            R.id.item_delete_my_post -> deletePost(listTmpSearch[position])
        }

        return super.onContextItemSelected(item)
    }

    fun IntentUpdate(position: Int){
        var postId = listTmpSearch[position].postId
        var intent = Intent(requireContext(), UpdatePostActivity::class.java)
        intent.putExtra("postId",postId)

        startActivityForResult(intent,2000)
    }

    fun IntentDetail(postModel: PostsModel){
        var intent = Intent(requireContext(), PostDetailActivity::class.java)

        intent.putExtra("postId",postModel.postId)

        startActivity(intent)
    }

}