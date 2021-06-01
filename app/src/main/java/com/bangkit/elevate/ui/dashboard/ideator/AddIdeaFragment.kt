package com.bangkit.elevate.ui.dashboard.ideator

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bangkit.elevate.R
import com.bangkit.elevate.data.IdeaEntity
import com.bangkit.elevate.data.UserEntity
import com.bangkit.elevate.databinding.FragmentAddIdeaBinding
import com.bangkit.elevate.ui.dashboard.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class AddIdeaFragment : Fragment() {

    private lateinit var binding: FragmentAddIdeaBinding
    private lateinit var userDataProfile: UserEntity
    private lateinit var ideaData: IdeaEntity
    private val mainViewModel: MainViewModel by activityViewModels()
    private val ideatorViewModel: IdeatorViewModel by activityViewModels()
    private lateinit var uriPath: Uri
    private lateinit var uriImagePath: Uri
    private lateinit var filesName: String
    private lateinit var proposalUrl: String
    private lateinit var imgFilesName: String
    private lateinit var imgFilesUrl: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddIdeaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ideaData = IdeaEntity()
        userDataProfile = UserEntity()

        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })

        val getImage =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uriImage ->
                if (uriImage.path != null) {

                    uriImagePath = uriImage
                    imgFilesName = uriImage.path.toString()
                    val cut = imgFilesName.lastIndexOf('/')
                    if (cut != -1) {
                        imgFilesName = imgFilesName.substring(cut + 1)
                    }
                    with(binding) {
                        titleBrowsePhoto.visibility = View.GONE
                        photoAfterUpload.visibility = View.VISIBLE
                        titleImgAfterUpload.visibility = View.VISIBLE

                        titleImgAfterUpload.text = imgFilesName
                        photoAfterUpload.setImageURI(uriImagePath)
                    }
                }
            }

        val getPdf = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uriPdf ->

            if (uriPdf.path != null) {
                uriPath = uriPdf
                filesName = uriPdf.path.toString()
                val cut = filesName.lastIndexOf('/')
                if (cut != -1) {
                    filesName = filesName.substring(cut + 1)
                }

                with(binding) {
                    icProposalAfterUpload.visibility = View.VISIBLE
                    titleBrowseProposal.visibility = View.VISIBLE
                    titleAfterUpload.visibility = View.VISIBLE
                    titleAfterUpload.text = filesName
                }
            }
        }

        binding.titleBrowseProposal.setOnClickListener {
            getPdf.launch(arrayOf("application/pdf"))
        }

        binding.titleBrowsePhoto.setOnClickListener {
            getImage.launch(arrayOf("image/*"))
        }

        binding.btnSubmitIdea.setOnClickListener {
            uploadFiles()
        }

    }

    private fun uploadFiles() {
        //TODO Check Error

        showLoading(true)
        ideatorViewModel.uploadFiles(
            uriPath,
            "${userDataProfile.uid.toString()}${userDataProfile.username}",
            "PDF",
            filesName
        ).observe(viewLifecycleOwner, { downloadUrl ->
            if (downloadUrl != null) {
                proposalUrl = downloadUrl.toString()
                uploadImg()
                Log.d("proposalUrl: ", proposalUrl)
            }
        })
    }

    private fun uploadImg() {
        ideatorViewModel.uploadImages(
            uriImagePath,
            "${userDataProfile.uid.toString()}${userDataProfile.username}",
            "Images",
            imgFilesName
        ).observe(viewLifecycleOwner, { downloadUrl ->
            if (downloadUrl != null) {
                imgFilesUrl = downloadUrl.toString()
                uploadAllDataIdea()
                Log.d("imgFilesUrl: ", imgFilesUrl)
            }
            Log.d("imgFilesUrl: ", downloadUrl.toString())
        })
    }

    private fun uploadAllDataIdea() {
        with(binding) {
            ideaData.brandName = etBrandName.text.toString()
            ideaData.businessIdea = etBusinessIdea.text.toString()
            ideaData.description = etDescription.text.toString()
            ideaData.requiredCost = etCost.text.toString().toLong()
            ideaData.location = etLocation.text.toString()
            ideaData.proposalFile = proposalUrl
            ideaData.logoFile = imgFilesUrl
            ideaData.ideatorUid = userDataProfile.uid
            ideaData.status = getString(R.string.unfulfilled)
        }

        ideatorViewModel.postIdeaData(ideaData, userDataProfile.uid.toString())
            .observe(viewLifecycleOwner, { isSuccessful ->
                if (isSuccessful) {
                    showLoading(false)

                    val bottomNav: BottomNavigationView =
                        requireActivity().findViewById(R.id.bottom_nav)
                    bottomNav.visibility = View.VISIBLE
                    requireActivity().supportFragmentManager.popBackStackImmediate()
                }
            })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarAddIdea.visibility = View.VISIBLE
        } else {
            binding.progressBarAddIdea.visibility = View.GONE
        }
    }
}