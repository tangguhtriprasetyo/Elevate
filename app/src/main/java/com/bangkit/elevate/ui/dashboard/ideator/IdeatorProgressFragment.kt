package com.bangkit.elevate.ui.dashboard.ideator

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
import com.bangkit.elevate.R
import com.bangkit.elevate.data.IdeaEntity
import com.bangkit.elevate.data.UserEntity
import com.bangkit.elevate.databinding.FragmentIdeatorProgressBinding
import com.bangkit.elevate.ui.bottomsheet.BottomSheetUploadFragment
import com.bangkit.elevate.ui.dashboard.MainActivity
import com.bangkit.elevate.ui.dashboard.MainViewModel
import com.bangkit.elevate.utils.loadImage
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt


class IdeatorProgressFragment : Fragment() {

    private lateinit var binding: FragmentIdeatorProgressBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val ideatorViewModel: IdeatorViewModel by activityViewModels()
    private lateinit var userDataProfile: UserEntity
    private lateinit var ideaData: IdeaEntity
    private lateinit var uriPath: Uri
    private lateinit var filesName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIdeatorProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addIdeaFragment = AddIdeaFragment()
        val bottomSheetUpload = BottomSheetUploadFragment()

        userDataProfile = UserEntity()
        ideaData = IdeaEntity()

        if (arguments != null) {
            userDataProfile = requireArguments().getParcelable("UserData")!!
        }

        ideatorViewModel.alreadyUploadTerm1.observe(viewLifecycleOwner, { isUploaded ->
            if (isUploaded) {
                binding.layoutIdeatorProgress.btnAddTerm1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_mdi_file_download
                    )
                )
                binding.layoutIdeatorProgress.titlePdfTerm1.visibility = View.VISIBLE
                binding.layoutIdeatorProgress.titlePdfTerm1.text = filesName
            }
        })

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

        mainViewModel.setUserProfile(userDataProfile.uid.toString())
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                    if (userProfile.activeIdea == true) {
                        binding.layoutIdeatorProgress.viewIdeatorProgress.visibility = View.VISIBLE
                        binding.layoutEmptyIdea.constraintEmptyIdea.visibility = View.GONE
                        getIdeaData()
                    } else {
                        binding.layoutIdeatorProgress.viewIdeatorProgress.visibility = View.GONE
                        binding.layoutEmptyIdea.constraintEmptyIdea.visibility = View.VISIBLE
                    }
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })

        binding.layoutEmptyIdea.btnAddIdea.setOnClickListener {

            val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav)
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(
                    R.id.host_fragment_activity_main,
                    addIdeaFragment,
                    MainActivity.CHILD_FRAGMENT
                )
                addToBackStack(null)
                commit()
            }
            bottomNav.visibility = View.GONE
        }

        binding.layoutIdeatorProgress.icProposal.setOnClickListener {
            // TODO view PDF
        }

        binding.layoutIdeatorProgress.btnAddTerm1.setOnClickListener {
            getPdf.launch(arrayOf("application/pdf"))
        }
    }

    private fun getIdeaData() {
        ideatorViewModel.getIdeaData(userDataProfile.uid.toString())
            .observe(viewLifecycleOwner, { ideaDataResult ->
                if (ideaDataResult != null) {
                    ideaData = ideaDataResult
                    setIdeaData()
                }
            })
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

        with(binding.layoutIdeatorProgress) {
            tvBrandName.text = ideaData.brandName
            tvBrandType.text = ideaData.businessIdea
            tvLocation.text = ideaData.location
            detailDescription.text = ideaData.description
            imgBrand.loadImage(ideaData.logoFile)

            tvCurrentFund.text = priceFormat.format(ideaData.currentFund)
            tvTotalFund.text = priceFormat.format(ideaData.requiredCost)
            progressFunding.progress = progressPercentage.roundToInt()
            val currentProgressPercentage = "${progressPercentage.roundToInt()} %"
            detailCurrentProgress.text = currentProgressPercentage

            if (ideaData.term1Unlocked) {
                ideatorTerm1ProgressLock.constraintProgressLock.visibility = View.GONE
                captionIdeatorTerm1.visibility = View.VISIBLE
                btnAddTerm1.visibility = View.VISIBLE
                titlePdfTerm1.visibility = View.VISIBLE

            } else {
                ideatorTerm1ProgressLock.constraintProgressLock.visibility = View.VISIBLE
                captionIdeatorTerm1.visibility = View.GONE
                btnAddTerm1.visibility = View.GONE
                titlePdfTerm1.visibility = View.GONE
            }

            if (ideaData.term2Unlocked) {
                ideatorTerm2ProgressLock.constraintProgressLock.visibility = View.GONE
                captionIdeatorTerm2.visibility = View.VISIBLE
                btnAddTerm2.visibility = View.VISIBLE
                titlePdfTerm2.visibility = View.VISIBLE
            } else {
                ideatorTerm2ProgressLock.constraintProgressLock.visibility = View.VISIBLE
                captionIdeatorTerm2.visibility = View.GONE
                btnAddTerm2.visibility = View.GONE
                titlePdfTerm2.visibility = View.GONE
            }

            if (ideaData.term3Unlocked) {
                ideatorTerm3ProgressLock.constraintProgressLock.visibility = View.GONE
                captionIdeatorTerm3.visibility = View.VISIBLE
                btnAddTerm3.visibility = View.VISIBLE
                titlePdfTerm3.visibility = View.VISIBLE
            } else {
                ideatorTerm3ProgressLock.constraintProgressLock.visibility = View.VISIBLE
                captionIdeatorTerm3.visibility = View.GONE
                btnAddTerm3.visibility = View.GONE
                titlePdfTerm3.visibility = View.GONE
            }

            when (ideaData.status) {
                getString(R.string.unfulfilled) -> tvStatusFund.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                getString(R.string.process_term1) -> tvStatusFund.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.Cyan_A_400_Dark
                    )
                )
                getString(R.string.process_term2) -> tvStatusFund.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.teal_200
                    )
                )
                getString(R.string.completed) -> tvStatusFund.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
            }
            tvStatusFund.text = ideaData.status
        }
    }
}