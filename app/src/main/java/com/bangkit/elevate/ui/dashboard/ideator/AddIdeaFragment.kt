package com.bangkit.elevate.ui.dashboard.ideator

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bangkit.elevate.databinding.FragmentAddIdeaBinding
import java.io.File


class AddIdeaFragment : Fragment() {

    private lateinit var binding: FragmentAddIdeaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddIdeaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getImage =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uriImage ->
                if (uriImage.path != null) {

                    var imgPath = uriImage.path
                    val imgCut = imgPath?.lastIndexOf('/')
                    if (imgCut != -1) {
                        imgPath = imgCut?.plus(1)?.let { imgPath?.substring(it) }
                    }
                    with(binding) {
                        titleBrowsePhoto.visibility = View.GONE
                        photoAfterUpload.visibility = View.VISIBLE
                        titleImgAfterUpload.visibility = View.VISIBLE

                        titleImgAfterUpload.text = imgPath
                        photoAfterUpload.setImageURI(uriImage)
                    }
                }
            }

        val getPdf = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uriPdf ->

            if (uriPdf.path != null) {
                val file = Uri.fromFile(File(uriPdf.path.toString()))
                var path = uriPdf.path
                val cut = path?.lastIndexOf('/')
                if (cut != -1) {
                    path = cut?.plus(1)?.let { path?.substring(it) }
                }

                with(binding) {
                    icProposalAfterUpload.visibility = View.VISIBLE
                    titleBrowseProposal.visibility = View.VISIBLE
                    titleAfterUpload.visibility = View.VISIBLE
                    titleAfterUpload.text = path

                    Log.d("getPdfPath: ", uriPdf.path.toString())
                    Log.d("getPdfPathSegment: ", uriPdf.pathSegments.toString())
                    Log.d("getPdfLastPathSegment: ", uriPdf.lastPathSegment.toString())
                    Log.d("getPdfLastPathSegment: ", uriPdf.lastPathSegment.toString())
                    Log.d("getUserInfo: ", uriPdf.userInfo.toString())
                    Log.d("encodedPath: ", uriPdf.encodedPath.toString())
                    Log.d("encodedQuery: ", uriPdf.encodedQuery.toString())
                    Log.d("queryParameterNames: ", uriPdf.queryParameterNames.toString())
                    Log.d("scheme: ", uriPdf.scheme.toString())
                    Log.d("schemeSpecificPart: ", uriPdf.schemeSpecificPart.toString())
                    Log.d("File: ", file.toString())
                }
            }
        }

        binding.titleBrowseProposal.setOnClickListener {
            getPdf.launch(arrayOf("application/pdf"))
        }

        binding.titleBrowsePhoto.setOnClickListener {
            getImage.launch(arrayOf("image/*"))
        }

    }
}