package com.bangkit.elevate

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bangkit.elevate.data.IdeaEntity
import com.bangkit.elevate.data.UserEntity
import com.bangkit.elevate.databinding.FragmentIdeatorProgressBinding
import com.bangkit.elevate.databinding.LayoutDetailIdeaBinding
import com.bangkit.elevate.ui.bottomsheet.BottomSheetDonateFragment
import com.bangkit.elevate.ui.bottomsheet.BottomSheetUploadFragment
import com.bangkit.elevate.ui.dashboard.MainActivity
import com.bangkit.elevate.ui.dashboard.MainViewModel
import com.bangkit.elevate.ui.dashboard.ideator.AddIdeaFragment
import com.bangkit.elevate.ui.dashboard.ideator.IdeatorViewModel
import com.bangkit.elevate.utils.loadImage
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

class DetailIdeaFragment: Fragment() {
    private lateinit var binding: LayoutDetailIdeaBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val detailIdeaViewModel: DetailIdeaViewModel by activityViewModels()
    private lateinit var userDataProfile: UserEntity
    private lateinit var ideaData: IdeaEntity
    private lateinit var uriPath: Uri
    private lateinit var filesName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LayoutDetailIdeaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addIdeaFragment = AddIdeaFragment()
        val bottomSheetDonate = BottomSheetDonateFragment()
        val bottomSheetUpload = BottomSheetUploadFragment()

        userDataProfile = UserEntity()
        ideaData = IdeaEntity()

        if (arguments != null) {
            userDataProfile = requireArguments().getParcelable("UserData")!!
        }

        val getPdf = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uriPdf ->

            if (uriPdf.path != null) {
                uriPath = uriPdf
                filesName = uriPdf.path.toString()
                val cut = filesName.lastIndexOf('/')
                if (cut != -1) {
                    filesName = filesName.substring(cut + 1)
                }

                val mBundle = Bundle()
                mBundle.putString(BottomSheetUploadFragment.EXTRA_FILE, uriPath.toString())
                mBundle.putString(BottomSheetUploadFragment.EXTRA_FILE_NAME, filesName)
                mBundle.putString(BottomSheetUploadFragment.EXTRA_UID, userDataProfile.uid)
                mBundle.putString(BottomSheetUploadFragment.EXTRA_NAME, userDataProfile.username)
                bottomSheetUpload.arguments = mBundle
                bottomSheetUpload.show(
                    requireActivity().supportFragmentManager,
                    BottomSheetUploadFragment::class.java.simpleName
                )

            }
        }
        //TODO
//        binding.layoutIdeatorProgress.icProposal.setOnClickListener {
//            bottomSheetDonate.show(
//                requireActivity().supportFragmentManager,
//                BottomSheetDonateFragment::class.java.simpleName
//            )
//        }
//
//        binding.layoutIdeatorProgress.btnAddTerm1.setOnClickListener {
//            getPdf.launch(arrayOf("application/pdf"))
//        }
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