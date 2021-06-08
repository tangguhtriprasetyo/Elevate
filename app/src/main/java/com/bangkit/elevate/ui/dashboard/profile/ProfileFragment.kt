package com.bangkit.elevate.ui.dashboard.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import java.text.NumberFormat
import java.util.*


class ProfileFragment : Fragment() {

    private lateinit var _binding: FragmentProfileBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var userDataProfile: UserEntity
    private lateinit var uriImagePath: Uri

    private val mainViewModel: MainViewModel by activityViewModels()

    private var isIdeator = false
    private var isEditProfile = false
    private var isChangeAvatar = false

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

        val getImage =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uriImage ->
                if (uriImage.path != null) {
                    uriImagePath = uriImage
                    binding.ProfilePicture.setImageURI(uriImagePath)
                    isChangeAvatar = true
                }
            }

        val topUpFrag = TopUpFragment()
        val withdrawnFrag = WithdrawnFragment()

        isIdeator = mUserPreference.getRole()
        binding.switchIdeator.isChecked = isIdeator
        setRole()


        mainViewModel.setUserProfile(userDataProfile.uid.toString())
            .observe(viewLifecycleOwner, { userProfile ->
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
                    icChangePicture.visibility = View.GONE
                    EditProfile.text = getString(R.string.title_edit_profile)

                    userDataProfile.username = etName.text.toString()
                    userDataProfile.address = etAddress.text.toString()
                    userDataProfile.email = etEmail.text.toString()
                    userDataProfile.phone = etPhone.text.toString()

                    if (isChangeAvatar) {
                        progressBarProfile.visibility = View.VISIBLE
                        mainViewModel.uploadImages(
                            uriImagePath,
                            "${userDataProfile.uid.toString()}${userDataProfile.username}",
                            "Images",
                            "profilePicture"
                        ).observe(viewLifecycleOwner, { downloadUrl ->
                            if (downloadUrl != null) {
                                userDataProfile.avatar = downloadUrl.toString()
                                mainViewModel.editProfileUser(userDataProfile)
                                progressBarProfile.visibility = View.GONE
                                Toast.makeText(
                                    requireActivity(),
                                    "Profile Updated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireActivity(),
                                    "Update Profile Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressBarProfile.visibility = View.GONE
                            }
                            Log.d("imgFilesUrl: ", downloadUrl.toString())
                        })
                    } else {
                        mainViewModel.editProfileUser(userDataProfile)
                        progressBarProfile.visibility = View.GONE
                        Toast.makeText(requireActivity(), "Profile Updated", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                with(binding) {
                    etName.isEnabled = true
                    etAddress.isEnabled = true
                    etEmail.isEnabled = true
                    etPhone.isEnabled = true
                    icChangePicture.visibility = View.VISIBLE
                    EditProfile.text = getString(R.string.title_save)
                }
            }
            isEditProfile = !isEditProfile
        }

        binding.icChangePicture.setOnClickListener {
            getImage.launch(arrayOf("image/*"))
        }

        binding.btnLogout.setOnClickListener {
            val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
            val googleSignInClient: GoogleSignInClient
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
            firebaseAuth.signOut()
            googleSignInClient.signOut()
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