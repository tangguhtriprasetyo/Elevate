package com.bangkit.elevate.ui.dashboard.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.elevate.R
import com.bangkit.elevate.data.preference.UserPreference
import com.bangkit.elevate.databinding.FragmentProfileBinding
import com.bangkit.elevate.ui.dashboard.profile.saldo.TopUpFragment
import com.bangkit.elevate.ui.dashboard.profile.saldo.WithdrawnFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var _binding: FragmentProfileBinding
    private lateinit var mUserPreference: UserPreference

    private var isIdeator = false

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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mUserPreference = UserPreference(requireContext())
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topUpFrag = TopUpFragment()
        val withdrawnFrag = WithdrawnFragment()

        isIdeator = mUserPreference.getRole()
        binding.switchIdeator.isChecked = isIdeator
        setRole()

        _binding.ProfileTopUp.setOnClickListener {
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

        binding.switchIdeator.setOnCheckedChangeListener { _, isChecked ->
            isIdeator = isChecked
            mUserPreference.setRole(isIdeator)
            setRole()
        }
    }

    private fun setRole() {
        if (isIdeator) {
            binding.ProfileRole.text = getString(R.string.title_ideator)
        } else {
            binding.ProfileRole.text = getString(R.string.title_funder)
        }
    }

}