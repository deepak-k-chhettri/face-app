package com.kcdeepak.faceapp

import androidx.lifecycle.LiveData

class MyRepository(private val userFaceDAO: UserFaceDAO) {

    val readAllUsers = userFaceDAO.readAllUsers()
//    val rowCount = userFaceDAO.getRowCount()
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