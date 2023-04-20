package com.example.parentsconnect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserCardAdapter(private val userList: List<UserCardViewModel>) : RecyclerView.Adapter<UserCardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_card_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = userList[position]

        // sets the text to the textview from our itemHolder class
        holder.nameTextView.text = ItemsViewModel.name
        holder.emailTextView.text = ItemsViewModel.email
        holder.passwordTextView.text = ItemsViewModel.password
        holder.idTextView.text = "User id: ${ItemsViewModel.userId.toString()}"

    }
    override fun getItemCount(): Int {
        return userList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        val passwordTextView: TextView = itemView.findViewById(R.id.passwordTextView)
        val idTextView: TextView = itemView.findViewById(R.id.userIdTextView)
    }
}