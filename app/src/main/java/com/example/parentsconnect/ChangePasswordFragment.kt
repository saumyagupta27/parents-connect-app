package com.example.parentsconnect

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class ChangePasswordFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_password, container, false)

        val sharedPref = requireActivity().getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)
        val db = DatabaseHelper(requireContext())

        val userId = sharedPref.getInt("userId", 0)

        val oldPasswordField = view.findViewById<EditText>(R.id.oldPasswordEdit)
        val newPasswordField = view.findViewById<EditText>(R.id.newPasswordEdit)
        val reNewPasswordField = view.findViewById<EditText>(R.id.reNewPasswordEdit)
        val submitBtn = view.findViewById<Button>(R.id.changePasswordSubmit)
        val backBtn = view.findViewById<ImageView>(R.id.backBtnCP)

        val storedPassword = db.getPassword(userId)

        submitBtn.setOnClickListener {
            if(oldPasswordField.text.toString() == storedPassword) {
                if(newPasswordField.text.toString() == reNewPasswordField.text.toString()) {
//                    Toast.makeText(requireContext(), newPasswordField.text.toString(), Toast.LENGTH_SHORT).show()
//                    Log.d("newpassword: ", newPasswordField.text.toString())
                    db.updatePassword(userId, newPasswordField.text.toString())
                    oldPasswordField.setText("")
                    newPasswordField.setText("")
                    reNewPasswordField.setText("")
                } else {
                    Toast.makeText(requireContext(), "Password does not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Invalid old password", Toast.LENGTH_SHORT).show()
            }
        }

        backBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        return view
    }

}