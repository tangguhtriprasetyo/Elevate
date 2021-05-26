package com.bangkit.elevate.ui.dashboard.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.elevate.databinding.FragmentProfileBinding


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

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        _binding.ProfileTopUp.setOnClickListener{
//            val mCategoryFragment = TopUpFragment()
//            val mFragmentManager = fragmentManager
//            mFragmentManager?.beginTransaction()?.apply {
//                replace(R.id.ProfileFrag, mCategoryFragment, TopUpFragment::class.java.simpleName)
//                addToBackStack(null)
//                commit()
//            }
        }
//        val textView: TextView = binding.textNotifications
//        profileViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

}