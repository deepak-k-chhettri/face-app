package com.kcdeepak.faceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    lateinit var fabDeleteAll:FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel = MyViewModel(applicationContext)
        myAdapter = MyAdapter(this)
        fabDeleteAll = findViewById(R.id.fabDelAll)

        recyclerView = findViewById(R.id.container)
        recyclerView.adapter = myAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        myViewModel.readAllUsers.observe(this) { it ->
            it?.let { myAdapter.updateList(it) }
        }

        fab = findViewById(R.id.fab)
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
                    //myAdapter.notifyItemInserted(position)
                }.show()
            }

        }).attachToRecyclerView(recyclerView)

        fabDeleteAll.setOnClickListener {
            myViewModel.deleteAllUsers()
        }
    }
}