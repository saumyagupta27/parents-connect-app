package com.example.parentsconnect

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminViewUsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_view_users)

        val facultyRecyclerView = findViewById<RecyclerView>(R.id.faculty_recycler_view)
        val parentRecyclerView = findViewById<RecyclerView>(R.id.parent_recycler_view)
        val backBtn = findViewById<ImageView>(R.id.backBtnViewU)

        facultyRecyclerView.layoutManager = LinearLayoutManager(this)
        parentRecyclerView.layoutManager = LinearLayoutManager(this)
        val facultyData = ArrayList<UserCardViewModel>()
        val parentData = ArrayList<UserCardViewModel>()

        val db = DatabaseHelper(this)

        val allParents: Cursor = db.getAllParents()
        while(allParents.moveToNext()) {
            val email = allParents.getString(allParents.getColumnIndexOrThrow("EMAIL"))
            val fname = allParents.getString(allParents.getColumnIndexOrThrow("FNAME"))
            val lname = allParents.getString(allParents.getColumnIndexOrThrow("LNAME"))
            val password = allParents.getString(allParents.getColumnIndexOrThrow("PASSWORD"))
            val userId = allParents.getInt(allParents.getColumnIndexOrThrow("ID"))
            val name = "$fname $lname"
            parentData.add(UserCardViewModel(name, email, password, userId))
        }

        val allTeachers: Cursor = db.getAllTeachers()
        while(allTeachers.moveToNext()) {
            val email = allTeachers.getString(allTeachers.getColumnIndexOrThrow("EMAIL"))
            val fname = allTeachers.getString(allTeachers.getColumnIndexOrThrow("FNAME"))
            val lname = allTeachers.getString(allTeachers.getColumnIndexOrThrow("LNAME"))
            val password = allTeachers.getString(allTeachers.getColumnIndexOrThrow("PASSWORD"))
            val userId = allTeachers.getInt(allTeachers.getColumnIndexOrThrow("ID"))
            val name = "$fname $lname"
            facultyData.add(UserCardViewModel(name, email, password, userId))
        }

        val fadapter = UserCardAdapter(facultyData)
        val padapter = UserCardAdapter(parentData)
        facultyRecyclerView.adapter = fadapter
        parentRecyclerView.adapter = padapter

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}