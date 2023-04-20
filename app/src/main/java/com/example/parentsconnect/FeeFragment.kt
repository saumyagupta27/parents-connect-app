package com.example.parentsconnect

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FeeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fee, container, false)

        val db = DatabaseHelper(requireContext())

        val sharedPref = requireActivity().getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("userId", 0)

        val nameTextView = view.findViewById<TextView>(R.id.studentNameTextView2)
        val classTextView = view.findViewById<TextView>(R.id.studentClassTextView2)
        val monthsTextView = view.findViewById<TextView>(R.id.monthsPaidTextView)

        val studentName = db.getStudentName(userId)
        val studentClass = db.getStudentClass(userId)
        nameTextView.text = "Student name: $studentName"
        classTextView.text = "Student class: $studentClass"

        val monthsTrue = db.getMonthsTrue(userId)
        if(monthsTrue.size == 0) {
            monthsTextView.text = "NONE"
        } else {
            val monthsString = monthsTrue.joinToString(separator = ",  ", transform = { it.toLowerCase().capitalize() })
            monthsTextView.text = "$monthsString"
        }
//      Today students completed worksheets 1 and 2 for Chapter 3.
//Worksheet 3 has been given as homework.

        val payFeeTextViewBtn = view.findViewById<TextView>(R.id.payFeeBtn)
        payFeeTextViewBtn.setOnClickListener {
            val payFeeFragment = PayFeeFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.parent_framelayout, payFeeFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return view
    }

}