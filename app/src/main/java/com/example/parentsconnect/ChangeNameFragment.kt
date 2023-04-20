package com.example.parentsconnect

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class ChangeNameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_name, container, false)

        val sharedPref = requireActivity().getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)
        val db = DatabaseHelper(requireContext())

        val userId = sharedPref.getInt("userId", 0)
        val name = db.getUserName(userId)
        val firstName = name.split(" ")[0]
        val lastName = name.split(" ")[1]


        val firstNameField = view.findViewById<EditText>(R.id.newFirstNameEdit)
        val lastNameField = view.findViewById<EditText>(R.id.newLastNameEdit)
        val submitBtn = view.findViewById<Button>(R.id.changeNameSubmit)
        val backBtn = view.findViewById<ImageView>(R.id.backBtnCN)

        firstNameField.setText(firstName)
        lastNameField.setText(lastName)

        submitBtn.setOnClickListener {
            val newFirstName = firstNameField.text.toString()
            val newLastName = lastNameField.text.toString()
            if(!(newFirstName == firstName && newLastName == lastName)) {
                db.updateUserName(userId, newFirstName, newLastName)
                Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
            }
        }

        backBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        return view
    }
}