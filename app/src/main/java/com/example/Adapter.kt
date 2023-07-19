package com.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.model.UserModel
import com.example.monkhood.R
import com.squareup.picasso.Picasso

class Adapter(private val userList: ArrayList<UserModel>):RecyclerView.Adapter<Adapter.MyViewHolder>() {

    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        val userName:TextView= itemView.findViewById(R.id.userName)
        val userImage:ImageView=itemView.findViewById(R.id.userImage)
        val userNumber:TextView=itemView.findViewById(R.id.userNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = userList[position]
        holder.userName.text= currentItem.name
        holder.userNumber.text=currentItem.number.toString()
        Picasso.get().load(currentItem.email).into(holder.userImage)
    }
}