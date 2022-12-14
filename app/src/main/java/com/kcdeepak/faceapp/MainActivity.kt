package com.kcdeepak.faceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var fab:FloatingActionButton
    private lateinit var myViewModel: MyViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var myAdapter: MyAdapter
    lateinit var fabDelAll:FloatingActionButton
    lateinit var empty:View
    lateinit var encryptDecrypt: EncryptDecrypt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        encryptDecrypt = EncryptDecrypt.getInstance()
//
//        val plainText = "Android"
//        val cipherText = encryptDecrypt.encrypt(plainText.toByteArray())
//        Log.d("MainActivity", String(cipherText))
//
//        val decryptedText = encryptDecrypt.decrypt(cipherText)
//        Log.d("MainActivity", String(decryptedText))

        initialMyUI()

        val emptyDataObserver = EmptyDataObserver(recyclerView,empty)
        myAdapter.registerAdapterDataObserver(emptyDataObserver)

        functionalities()
    }

    fun functionalities(){
        myViewModel.readAllUsers.observe(this, Observer {
                it -> it?.let { myAdapter.updateList(it)}
        })

        fab.setOnClickListener {
            intent = Intent(this,AddNewUserFace::class.java)
            startActivity(intent)
            finish()
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val userFace = myAdapter.getUserFaceAt(position)
                myViewModel.deleteFaceUser(userFace)
                Snackbar.make(recyclerView,"User Face Deleted",5000).setAction("UNDO"
                ) {
                    myViewModel.insertFaceUser(userFace)
                    myAdapter.notifyItemInserted(position)
                }.show()
            }

        }).attachToRecyclerView(recyclerView)

        fabDelAll.setOnClickListener {
            myViewModel.deleteAllUsers()
        }
    }

    private fun initialMyUI(){
        myViewModel = MyViewModel(applicationContext)
        myAdapter = MyAdapter(this)

        recyclerView = findViewById(R.id.container)
        recyclerView.adapter = myAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        fabDelAll  = findViewById(R.id.fabDelAll)
        fab = findViewById(R.id.fab)
        empty = findViewById(R.id.empty_data_parent)
    }
}