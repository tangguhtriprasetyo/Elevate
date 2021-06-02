package com.bangkit.elevate.ui.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bangkit.elevate.databinding.FragmentBottomSheetDonateBinding
import com.bangkit.elevate.ui.detail.DetailIdeaViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class BottomSheetDonateFragment : BottomSheetDialogFragment() {

    companion object {
        const val EXTRA_IDEA_ID = "extra_idea_id"
        const val EXTRA_BALANCED_USER = "extra_balanced_user"
        const val EXTRA_USER_ID = "extra_user_id"
    }

    private lateinit var binding: FragmentBottomSheetDonateBinding
    private var ideatorId: String? = null
    private var userId: String? = null
    private var userBalance: Long = 0
    private val detailIdeaViewModel: DetailIdeaViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBottomSheetDonateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            ideatorId = requireArguments().getString(EXTRA_IDEA_ID)!!
            userId = requireArguments().getString(EXTRA_USER_ID)!!
            userBalance = requireArguments().getLong(EXTRA_BALANCED_USER)
            Log.d("BalanceBottomDonate: ", userBalance.toString())
        }


        binding.btnDonateDialog.setOnClickListener {
            binding.progressBarDonate.visibility = View.VISIBLE
            val donation = binding.etDonateDialog.text.toString()
            val totalDonation: Int = donation.toInt()
            Log.d("totalDonation: ", totalDonation.toString())
            Log.d("userBalance: ", userBalance.toString())

            if (userBalance.toInt() > totalDonation) {
                detailIdeaViewModel.updateDonation(
                    userId.toString(),
                    ideatorId.toString(),
                    totalDonation.toLong(),
                    userBalance.toLong()
                )
                    .observe(viewLifecycleOwner, { isSuccessful ->
                        if (isSuccessful) {
                            Toast.makeText(activity, "Successful Donating", Toast.LENGTH_SHORT)
                                .show()
                            detailIdeaViewModel.getStatusDonation(true)
                            binding.progressBarDonate.visibility = View.GONE
                            this.dismiss()
                        }
                    })
            } else {
                Toast.makeText(activity, "insufficient Balance", Toast.LENGTH_SHORT).show()
            }
        }
    }
}