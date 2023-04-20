package com.example.parentsconnect

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView


class UserChatAdapter(val context: Context, val userList: ArrayList<User>, val fragmentCallback: FragmentCallback): RecyclerView.Adapter<UserChatAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_chat_layout, parent, false)

        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        val sharedPref = context.getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("userId", 0)

        // setting profile picture to ImageView
        val imageByteArray = currentUser.image
        if(imageByteArray == null) {
            val drawable = context.resources.getDrawable(R.drawable.ic_baseline_account_circle_24, null)
            holder.imageView.setImageDrawable(drawable)
        } else {
            val imageBitMap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            holder.imageView.setImageBitmap(imageBitMap)
        }

        holder.name.text = currentUser.name

        if(currentUser.isUnread!!) {
            holder.notifImageView.visibility = View.VISIBLE
        } else {
            holder.notifImageView.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
//            val intent = Intent(context, ChatFragment)
            val fragment = ChatFragment()
            val bundle = Bundle()
            bundle.putInt("EXTRA_USER_SELECTED_ID", currentUser.id!!)
            fragment.arguments = bundle
            fragmentCallback.onFragmentSelected(fragment)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.chat_name)
        val imageView = itemView.findViewById<ImageView>(R.id.chatProfilePic)
        val notifImageView = itemView.findViewById<ImageView>(R.id.chatNotifImageView)
    }

}