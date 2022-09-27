package com.kcdeepak.faceapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Base64.*
import android.util.Log
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters {
//    @TypeConverter
//    fun fromBitmap(bitmap: Bitmap):ByteArray{
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream)
//        Log.d("TAG", "fromBitmap: ${byteArrayOutputStream.toByteArray().size}")
//        return byteArrayOutputStream.toByteArray()
//    }
//
//    @TypeConverter
//    fun toBitmap(byteArray: ByteArray):Bitmap{
//        Log.d("TAG", "toBitmap: ${BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)}")
//        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
//    }
////    @TypeConverter
////    fun base64StringToBitmap(base64String: String):Bitmap{
////        val byteArray = decode(base64String, DEFAULT)
////        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
////    }

    @TypeConverter
    fun fromUriToString(imageUri: Uri):String{
        return imageUri.toString()
    }

    @TypeConverter
    fun fromStringToUri(string: String):Uri{
        return Uri.parse(string)
    }
}