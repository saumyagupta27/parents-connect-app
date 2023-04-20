package com.example.parentsconnect

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView


class FacultySettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_faculty_settings, container, false)
        val sharedPref = requireActivity().getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)
        val db = DatabaseHelper(requireContext())
        val userId = sharedPref.getInt("userId", 0)


        val nameTextView = view.findViewById<TextView>(R.id.profileNameTextF)
        val emailTextView = view.findViewById<TextView>(R.id.profileEmailTextF)
        val imageView = view.findViewById<ImageView>(R.id.profilePictureF)

        val changeNameBtn = view.findViewById<Button>(R.id.changeNameBtnF)
        val changePasswordBtn = view.findViewById<Button>(R.id.changePasswordBtnF)
        val changePicBtn = view.findViewById<Button>(R.id.changePictureBtnF)

        // setting name to the TextView fields
        nameTextView.text = db.getUserName(userId)
        emailTextView.text = db.getEmail(userId)

        // setting profile picture to ImageView
        val imageByteArray = db.getImage(userId)
        if(imageByteArray == null) {
            val drawable = resources.getDrawable(R.drawable.ic_baseline_account_circle_24, null)
            imageView.setImageDrawable(drawable)
        } else {
            val imageBitMap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            imageView.setImageBitmap(imageBitMap)
        }

        changeNameBtn.setOnClickListener {
            val changeNameFragment = ChangeNameFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.faculty_framelayout, changeNameFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        changePasswordBtn.setOnClickListener {
            val changePasswordFragment = ChangePasswordFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.faculty_framelayout, changePasswordFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        changePicBtn.setOnClickListener {
            val changePictureFragment = ChangePictureFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.faculty_framelayout, changePictureFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val logoutBtn = view.findViewById<Button>(R.id.facultyLogoutBtn)
        logoutBtn.setOnClickListener {
            val editor = sharedPref.edit()
            editor.remove("userId")
            editor.remove("userType")
            editor.apply()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }

        return view
    }

}