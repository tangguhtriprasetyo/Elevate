package com.bangkit.elevate.ui.dashboard.ideator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.elevate.R
import com.bangkit.elevate.databinding.FragmentIdeatorProgressBinding
import com.bangkit.elevate.ui.bottomsheet.BottomSheetDonateFragment
import com.bangkit.elevate.ui.bottomsheet.BottomSheetUploadFragment
import com.bangkit.elevate.ui.dashboard.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class IdeatorProgressFragment : Fragment() {

    private lateinit var binding: FragmentIdeatorProgressBinding

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
        val bottomSheetDonate = BottomSheetDonateFragment()
        val bottomSheetUpload = BottomSheetUploadFragment()

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
            bottomSheetDonate.show(
                requireActivity().supportFragmentManager,
                BottomSheetDonateFragment::class.java.simpleName
            )
        }

        binding.layoutIdeatorProgress.btnAddTerm1.setOnClickListener {
            bottomSheetUpload.show(
                requireActivity().supportFragmentManager,
                BottomSheetUploadFragment::class.java.simpleName
            )
        }
    }
}