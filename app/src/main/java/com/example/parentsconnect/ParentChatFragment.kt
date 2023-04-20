package com.example.parentsconnect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getBlobOrNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parentsconnect.FragmentCallback


class ParentChatFragment : Fragment(), FragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_parent_chat, container, false)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Parents Connect"


        val userId = requireArguments().getInt("EXTRA_USER_ID", 0)
        val db = DatabaseHelper(requireContext())

        val chatRecyclerView = view.findViewById<RecyclerView>(R.id.parentChatRecyclerview)
        val userList = ArrayList<User>()
        val adapter = UserChatAdapter(requireContext(), userList, this)

        val cursor = db.getTeacherList(userId)
        val list = db.getUnreadTeachers(userId)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"))
            val fname = cursor.getString(cursor.getColumnIndexOrThrow("FNAME"))
            val lname = cursor.getString(cursor.getColumnIndexOrThrow("LNAME"))
            val image = cursor.getBlobOrNull(cursor.getColumnIndexOrThrow("IMAGE"))
            val name = "$fname $lname"
            val isUnread = list.contains(id)
            userList.add(User(id, email, name, image, isUnread))
        }

        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatRecyclerView.adapter = adapter

        return view
    }

    override fun onFragmentSelected(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.parent_framelayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}