package com.example.bunchtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(var userList: ArrayList<DataList>): RecyclerView.Adapter<Adapter.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val item_name: TextView = itemView.findViewById(R.id.item_one)
        val item_email: TextView = itemView.findViewById(R.id.item_two)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent, false )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.item_name.text = currentItem.name
        holder.item_email.text = currentItem.contact
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}