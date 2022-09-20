package com.kcdeepak.faceapp

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MyRepository(private val userFaceDAO: UserFaceDAO) {

    val readAllUsers = userFaceDAO.readAllUsers()
    suspend fun addFaceUser(userFace: UserFace){
        userFaceDAO.addFaceUser(userFace)
    }

    fun deleteFaceUser(userFace: UserFace){
        userFaceDAO.deleteFaceUser(userFace)
    }

    suspend fun deleteAllUsers(){
        userFaceDAO.deleteAllUsers()
    }
}