package com.example.parentsconnect

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.database.getStringOrNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ParentNotifFragment : Fragment(), NoticeAdapter.OnDocumentClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_parent_notif, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.notificationRecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val data = ArrayList<NoticeViewModel>()
        val db = DatabaseHelper(requireContext())
        val cursor = db.getAllNotices()
        while(cursor.moveToNext()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"))
            val message = cursor.getString(cursor.getColumnIndexOrThrow("MESSAGE"))
            val isImportant = cursor.getInt(cursor.getColumnIndexOrThrow("IS_IMPORTANT")) != 0
            val documentUri = cursor.getStringOrNull(cursor.getColumnIndexOrThrow("DOCUMENT_URI"))
            if(documentUri == null) {
                data.add(NoticeViewModel(title, message, isImportant, null))
            } else {
                data.add(NoticeViewModel(title, message, isImportant, Uri.parse(documentUri)))
            }
        }
        data.reverse()

        val adapter = NoticeAdapter(requireContext(), data, this)
        recyclerView.adapter = adapter

        return view
    }

    override fun onDocumentClicked(documentUri: Uri) {
        val viewDocumentIntent = Intent(Intent.ACTION_VIEW)
        viewDocumentIntent.setDataAndType(documentUri, "application/pdf")
        viewDocumentIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(viewDocumentIntent)
    }

}