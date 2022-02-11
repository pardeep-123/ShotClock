package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.MyRequestsAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentMyRequestsBinding
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible

class MyRequestsFragment : BaseFragment<FragmentMyRequestsBinding>() {

    private var requestAdapter = MyRequestsAdapter()

    override fun getViewBinding(): FragmentMyRequestsBinding {
        return FragmentMyRequestsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleClicks()
//        handleHomeFragmentBackPress()

        requestAdapter = MyRequestsAdapter()
        binding.rvMyRequests.adapter = requestAdapter

    }

    private fun handleClicks() {
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivMenu.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
        }

        binding.tvCurrent.setOnClickListener {
            binding.tvNoDataFound.isGone()
             binding.clSpeedDate.isVisible()

        }
        binding.tvPast.setOnClickListener {
            binding.clSpeedDate.isGone()
            binding.tvNoDataFound.isVisible()
        }
    }

    //        for back press in fragment
    private fun handleHomeFragmentBackPress() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //Handle back event from any fragment
                    activity?.onBackPressed()
                }
            })
    }
}