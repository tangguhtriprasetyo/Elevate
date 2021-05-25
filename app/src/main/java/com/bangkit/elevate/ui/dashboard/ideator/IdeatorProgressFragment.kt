package com.bangkit.elevate.ui.dashboard.ideator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.elevate.R
import com.bangkit.elevate.databinding.FragmentIdeatorProgressBinding


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


        binding.layoutEmptyIdea.btnAddIdea.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(
                    R.id.host_fragment_activity_main,
                    addIdeaFragment,
                    AddIdeaFragment::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
        }
    }
}