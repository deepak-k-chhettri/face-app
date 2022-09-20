package com.kcdeepak.faceapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MyViewModel(context: Context) : ViewModel(){
    private val dao = UserFaceDatabase.getDatabase(context).userFaceDao()
    private val repository = MyRepository(dao)

    val readAllUsers = repository.readAllUsers
    fun insertFaceUser(userFace: UserFace){
        viewModelScope.launch(Dispatchers.IO){
            repository.addFaceUser(userFace)
        }
    }

    fun deleteFaceUser(userFace: UserFace){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteFaceUser(userFace)
        }
    }
}