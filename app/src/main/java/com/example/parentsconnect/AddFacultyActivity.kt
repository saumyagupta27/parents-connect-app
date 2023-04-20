package com.example.parentsconnect

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class AddFacultyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_faculty)

        val email = findViewById<TextInputEditText>(R.id.ed_add_email_f)
        val password = findViewById<TextInputEditText>(R.id.ed_add_password_f)
        val fname = findViewById<TextInputEditText>(R.id.ed_add_fname_f)
        val lname = findViewById<TextInputEditText>(R.id.ed_add_lname_f)
        val classTaught = findViewById<TextInputEditText>(R.id.ed_add_classes_f)
        val addBtn = findViewById<Button>(R.id.admin_add_f_btn)
        val backBtn = findViewById<ImageView>(R.id.backBtnAddT)

        val db = DatabaseHelper(this)

        addBtn.setOnClickListener {
            val emailText = email.text.toString()
            val pwdText = password.text.toString()
            val fnameText = fname.text.toString()
            val lnameText = lname.text.toString()
            val classText = classTaught.text.toString()
            if(emailText == ""  || pwdText == "" || fnameText == "" || lnameText == "" || classText == "") {
                Toast.makeText(this, "No field should be empty", Toast.LENGTH_SHORT).show()
            } else {
                val res = db.addFaculty(emailText, pwdText, fnameText, lnameText, classText)
                if(res) {
                    Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show()
                    email.setText("")
                    password.setText("")
                    fname.setText("")
                    lname.setText("")
                    classTaught.setText("")
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