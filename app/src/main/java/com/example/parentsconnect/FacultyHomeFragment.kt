package com.example.parentsconnect

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FacultyHomeFragment : Fragment(), FragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_faculty_home, container, false)

//        val userId = requireArguments()!!.getInt("EXTRA_USER_ID", 0)
        val sharedPref = requireActivity().getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("userId", 0)

        // add new post button
        val addNewPostBtn = view.findViewById<ImageView>(R.id.addProgressBtn)
        addNewPostBtn.setOnClickListener {
            // load AddProgressFragment fragment
            val newFragment = AddProgressFragment()
            onFragmentSelected(newFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.facultyProgressRecycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val data = ArrayList<ProgressPostViewModel>()
        val db = DatabaseHelper(requireContext())
        val cursor = db.getProgressPostsFaculty(userId)
        while (cursor.moveToNext()) {
            val targetClass = cursor.getString(cursor.getColumnIndexOrThrow("CLASS"))
            val msg = cursor.getString(cursor.getColumnIndexOrThrow("MESSAGE"))
            data.add(ProgressPostViewModel(targetClass, msg))
        }
        // reverse data arraylist so that latest posts appear on top
        data.reverse()

        val adapter = ProgressPostAdapter(data)
        recyclerView.adapter = adapter

        return view
    }

    override fun onFragmentSelected(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.faculty_framelayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}