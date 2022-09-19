package com.kcdeepak.faceapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@TypeConverters(Converters::class)
@Database(entities = [UserFace::class], version = 1)
abstract class UserFaceDatabase:RoomDatabase() {
    abstract fun userFaceDao():UserFaceDAO

    companion object{
        @Volatile
        private var INSTANCE: UserFaceDatabase ?= null

        fun getDatabase(context: Context):UserFaceDatabase =
            INSTANCE?: synchronized(this){
                INSTANCE?: buildBatabase(context).also {
                    INSTANCE = it
                }
            }
        fun buildBatabase(context: Context) =
            Room.databaseBuilder(context,
                UserFaceDatabase::class.java,
                "face_user_database").build()
    }
}