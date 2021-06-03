package com.bangkit.elevate.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.bangkit.elevate.R
import com.bangkit.elevate.data.UserEntity
import com.bangkit.elevate.data.preference.UserPreference
import com.bangkit.elevate.databinding.ActivityMainBinding
import com.bangkit.elevate.ui.dashboard.funder.FunderProgressFragment
import com.bangkit.elevate.ui.dashboard.home.HomeFragment
import com.bangkit.elevate.ui.dashboard.ideator.IdeatorProgressFragment
import com.bangkit.elevate.ui.dashboard.profile.ProfileFragment
import com.bangkit.elevate.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    companion object {
        const val CHILD_FRAGMENT = "child_fragment"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var user: UserEntity
    private lateinit var mainViewModel: MainViewModel
    private var doubleBackToExit = false
    private var isIdeator = false
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBarMain.visibility = View.VISIBLE

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        mUserPreference = UserPreference(this)

        mainViewModel.setUserProfile(firebaseAuth.uid!!).observe(this, { userProfile ->
            if (userProfile != null) {
                user = userProfile
                val homeFragment = HomeFragment()
                val funderProgressFragment = FunderProgressFragment()
                val ideatorProgressFragment = IdeatorProgressFragment()
                val profileFragment = ProfileFragment()

                val mBundle = Bundle()
                mBundle.putParcelable("UserData", user)
                homeFragment.arguments = mBundle
                funderProgressFragment.arguments = mBundle
                ideatorProgressFragment.arguments = mBundle
                profileFragment.arguments = mBundle

                setCurrentFragment(homeFragment)
                binding.progressBarMain.visibility = View.GONE

                binding.bottomNav.setOnNavigationItemSelectedListener {
                    when (it.itemId) {
                        R.id.navigation_home -> setCurrentFragment(homeFragment)
                        R.id.navigation_progress -> {
                            isIdeator = mUserPreference.getRole()
                            if (isIdeator) {
                                setCurrentFragment(ideatorProgressFragment)
                            } else {
                                setCurrentFragment(funderProgressFragment)
                            }
                        }
                        R.id.navigation_profile -> setCurrentFragment(profileFragment)
                    }
                    true
                }
            }
        })
    }


    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
            return
        } else if (supportFragmentManager.findFragmentByTag(CHILD_FRAGMENT) != null) {
            super.onBackPressed()
            binding.bottomNav.visibility = View.VISIBLE
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
        binding.bottomNav.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(this)
    }

    override fun onAuthStateChanged(@NonNull firebaseAuth: FirebaseAuth) {
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
        if (firebaseUser == null) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}