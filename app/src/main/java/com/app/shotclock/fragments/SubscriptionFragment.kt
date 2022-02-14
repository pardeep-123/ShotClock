package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.adapters.SubscriptionViewPager
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentSubscriptionBinding
import com.app.shotclock.models.SubscriptionModel
import com.app.shotclock.utils.isVisible

class SubscriptionFragment : BaseFragment<FragmentSubscriptionBinding>() {
    private var selectedArrayList = ArrayList<Int>()
    private var unSelectedArrayList = ArrayList<Int>()
    private val list = ArrayList<SubscriptionModel>()

    override fun getViewBinding(): FragmentSubscriptionBinding {
        return FragmentSubscriptionBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()

        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        list.add(SubscriptionModel("$5.99", "for 1 Day", "connect with your matches for 1 day"))
        list.add(SubscriptionModel("$9.99", "for 1 Month", "connect with your matches for 1 month"))
        list.add(SubscriptionModel("$100.99", "for 1 Year", "connect with your matches for 1 year"))
        binding.viewPager.adapter = SubscriptionViewPager(requireContext(), list)
        binding.viewPagerIndicator.setViewPager(binding.viewPager)

//        binding.viewPagerIndicator.attachToRecyclerView(binding.rvWalkThrough, pagerSnapHelper)
    }


}