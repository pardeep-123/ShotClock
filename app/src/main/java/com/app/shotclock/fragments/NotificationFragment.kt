package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
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

        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()

        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.rvNotification.adapter = NotificationsAdapter()

    }
}