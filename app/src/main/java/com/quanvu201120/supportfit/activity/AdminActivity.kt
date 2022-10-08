package com.quanvu201120.supportfit.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.quanvu201120.supportfit.R
import com.quanvu201120.supportfit.adapter.ListViewAdminAdapter
import com.quanvu201120.supportfit.model.PostsModel
import com.quanvu201120.supportfit.model.UserModel

class AdminActivity : AppCompatActivity() {

    lateinit var searchViewAdmin : SearchView
    lateinit var checkBoxAdmin : CheckBox
    lateinit var listViewAdmin : ListView

    lateinit var listTmpSearch : ArrayList<UserModel>
    lateinit var adapter : ListViewAdminAdapter

    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        searchViewAdmin = findViewById(R.id.searchViewAdmin)
        checkBoxAdmin = findViewById(R.id.checkBoxAdmin)
        listViewAdmin = findViewById(R.id.listViewAdmin)

        firestore = Firebase.firestore

        listTmpSearch = ArrayList()
        listTmpSearch.clear()
        listTmpSearch.addAll(mListUser)

        adapter = ListViewAdminAdapter(this, listTmpSearch)
        listViewAdmin.adapter = adapter

        checkBoxAdmin.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                listTmpSearch.clear()
                listTmpSearch.addAll(mListUser.filter { it.admin == true })
                adapter.notifyDataSetChanged()
            }
            else{
                listTmpSearch.clear()
                listTmpSearch.addAll(mListUser)
                adapter.notifyDataSetChanged()
            }
        }

        searchViewAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                listTmpSearch.clear()
                listTmpSearch.addAll(mListUser)

                var listFilter = ArrayList<UserModel>()
                for (item in listTmpSearch) {
                    if (item.name!!.lowercase().contains(p0!!.lowercase())) {
                        listFilter.add(item)
                    }
                }

                listTmpSearch.clear()
                listTmpSearch.addAll(listFilter)
                adapter.notifyDataSetChanged()

                return false
            }
        })

        listViewAdmin.setOnItemClickListener { adapterView, view, i, l ->
            var user = listTmpSearch.get(i)

            var isAdminChange = false

            var dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_role_admin)

            var radioGroup_dialog_role = dialog.findViewById<RadioGroup>(R.id.radioGroup_dialog_role)
            var btnSubmitDialogRoleAdmin = dialog.findViewById<Button>(R.id.btnSubmitDialogRoleAdmin)
            var tv_email_dialog_role = dialog.findViewById<TextView>(R.id.tv_email_dialog_role)
            var radioButtonAdmin = dialog.findViewById<RadioButton>(R.id.radioButtonAdmin)
            var radioButtonCustomer = dialog.findViewById<RadioButton>(R.id.radioButtonCustomer)
            var progressBarAdmin = dialog.findViewById<ProgressBar>(R.id.progressBarAdmin)


            if (user.admin){
                radioButtonAdmin.isChecked = true
            }
            else{
                radioButtonCustomer.isChecked = true
            }

            tv_email_dialog_role.text = user.email

            radioGroup_dialog_role.setOnCheckedChangeListener { radioGroup, i ->
                when(i){
                    R.id.radioButtonAdmin -> {isAdminChange = true}
                    R.id.radioButtonCustomer -> {isAdminChange = false}
                }
            }

            btnSubmitDialogRoleAdmin.setOnClickListener {
                if (user.admin != isAdminChange){
                    progressBarAdmin.visibility = View.VISIBLE
                    btnSubmitDialogRoleAdmin.visibility = View.VISIBLE

                    user.admin = isAdminChange
                    firestore.collection(C_USER).document(user.userId).update("admin",user.admin)
                        .addOnSuccessListener {

                            listTmpSearch.clear()
                            listTmpSearch.addAll(mListUser)
                            adapter = ListViewAdminAdapter(this, listTmpSearch)
                            listViewAdmin.adapter = adapter

                            dialog.dismiss()
                        }
                }
                else{
                    dialog.dismiss()
                }
            }

            dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.show()

        }



    }
}