package com.example.parentsconnect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class AddStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        val email = findViewById<TextInputEditText>(R.id.ed_add_email_s)
        val fname = findViewById<TextInputEditText>(R.id.ed_add_fname_s)
        val lname = findViewById<TextInputEditText>(R.id.ed_add_lname_s)
        val studentClass = findViewById<TextInputEditText>(R.id.ed_add_class_s)
        val addBtn = findViewById<Button>(R.id.admin_add_s_btn)
        val backBtn = findViewById<ImageView>(R.id.backBtnAddS)

        val db = DatabaseHelper(this)

        addBtn.setOnClickListener {
            val emailText = email.text.toString()
            val fnameText = fname.text.toString()
            val lnameText = lname.text.toString()
            val classText = studentClass.text.toString()
            if(emailText == "" || fnameText == "" || lnameText == "" || classText == "") {
                Toast.makeText(this, "No field should be empty", Toast.LENGTH_SHORT).show()
            } else {
                val res = db.addStudent(emailText, fnameText, lnameText, classText)
                if(res) {
                    Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show()
                    email.setText("")
                    fname.setText("")
                    lname.setText("")
                    studentClass.setText("")
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