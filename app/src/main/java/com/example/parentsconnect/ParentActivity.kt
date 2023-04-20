package com.example.parentsconnect

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class ParentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)

        val homeFragment = ParentHomeFragment()
        val chatFragment = ParentChatFragment()
        val notifFragment = ParentNotifFragment()
        val feeFragment = FeeFragment()
        val settingsFragment = ParentSettingsFragment()

        val id = intent.getIntExtra("userId", 0)
        val bundle = Bundle()
        bundle.putInt("EXTRA_USER_ID", id)
        homeFragment.arguments = bundle
        chatFragment.arguments = bundle
        notifFragment.arguments = bundle
        settingsFragment.arguments = bundle
        feeFragment.arguments = bundle

        setCurrentFragment(homeFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.parent_bottom_nav_bar)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item_home->setCurrentFragment(homeFragment)
                R.id.item_chat->setCurrentFragment(chatFragment)
                R.id.item_notification->setCurrentFragment(notifFragment)
                R.id.item_settings->setCurrentFragment(settingsFragment)
                R.id.item_fee->setCurrentFragment(feeFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.parent_framelayout, fragment)
            commit()
        }
}