package com.bangkit.elevate.ui.onboardingscreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.elevate.R
import com.bangkit.elevate.data.OnBoardingEntity
import com.bangkit.elevate.databinding.ActivityOnBoardingScreenBinding
import com.bangkit.elevate.ui.dashboard.MainActivity
import com.bangkit.elevate.ui.login.LoginActivity

class OnBoardingScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingScreenBinding

    private val onBoardingViewPagerAdapter = OnBoardingViewPagerAdapter(
        listOf(
            OnBoardingEntity(
                R.drawable.ic_onboarding1,
                "Ideator",
                "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint."
            ),
            OnBoardingEntity(
                R.drawable.ic_onboarding2,
                "Funder",
                "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint."
            ),
            OnBoardingEntity(
                R.drawable.ic_onboarding3,
                "Crowdfunding",
                "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint."
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPagerOnboarding.adapter = onBoardingViewPagerAdapter
        binding.imgIndicatorOnboarding.setViewPager(binding.viewPagerOnboarding)

        binding.btnGetStarted.setOnClickListener {
            val intent = Intent(this@OnBoardingScreenActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.viewPagerOnboarding.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == onBoardingViewPagerAdapter.itemCount - 1) {
                        binding.imgIndicatorOnboarding.visibility = View.GONE
                        binding.btnGetStarted.visibility = View.VISIBLE
                    } else {
                        binding.imgIndicatorOnboarding.visibility = View.VISIBLE
                        binding.btnGetStarted.visibility = View.GONE
                    }
                }
            }
        )
    }
}