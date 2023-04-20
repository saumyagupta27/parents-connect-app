package com.example.parentsconnect

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val sharedPref = getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)
        if(sharedPref.contains("userId")) {
            val storedUserType = sharedPref.getString("userType", "null")
            if(storedUserType == "admin") {
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            } else {
                val storedUserId = sharedPref.getInt("userId", 0)
                if(storedUserType == "teacher") {
                    val intent = Intent(this, FacultyActivity::class.java)
                    intent.putExtra("userId", storedUserId)
                    startActivity(intent)
                }
                else if(storedUserType == "parent") {
                    val intent = Intent(this, ParentActivity::class.java)
                    intent.putExtra("userId", storedUserId)
                    startActivity(intent)
                }
            }
        }

        val db = DatabaseHelper(this)

        val loginButton = findViewById<Button>(R.id.login_btn)
        val emailEditText = findViewById<TextInputEditText>(R.id.ed_email)
        val passwordEditText = findViewById<TextInputEditText>(R.id.ed_password)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if(email == "" && password == "") {
                Toast.makeText(this, "Enter email id and password", Toast.LENGTH_SHORT).show()
            }
            else if(password == "") {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
            }
            else if(email == "") {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
            }
            else {
                val isUserValid = db.checkUser(email, password)
                if(isUserValid) {
                    val userType = db.getUserType(email, password)
                    val sharedPref = getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)

                    when (userType) {
                        "admin" -> {
                            val editor = sharedPref.edit()
                            editor.putInt("userId", 0)
                            editor.putString("userType", "admin")
                            editor.apply()
                            val intent = Intent(this, AdminActivity::class.java)
                            startActivity(intent)
                        }
                        "teacher" -> {
                            val userId = db.getUserId(email)
                            val editor = sharedPref.edit()
                            editor.putInt("userId", userId)
                            editor.putString("userType", "teacher")
                            editor.apply()
                            val intent = Intent(this, FacultyActivity::class.java)
                            intent.putExtra("userId", userId)
                            startActivity(intent)
                        }
                        "parent" -> {
                            val userId = db.getUserId(email)
                            val editor = sharedPref.edit()
                            editor.putInt("userId", userId)
                            editor.putString("userType", "parent")
                            editor.apply()
                            val intent = Intent(this, ParentActivity::class.java)
                            intent.putExtra("userId", userId)
                            startActivity(intent)
                        }
                    }
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}