package com.kcdeepak.faceapp

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface UserFaceDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFaceUser(userFace: UserFace)

    @Query("select * from user_face order by id asc")
    fun readAllUsers():LiveData<List<UserFace>>

    @Delete
    fun deleteFaceUser(userFace: UserFace)

    @Query("delete from user_face")
    suspend fun deleteAllUsers()
}