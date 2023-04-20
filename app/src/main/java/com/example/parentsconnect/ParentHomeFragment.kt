package com.example.parentsconnect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ParentHomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_parent_home, container, false)



        val userId = requireArguments()!!.getInt("EXTRA_USER_ID", 0)

        val recyclerView = view.findViewById<RecyclerView>(R.id.posts_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val data = ArrayList<ProgressPostViewModel>()

        val db = DatabaseHelper(requireContext())
        val cursor = db.getProgressPosts(userId)
        while (cursor.moveToNext()) {
            val teacherID = cursor.getInt(cursor.getColumnIndexOrThrow("TEACHER_ID"))
            val teacherName = db.getUserName(teacherID)
            val msg = cursor.getString(cursor.getColumnIndexOrThrow("MESSAGE"))
            data.add(ProgressPostViewModel(teacherName, msg))
        }
        data.reverse()

        val adapter = ProgressPostAdapter(data)
        recyclerView.adapter = adapter

        return view
    }

}