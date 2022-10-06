package com.kcdeepak.faceapp

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.Base64.getDecoder


class MyAdapter(val context: Context) :RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private val usersFace = ArrayList<UserFace>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.face_user_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = EncryptDecrypt().decrypt(usersFace[position].name,usersFace[position].nameIV)
        val phone = EncryptDecrypt().decrypt(usersFace[position].phone,usersFace[position].phoneIV)
        val address = EncryptDecrypt().decrypt(usersFace[position].address,usersFace[position].addressIV)
        val imageUri = EncryptDecrypt().decrypt(usersFace[position].imageUri,usersFace[position].imageUriIV)
        Glide.with(context)
            .load(Uri.parse(String(imageUri)))
            .into(holder.imageViewSmall)
        Glide.with(context)
            .load(Uri.parse(String(imageUri)))
            .into(holder.imageViewLarge)
        holder.textViewName.text = String(name)
        holder.textViewPhone.text = String(phone)
        holder.textViewAddress.text = String(address)
    }

    override fun getItemCount(): Int {
        return usersFace.size
    }

    fun getUserFaceAt(position: Int):UserFace{
        return usersFace[position]
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewSmall: ImageView = itemView.findViewById(R.id.imageViewSmall)
        val imageViewLarge: ImageView = itemView.findViewById(R.id.imageViewLarge)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewPhone: TextView = itemView.findViewById(R.id.textViewPhone)
        val textViewAddress: TextView = itemView.findViewById(R.id.textViewAddress)
    }

    fun updateList(newList:List<UserFace>){
        usersFace.clear()
        usersFace.addAll(newList)
        notifyDataSetChanged()
    }

//    private fun cipherTextByteArrayFromBase64String(base64String: String):ByteArray{
//        return getDecoder().decode(base64String)
//    }
}