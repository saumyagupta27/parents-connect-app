package com.example.parentsconnect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class AddParentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_parent)

        val email = findViewById<TextInputEditText>(R.id.ed_add_email_p)
        val password = findViewById<TextInputEditText>(R.id.ed_add_password_p)
        val fname = findViewById<TextInputEditText>(R.id.ed_add_fname_p)
        val lname = findViewById<TextInputEditText>(R.id.ed_add_lname_p)
        val studentEmail = findViewById<TextInputEditText>(R.id.ed_add_student)
        val addBtn = findViewById<Button>(R.id.admin_add_p_btn)
        val backBtn = findViewById<ImageView>(R.id.backBtnAddP)

        val db = DatabaseHelper(this)

        addBtn.setOnClickListener {
            val emailText = email.text.toString()
            val pwdText = password.text.toString()
            val fnameText = fname.text.toString()
            val lnameText = lname.text.toString()
            val studentText = studentEmail.text.toString()
            if(emailText == ""  || pwdText == "" || fnameText == "" || lnameText == "" || studentText == "") {
                Toast.makeText(this, "No field should be empty", Toast.LENGTH_SHORT).show()
            } else {
                val res = db.addParent(emailText, pwdText, fnameText, lnameText, studentText)
//                Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
                if(res) {
                    Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show()
                    email.setText("")
                    password.setText("")
                    fname.setText("")
                    lname.setText("")
                    studentEmail.setText("")
                } else {
                    Toast.makeText(this, "Not added", Toast.LENGTH_SHORT).show()
                }
            }
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}