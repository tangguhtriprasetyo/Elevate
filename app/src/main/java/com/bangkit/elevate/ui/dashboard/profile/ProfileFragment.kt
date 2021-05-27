package com.bangkit.elevate.ui.dashboard.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.elevate.R
import com.bangkit.elevate.TopUpFragment
import com.bangkit.elevate.WithdrawnFragment
import com.bangkit.elevate.databinding.FragmentProfileBinding
import com.bangkit.elevate.ui.dashboard.ideator.AddIdeaFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class ProfileFragment : Fragment(){

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var _binding: FragmentProfileBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        val topUpFrag = TopUpFragment()
        val withdrawnFrag = WithdrawnFragment()
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        _binding.ProfileTopUp.setOnClickListener{
            val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav)
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(
                    R.id.host_fragment_activity_main,
                    topUpFrag,
                    TopUpFragment::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
            bottomNav.visibility = View.GONE
        }

        _binding.ProfileWithdraw.setOnClickListener {
            val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav)
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(
                    R.id.host_fragment_activity_main,
                    withdrawnFrag,
                    WithdrawnFragment::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
            bottomNav.visibility = View.GONE
        }
//        val textView: TextView = binding.textNotifications
//        profileViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

}