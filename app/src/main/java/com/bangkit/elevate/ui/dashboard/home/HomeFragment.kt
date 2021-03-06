package com.bangkit.elevate.ui.dashboard.home

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.bangkit.elevate.databinding.FragmentHomeBinding
import com.bangkit.elevate.ui.dashboard.MainActivity
import com.bangkit.elevate.ui.dashboard.MainViewModel
import com.bangkit.elevate.ui.detail.DetailIdeaFragment
import com.bangkit.elevate.utils.loadImage
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeFragment : Fragment(), HomeClickCallback {

    @ExperimentalCoroutinesApi
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var ideasData: IdeaEntity
    private lateinit var userDataProfile: UserEntity

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var filterQuery: String = "No Filters"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

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
                    binding.icAvatarHome.loadImage(userDataProfile.avatar)
                    setListIdeas("No Filters")
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })

        binding.icFilterData.setOnClickListener {
            // TODO MACHINE LEARNING
            dialog()
        }

    }

    private fun setListIdeas(filterQuery: String) {
        homeViewModel.getListIdeas(filterQuery).observe(viewLifecycleOwner, { listIdeas ->
            if (listIdeas != null) {
                Log.d("listIdeas: ", listIdeas.toString())
                val homeAdapter = HomeAdapter(this@HomeFragment)
                homeAdapter.setListIdeas(listIdeas)
                with(binding.recyclerView) {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = homeAdapter
                }
                showLoading(false)
            }
        })
    }

    private fun dialog() {
        val options = arrayOf("No Filters", "Parcels", "Groceries", "Foods", "Snacks", "Drinks")
        var selectedItem = 0
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Filter Ideas By Category")
        builder.setSingleChoiceItems(
            options, 0
        ) { _: DialogInterface, item: Int ->
            selectedItem = item
        }
        builder.setPositiveButton("Filter") { dialogInterface: DialogInterface, _: Int ->
            //TODO Machine Learning Filter
            filterQuery = options[selectedItem]
            setListIdeas(filterQuery)
            Log.d("SelectedITem", filterQuery)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("cancel") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }
        builder.create()
        builder.show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarHome.visibility = View.VISIBLE
        } else {
            binding.progressBarHome.visibility = View.GONE
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}