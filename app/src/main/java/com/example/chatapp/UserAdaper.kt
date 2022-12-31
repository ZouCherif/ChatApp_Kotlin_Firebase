package com.example.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdaper(val context: Context, val userList: ArrayList<User>): RecyclerView.Adapter<UserAdaper.UserViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewholder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.card, parent, false)
        return UserViewholder(view)
    }

    override fun onBindViewHolder(holder: UserViewholder, position: Int) {
        val currentUser = userList[position]
        holder.username.text = currentUser.username
        holder.id.text = currentUser.uid
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewholder(itemView: View): RecyclerView.ViewHolder(itemView){
        val username = itemView.findViewById<TextView>(R.id.cardUserName)
        val id = itemView.findViewById<TextView>(R.id.cardId)

    }
}
