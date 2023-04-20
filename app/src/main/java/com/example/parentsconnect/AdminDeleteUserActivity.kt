package com.example.parentsconnect

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class AdminDeleteUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_delete_user)

        val db = DatabaseHelper(this)
        val email = findViewById<TextInputEditText>(R.id.ed_delete_email)
        val deleteBtn = findViewById<Button>(R.id.admin_delete_btn)
        val backBtn = findViewById<ImageView>(R.id.backBtnDU)

        deleteBtn.setOnClickListener {
            val emailText = email.text.toString()
            if(db.checkEmail(emailText)) {
                if (db.deleteUser(emailText)) {
                    Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()
                }
                email.setText("")
            } else {
                Toast.makeText(this, "User with this email does not exist", Toast.LENGTH_SHORT).show()
            }
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}