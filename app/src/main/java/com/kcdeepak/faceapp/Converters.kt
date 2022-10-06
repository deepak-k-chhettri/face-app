package com.kcdeepak.faceapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64.*
import androidx.room.TypeConverter
import java.nio.ByteBuffer
import java.util.Base64


class Converters{
    @TypeConverter
    fun bitmapToBase64(bitmap: Bitmap) : String{
        // create a ByteBuffer and allocate size equal to bytes in the bitmap
        val byteBuffer = ByteBuffer.allocate(bitmap.height * bitmap.rowBytes)
        //copy all the pixels from bitmap to byteBuffer
        bitmap.copyPixelsToBuffer(byteBuffer)
        //convert byte buffer into byteArray
        val byteArray = byteBuffer.array()
        //convert byteArray to Base64 String with default flags
        return encodeToString(byteArray, DEFAULT)
    }

    @TypeConverter
    fun base64ToBitmap(base64String: String):Bitmap{
        //convert Base64 String into byteArray
        val byteArray = decode(base64String, DEFAULT)
        //byteArray to Bitmap
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }
}