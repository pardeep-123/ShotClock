package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.NotificationsAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentNotificationBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.GetNotificationResponse
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.viewmodels.HomeViewModel
import javax.inject.Inject

class NotificationFragment : BaseFragment<FragmentNotificationBinding>(), Observer<Resource<GetNotificationResponse>> {

    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var notificationAdapter: NotificationsAdapter? = null
    private var notificationList = ArrayList<GetNotificationResponse.GetNotificationBody>()

    override fun getViewBinding(): FragmentNotificationBinding {
        return FragmentNotificationBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewModel()
        handleClicks()

        notificationAdapter = NotificationsAdapter(requireContext(),notificationList)
        binding.rvNotification.adapter = notificationAdapter

    }

    private fun configureViewModel() {
        homeViewModel = ViewModelProviders.of(this,viewModelFactory).get(HomeViewModel::class.java)
        homeViewModel.getNotifications().observe(viewLifecycleOwner,this)
    }

    private fun handleClicks() {
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivMenu.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
        }
    }

    override fun onChanged(t: Resource<GetNotificationResponse>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                notificationList.addAll(t.data?.body!!)
                notificationAdapter?.notifyDataSetChanged()
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