package com.bangkit.elevate.ui.bottomsheet

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bangkit.elevate.databinding.FragmentBottomSheetUploadBinding
import com.bangkit.elevate.ui.dashboard.ideator.IdeatorViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetUploadFragment : BottomSheetDialogFragment() {

    companion object {
        const val EXTRA_FILE = "extra_file"
        const val EXTRA_FILE_NAME = "extra_file_name"
        const val EXTRA_UID = "extra_uid"
        const val EXTRA_NAME = "extra_name"
    }

    private lateinit var binding: FragmentBottomSheetUploadBinding
    private val ideatorViewModel: IdeatorViewModel by activityViewModels()

    private var uriPath: Uri? = null
    private var filesName: String? = null
    private var uid: String? = null
    private var username: String? = null
    private var proposalUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            filesName = requireArguments().getString(EXTRA_FILE_NAME)!!
            uid = requireArguments().getString(EXTRA_UID)!!
            username = requireArguments().getString(EXTRA_NAME)!!
            uriPath = Uri.parse(requireArguments().getString(EXTRA_FILE))
            binding.tvFileName.text = filesName
        }

        binding.btnUploadFile.setOnClickListener {
            binding.progressBarBottomSheetUpload.visibility = View.VISIBLE
            ideatorViewModel.uploadFiles(
                uriPath!!,
                "$uid$username",
                "PDF",
                filesName.toString()
            ).observe(viewLifecycleOwner, { downloadUrl ->
                if (downloadUrl != null) {
                    proposalUrl = downloadUrl.toString()
                    Log.d("proposalUrl: ", proposalUrl.toString())
                    binding.progressBarBottomSheetUpload.visibility = View.GONE
                    Toast.makeText(activity, "Uploaded", Toast.LENGTH_SHORT).show()

                    ideatorViewModel.getStatusTerm1(true)
                    this.dismiss()
                }
            })
        }
    }
}