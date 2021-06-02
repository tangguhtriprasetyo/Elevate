package com.bangkit.elevate.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bangkit.elevate.R
import com.bangkit.elevate.data.IdeaEntity
import com.bangkit.elevate.data.UserEntity
import com.bangkit.elevate.databinding.FragmentDetailIdeaBinding
import com.bangkit.elevate.ui.bottomsheet.BottomSheetDonateFragment
import com.bangkit.elevate.ui.dashboard.MainViewModel
import com.bangkit.elevate.utils.loadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

@ExperimentalCoroutinesApi
class DetailIdeaFragment : Fragment() {

    companion object {
        const val EXTRA_DATA_IDEA = "extra_data_idea"
        const val EXTRA_DATA_USER = "extra_data_user"
    }

    private lateinit var binding: FragmentDetailIdeaBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val detailIdeaViewModel: DetailIdeaViewModel by activityViewModels()
    private lateinit var userDataProfile: UserEntity
    private lateinit var ideaData: IdeaEntity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailIdeaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetDonate = BottomSheetDonateFragment()

        userDataProfile = UserEntity()
        ideaData = IdeaEntity()

        if (arguments != null) {
            ideaData = requireArguments().getParcelable(EXTRA_DATA_IDEA)!!
            userDataProfile = requireArguments().getParcelable(EXTRA_DATA_USER)!!
            Log.d("User Profile DetailIDea", userDataProfile.balance.toString())
            setIdeaData()
        }

        detailIdeaViewModel.successfulDonate.observe(viewLifecycleOwner, { isDonated ->
            if (isDonated) {
                Log.d("SuccessDonate", "Success")

                mainViewModel.setUserProfile(userDataProfile.uid.toString())
                    .observe(viewLifecycleOwner, { userProfile ->
                        if (userProfile != null) {
                            userDataProfile = userProfile
                        }
                        Log.d("ViewModelProfile: ", userProfile.toString())
                    })

                detailIdeaViewModel.getIdeaData(ideaData.ideatorUid.toString())
                    .observe(viewLifecycleOwner, { task ->
                        if (task != null) {
                            ideaData = task
                            setIdeaData()
                        }
                    })
                detailIdeaViewModel.getStatusDonation(false)
            }
        })

        binding.DonateButton.setOnClickListener {
            val mBundle = Bundle()
            mBundle.putString(BottomSheetDonateFragment.EXTRA_IDEA_ID, ideaData.ideatorUid)
            mBundle.putString(BottomSheetDonateFragment.EXTRA_USER_ID, userDataProfile.uid)
            mBundle.putLong(BottomSheetDonateFragment.EXTRA_BALANCED_USER, userDataProfile.balance)
            bottomSheetDonate.arguments = mBundle
            Log.d("Balanced DetailIDea", userDataProfile.balance.toString())
            bottomSheetDonate.show(
                requireActivity().supportFragmentManager,
                BottomSheetDonateFragment::class.java.simpleName
            )
        }

        binding.detailFileProposal.setOnClickListener {
            openPdftoBrowser()
        }

    }

    private fun openPdftoBrowser() {
        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(ideaData.proposalFile))
        startActivity(intentBrowser)
    }

    private fun setIdeaData() {

        val localeId = Locale("in", "ID")
        val priceFormat = NumberFormat.getCurrencyInstance(localeId)
        val currentFund = ideaData.currentFund.toDouble()
        val requiredCost = ideaData.requiredCost.toDouble()
        val progressPercentage: Double = (currentFund / requiredCost * 100)
        Log.d("Percentage", progressPercentage.toString())
        Log.d("Percentage", currentFund.toString())
        Log.d("Percentage", requiredCost.toString())

        with(binding) {
            detailName.text = ideaData.brandName
            detailBrandType.text = ideaData.businessIdea
            detailLocation.text = ideaData.location
            detailDescription.text = ideaData.description
            imgBrand.loadImage(ideaData.logoFile)

            detailCurrentFund.text = priceFormat.format(ideaData.currentFund)
            detailTotalFund.text = priceFormat.format(ideaData.requiredCost)
            detailProgressBar.progress = progressPercentage.roundToInt()
            val currentProgressPercentage = "${progressPercentage.roundToInt()} %"
            detailCurrentProgress.text = currentProgressPercentage

            if (ideaData.term1Unlocked) {
                term1Locked.constraintProgressLock.visibility = View.GONE
                Term1ProgressBar.visibility = View.VISIBLE
                Term1Description.visibility = View.VISIBLE
                Term1FileType.visibility = View.VISIBLE
                Term1Progress.visibility = View.VISIBLE
                Term1FinalProgress.visibility = View.VISIBLE
                Term1Proposal.visibility = View.VISIBLE

            } else {
                term1Locked.constraintProgressLock.visibility = View.VISIBLE
                Term1ProgressBar.visibility = View.GONE
                Term1Description.visibility = View.GONE
                Term1FileType.visibility = View.GONE
                Term1Progress.visibility = View.GONE
                Term1FinalProgress.visibility = View.GONE
                Term1Proposal.visibility = View.GONE
            }

            if (ideaData.term2Unlocked) {
                term2Locked.constraintProgressLock.visibility = View.GONE
                Term2ProgressBar.visibility = View.VISIBLE
                Term2Description.visibility = View.VISIBLE
                Term2FileType.visibility = View.VISIBLE
                Term2Progress.visibility = View.VISIBLE
                Term2FinalProgress.visibility = View.VISIBLE
                Term2Proposal.visibility = View.VISIBLE
            } else {
                term2Locked.constraintProgressLock.visibility = View.VISIBLE
                Term2ProgressBar.visibility = View.GONE
                Term2Description.visibility = View.GONE
                Term2FileType.visibility = View.GONE
                Term2Progress.visibility = View.GONE
                Term2FinalProgress.visibility = View.GONE
                Term2Proposal.visibility = View.GONE
            }

            if (ideaData.term3Unlocked) {
                term3Locked.constraintProgressLock.visibility = View.GONE
                Term3ProgressBar.visibility = View.VISIBLE
                Term3Description.visibility = View.VISIBLE
                Term3FileType.visibility = View.VISIBLE
                Term3Progress.visibility = View.VISIBLE
                Term3FinalProgress.visibility = View.VISIBLE
                Term3Proposal.visibility = View.VISIBLE
            } else {
                term3Locked.constraintProgressLock.visibility = View.VISIBLE
                Term3ProgressBar.visibility = View.GONE
                Term3Description.visibility = View.GONE
                Term3FileType.visibility = View.GONE
                Term3Progress.visibility = View.GONE
                Term3FinalProgress.visibility = View.GONE
                Term3Proposal.visibility = View.GONE
            }

            when (ideaData.status) {
                getString(R.string.unfulfilled) -> detailStatusFund.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                getString(R.string.process_term1) -> detailStatusFund.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.Cyan_A_400_Dark
                    )
                )
                getString(R.string.process_term2) -> detailStatusFund.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.teal_200
                    )
                )
                getString(R.string.completed) -> detailStatusFund.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
            }
            detailStatusFund.text = ideaData.status
        }
    }
}