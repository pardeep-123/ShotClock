package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.adapters.MyRequestsAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentMyRequestsBinding

class MyRequestsFragment : BaseFragment<FragmentMyRequestsBinding>() {


    override fun getViewBinding(): FragmentMyRequestsBinding {
        return FragmentMyRequestsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMyRequests.adapter = MyRequestsAdapter()

    }
}