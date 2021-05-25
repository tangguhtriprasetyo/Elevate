package com.bangkit.elevate.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bangkit.elevate.R
import com.bangkit.elevate.databinding.ActivityMainBinding
import com.bangkit.elevate.ui.dashboard.funder.FunderProgressFragment
import com.bangkit.elevate.ui.dashboard.home.HomeFragment
import com.bangkit.elevate.ui.dashboard.ideator.IdeatorProgressFragment
import com.bangkit.elevate.ui.dashboard.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val funderProgressFragment = FunderProgressFragment()
        val ideatorProgressFragment = IdeatorProgressFragment()
        val profileFragment = ProfileFragment()

        setCurrentFragment(homeFragment)

        binding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> setCurrentFragment(homeFragment)
                R.id.navigation_history -> setCurrentFragment(ideatorProgressFragment)
                R.id.navigation_profile -> setCurrentFragment(profileFragment)
            }
            true
        }
    }


    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExit = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExit = false }, 2000)
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.host_fragment_activity_main, fragment)
        }
    }
}