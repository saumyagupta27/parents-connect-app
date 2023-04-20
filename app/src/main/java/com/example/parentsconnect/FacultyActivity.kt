package com.example.parentsconnect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class FacultyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty)

        val homeFragment = FacultyHomeFragment()
        val chatFragment = FacultyChatFragment()
//        val notifFragment = FacultyNotifFragment()
        val notifFragment = ParentNotifFragment()
        val settingsFragment = FacultySettingsFragment()
//        val settingsFragment = ParentSettingsFragment()

        val id = intent.getIntExtra("userId", 0)
        val bundle = Bundle()
        bundle.putInt("EXTRA_USER_ID", id)
        homeFragment.arguments = bundle
        chatFragment.arguments = bundle
        notifFragment.arguments = bundle
        settingsFragment.arguments = bundle

        setCurrentFragment(homeFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.faculty_bottom_nav_bar)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item_home_f->setCurrentFragment(homeFragment)
                R.id.item_chat_f->setCurrentFragment(chatFragment)
                R.id.item_notification_f->setCurrentFragment(notifFragment)
                R.id.item_settings_f->setCurrentFragment(settingsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.faculty_framelayout, fragment)
            commit()
        }
}