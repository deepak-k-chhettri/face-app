package com.kcdeepak.faceapp

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_face")
data class UserFace(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val name:String,
    val phone:String,
    val address:String,
    val imageUri : String
)