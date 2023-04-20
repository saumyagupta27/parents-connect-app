package com.example.parentsconnect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProgressPostAdapter(private val postList: List<ProgressPostViewModel>):
    RecyclerView.Adapter<ProgressPostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressPostAdapter.ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.progress_post_view, parent, false)

        return ProgressPostAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProgressPostAdapter.ViewHolder, position: Int) {
        val ItemsViewModel = postList[position]

        holder.teacherNameTextView.text = ItemsViewModel.teacher_name
        holder.messageTextView.text = ItemsViewModel.message
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val teacherNameTextView: TextView = itemView.findViewById(R.id.teacherNameTextView)
        val messageTextView: TextView = itemView.findViewById(R.id.progressPostMsg)
    }

}