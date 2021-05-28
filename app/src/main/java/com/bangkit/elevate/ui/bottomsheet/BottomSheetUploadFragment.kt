package com.bangkit.elevate.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bangkit.elevate.databinding.FragmentBottomSheetUploadBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetUploadFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetUploadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUploadFile.setOnClickListener {
            Toast.makeText(activity, "Uploaded", Toast.LENGTH_SHORT).show()
        }
    }
}