package com.example.parentsconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.ln

class AdminAddUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_user)

        val facultyBtn = findViewById<Button>(R.id.add_faculty_btn)
        val parentBtn = findViewById<Button>(R.id.add_parent_btn)
        val studentBtn = findViewById<Button>(R.id.add_student_btn)
        val backBtn = findViewById<ImageView>(R.id.backBtnAddU)

        facultyBtn.setOnClickListener {
            val intent = Intent(this, AddFacultyActivity::class.java)
            startActivity(intent)
        }

        parentBtn.setOnClickListener {
            val intent = Intent(this, AddParentActivity::class.java)
            startActivity(intent)
        }

        studentBtn.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }

    }
}