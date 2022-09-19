package com.kcdeepak.faceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var fab:FloatingActionButton
    private lateinit var myViewModel: MyViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var myAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel = MyViewModel(applicationContext)
        myAdapter = MyAdapter(this)

        recyclerView = findViewById(R.id.container)
        recyclerView.adapter = myAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        myViewModel.readAllUsers.observe(this, Observer {
            it -> it?.let { myAdapter.updateList(it)}
        })

        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            intent = Intent(this,AddNewUserFace::class.java)
            startActivity(intent)
            finish()
        }
    }
}