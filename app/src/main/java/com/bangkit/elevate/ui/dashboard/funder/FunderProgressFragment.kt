package com.bangkit.elevate.ui.dashboard.funder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.elevate.databinding.FragmentFunderProgressBinding

class FunderProgressFragment : Fragment() {

    private lateinit var funderProgressViewModel: FunderProgressViewModel
    private var _binding: FragmentFunderProgressBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        funderProgressViewModel =
            ViewModelProvider(this).get(FunderProgressViewModel::class.java)

        _binding = FragmentFunderProgressBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.layoutEmptyFunder.constraintEmptyFunder.visibility = View.VISIBLE
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}