package com.example.parentsconnect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val addButton = findViewById<Button>(R.id.add_user_btn)
        val deleteButton = findViewById<Button>(R.id.remove_user_btn)
        val viewButton = findViewById<Button>(R.id.view_users_btn)
        val postNoticeButton = findViewById<Button>(R.id.postNoticeBtn)
        val feeBtn = findViewById<Button>(R.id.adminAddFeeDetailsBtn)
        val logoutButton = findViewById<Button>(R.id.adminLogoutBtn)

        addButton.setOnClickListener{
            val intent = Intent(this, AdminAddUserActivity::class.java)
            startActivity(intent)
        }
        deleteButton.setOnClickListener{
            val intent = Intent(this, AdminDeleteUserActivity::class.java)
            startActivity(intent)
        }
        viewButton.setOnClickListener {
            val intent = Intent(this, AdminViewUsersActivity::class.java)
            startActivity((intent))
        }
        logoutButton.setOnClickListener {
            val sharedPref = getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.remove("userId")
            editor.remove("userType")
            editor.apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        postNoticeButton.setOnClickListener {
            val intent = Intent(this, PostNoticeActivity::class.java)
            startActivity(intent)
        }
        feeBtn.setOnClickListener {
            val intent = Intent(this, AddFeeDetailsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.item_logout) {
            val sharedPref = getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.remove("userId")
            editor.remove("userType")
            editor.apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            return true
        }
        return true
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}