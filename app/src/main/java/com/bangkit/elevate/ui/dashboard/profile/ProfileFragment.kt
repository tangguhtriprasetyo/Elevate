package com.bangkit.elevate.ui.dashboard.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bangkit.elevate.R
import com.bangkit.elevate.data.UserEntity
import com.bangkit.elevate.data.preference.UserPreference
import com.bangkit.elevate.databinding.FragmentProfileBinding
import com.bangkit.elevate.ui.dashboard.MainActivity
import com.bangkit.elevate.ui.dashboard.MainViewModel
import com.bangkit.elevate.ui.dashboard.profile.saldo.TopUpFragment
import com.bangkit.elevate.ui.dashboard.profile.saldo.WithdrawnFragment
import com.bangkit.elevate.utils.loadImage
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.NumberFormat
import java.util.*


class ProfileFragment : Fragment() {

    private lateinit var _binding: FragmentProfileBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var userDataProfile: UserEntity

    private val mainViewModel: MainViewModel by activityViewModels()

    private var isIdeator = false
    private var isEditProfile = false

    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mUserPreference = UserPreference(requireContext())
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            userDataProfile = requireArguments().getParcelable("UserData")!!
        }

        val topUpFrag = TopUpFragment()
        val withdrawnFrag = WithdrawnFragment()

        isIdeator = mUserPreference.getRole()
        binding.switchIdeator.isChecked = isIdeator
        setRole()


        mainViewModel.setUserProfile(userDataProfile.uid.toString())
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                    setProfileData(userDataProfile)
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })

        _binding.ProfileTopUp.setOnClickListener {
            val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav)
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(
                    R.id.host_fragment_activity_main,
                    topUpFrag,
                    MainActivity.CHILD_FRAGMENT
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
                    MainActivity.CHILD_FRAGMENT
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

        binding.EditProfile.setOnClickListener {
            if (isEditProfile) {
                with(binding) {
                    etName.isEnabled = false
                    etAddress.isEnabled = false
                    etEmail.isEnabled = false
                    etPhone.isEnabled = false
                    EditProfile.text = "Edit Profile"

                    userDataProfile.username = etName.text.toString()
                    userDataProfile.address = etAddress.text.toString()
                    userDataProfile.email = etEmail.text.toString()
                    userDataProfile.phone = etPhone.text.toString()

                    mainViewModel.editProfileUser(userDataProfile)

                    Log.d("EditProfile: ", userDataProfile.toString())
                }
            } else {
                with(binding) {
                    etName.isEnabled = true
                    etAddress.isEnabled = true
                    etEmail.isEnabled = true
                    etPhone.isEnabled = true
                    EditProfile.text = "Save"
                }
            }
            isEditProfile = !isEditProfile
        }
    }

    private fun setProfileData(userDataProfile: UserEntity) {
        val localeId = Locale("in", "ID")
        val priceFormat = NumberFormat.getCurrencyInstance(localeId)

        with(binding) {
            ProfilePicture.loadImage(userDataProfile.avatar)
            ProfileUsername.text = userDataProfile.username
            ProfileBalance.text = priceFormat.format(userDataProfile.balance)
            etName.setText(
                if (userDataProfile.username == null) {
                    " - "
                } else {
                    userDataProfile.username
                }
            )

            etEmail.setText(
                if (userDataProfile.email == null) {
                    " - "
                } else {
                    userDataProfile.email
                }
            )

            etPhone.setText(
                if (userDataProfile.phone == null) {
                    " - "
                } else {
                    userDataProfile.phone
                }
            )

            etAddress.setText(
                if (userDataProfile.address == null) {
                    " - "
                } else {
                    userDataProfile.address
                }
            )
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