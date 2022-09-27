package com.kcdeepak.faceapp

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(val context: Context) :RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private val usersFace = ArrayList<UserFace>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.face_user_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context)
            .load(usersFace[position].imageUri)
            .into(holder.imageViewSmall)
        Glide.with(context)
            .load(usersFace[position].imageUri)
            .into(holder.imageViewLarge)
        holder.textViewName.text = usersFace[position].name
        holder.textViewPhone.text = usersFace[position].phone
        holder.textViewAddress.text = usersFace[position].address
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
}