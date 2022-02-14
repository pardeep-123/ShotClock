package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.NotificationsAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentNotificationBinding
import com.app.shotclock.utils.isVisible

class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {


    override fun getViewBinding(): FragmentNotificationBinding {
        return FragmentNotificationBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleClicks()

        binding.rvNotification.adapter = NotificationsAdapter()

    }

    private fun handleClicks() {
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivMenu.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
        }
    }
}