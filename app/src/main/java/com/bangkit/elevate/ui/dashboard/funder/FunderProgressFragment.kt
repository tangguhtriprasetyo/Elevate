package com.bangkit.elevate.ui.dashboard.funder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.elevate.R
import com.bangkit.elevate.data.IdeaEntity
import com.bangkit.elevate.data.UserEntity
import com.bangkit.elevate.databinding.FragmentFunderProgressBinding
import com.bangkit.elevate.ui.dashboard.MainActivity
import com.bangkit.elevate.ui.dashboard.MainViewModel
import com.bangkit.elevate.ui.dashboard.home.HomeClickCallback
import com.bangkit.elevate.ui.detail.DetailIdeaFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class FunderProgressFragment : Fragment(), HomeClickCallback {

    private var _binding: FragmentFunderProgressBinding? = null
    private val binding get() = _binding!!

    private val funderProgressViewModel: FunderProgressViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var ideasData: IdeaEntity
    private lateinit var userDataProfile: UserEntity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFunderProgressBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)

        ideasData = IdeaEntity()
        userDataProfile = UserEntity()

        if (arguments != null) {
            userDataProfile = requireArguments().getParcelable("UserData")!!
        }

        mainViewModel.setUserProfile(userDataProfile.uid.toString())
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                    if (userProfile.totalFunded > 0) {
                        binding.layoutEmptyFunder.constraintEmptyFunder.visibility = View.GONE
                        binding.listProgressFunder.visibility = View.VISIBLE
                    } else {
                        binding.layoutEmptyFunder.constraintEmptyFunder.visibility = View.VISIBLE
                        binding.listProgressFunder.visibility = View.GONE
                    }
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })

        funderProgressViewModel.getListIdeas().observe(viewLifecycleOwner, { listIdeas ->
            if (listIdeas != null) {
                Log.d("listIdeas: ", listIdeas.toString())
                val funderProgressAdapter = FunderProgressAdapter(this@FunderProgressFragment)
                funderProgressAdapter.setListIdeas(listIdeas)
                with(binding.listProgressFunder) {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = funderProgressAdapter
                }
                showLoading(false)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(ideas: IdeaEntity) {
        val mBundle = Bundle()
        mBundle.putParcelable(DetailIdeaFragment.EXTRA_DATA_IDEA, ideas)
        mBundle.putParcelable(DetailIdeaFragment.EXTRA_DATA_USER, userDataProfile)

        val detailIdeaFragment = DetailIdeaFragment()
        detailIdeaFragment.arguments = mBundle

        val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav)
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(
                R.id.host_fragment_activity_main,
                detailIdeaFragment,
                MainActivity.CHILD_FRAGMENT
            )
            addToBackStack(null)
            commit()
        }
        bottomNav.visibility = View.GONE
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarFunder.visibility = View.VISIBLE
        } else {
            binding.progressBarFunder.visibility = View.GONE
        }
    }
}