package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.shotclock.adapters.SubscriptionViewPager
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentSubscriptionBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.SubscriptionPlansResponse
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.viewmodels.HomeViewModel
import javax.inject.Inject

class SubscriptionFragment : BaseFragment<FragmentSubscriptionBinding>(), Observer<Resource<SubscriptionPlansResponse>> {

    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val list = ArrayList<SubscriptionPlansResponse.SubscriptionPlansBody>()
    private var subscriptionAdapter: SubscriptionViewPager? = null

    override fun getViewBinding(): FragmentSubscriptionBinding {
        return FragmentSubscriptionBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewModel()
        clickHandles()
        setAdapters()

    }

    private fun setAdapters() {
        subscriptionAdapter = SubscriptionViewPager(requireContext(),list)
        binding.viewPager.adapter = subscriptionAdapter
        binding.viewPagerIndicator.setViewPager(binding.viewPager)
    }

    private fun clickHandles() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun configureViewModel() {
        homeViewModel = ViewModelProviders.of(this,viewModelFactory).get(HomeViewModel::class.java)
        homeViewModel.subscriptionPlans().observe(viewLifecycleOwner,this)
    }

    override fun onChanged(t: Resource<SubscriptionPlansResponse>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                list.addAll(t.data?.body!!)
                 subscriptionAdapter?.notifyDataSetChanged()
            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                showError(t.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

}