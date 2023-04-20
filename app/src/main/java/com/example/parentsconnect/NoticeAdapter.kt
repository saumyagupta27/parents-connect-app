package com.example.parentsconnect

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class NoticeAdapter(val context: Context, val noticeList: ArrayList<NoticeViewModel>, private val listener: OnDocumentClickListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val IS_IMPORTANT = 1
    val IS_NOT_IMPORTANT = 0

    // Inner interface for handling document clicks
    interface OnDocumentClickListener {
        fun onDocumentClicked(documentUri: Uri)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 0) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.not_important_notice, parent, false)
            return NotImportantViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.important_notice, parent, false)
            return ImportantViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentNotice = noticeList[position]

        if(holder.javaClass == NotImportantViewHolder::class.java) {
            val viewHolder = holder as NotImportantViewHolder
            viewHolder.title.text = currentNotice.noticeTitle
            viewHolder.message.text = currentNotice.noticeMessage
            if(currentNotice.documentUri == null) {
                viewHolder.viewFileBtn.visibility = View.GONE
            } else {
                viewHolder.viewFileBtn.visibility = View.VISIBLE
            }
            viewHolder.viewFileBtn.setOnClickListener {
                val selectedDocumentUri: Uri = currentNotice.documentUri!!
                listener.onDocumentClicked(selectedDocumentUri)
            }
        } else {
            val viewHolder = holder as ImportantViewHolder
            viewHolder.title.text = currentNotice.noticeTitle
            viewHolder.message.text = currentNotice.noticeMessage
            if(currentNotice.documentUri == null) {
                viewHolder.viewFileBtn.visibility = View.GONE
            } else {
                viewHolder.viewFileBtn.visibility = View.VISIBLE
            }
            viewHolder.viewFileBtn.setOnClickListener {
                val selectedDocumentUri: Uri = currentNotice.documentUri!!
                listener.onDocumentClicked(selectedDocumentUri)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentNotice = noticeList[position]
        if(currentNotice.isImportant) {
            return IS_IMPORTANT
        } else {
            return IS_NOT_IMPORTANT
        }
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    class NotImportantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.noticeTitleDisplay)
        val message = itemView.findViewById<TextView>(R.id.noticeTextDisplay)
        val viewFileBtn = itemView.findViewById<Button>(R.id.viewAttachedFileBtn)
    }

    class ImportantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.noticeTitleDisplayI)
        val message = itemView.findViewById<TextView>(R.id.noticeTextDisplayI)
        val viewFileBtn = itemView.findViewById<Button>(R.id.viewAttachedFileBtnI)
    }

    private fun checkPermission() : Boolean {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

}