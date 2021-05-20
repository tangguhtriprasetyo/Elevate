package com.bangkit.elevate.ui.onboardingscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.elevate.data.OnBoardingEntity
import com.bangkit.elevate.databinding.ItemsOnboardingBinding

class OnBoardingViewPagerAdapter(private val onBoardingData: List<OnBoardingEntity>) :
    RecyclerView.Adapter<OnBoardingViewPagerAdapter.OnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        return OnboardingViewHolder(
            ItemsOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return onBoardingData.size
    }

    override fun onBindViewHolder(
        holder: OnBoardingViewPagerAdapter.OnboardingViewHolder,
        position: Int
    ) {
        holder.bind(onBoardingData[position])
    }

    inner class OnboardingViewHolder(private val binding: ItemsOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(onBoardingData: OnBoardingEntity) {
            with(binding) {
                tvTitleOnboarding.text = onBoardingData.title
                tvCaptionOnboarding.text = onBoardingData.caption
                imgHeaderOnboarding.setImageResource(onBoardingData.imgHeader)
            }
        }
    }
}