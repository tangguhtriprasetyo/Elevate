package com.bangkit.elevate.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bangkit.elevate.databinding.FragmentBottomSheetDonateBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDonateFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetDonateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBottomSheetDonateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDonateDialog.setOnClickListener {
            Toast.makeText(activity, "Oke", Toast.LENGTH_SHORT).show()
        }
    }
}