package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentPrivcayPolicyBinding
import com.app.shotclock.utils.isVisible

class PrivacyPolicyFragment : BaseFragment<FragmentPrivcayPolicyBinding>() {

    override fun getViewBinding(): FragmentPrivcayPolicyBinding {
        return FragmentPrivcayPolicyBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickHandle()
    }

    private fun clickHandle() {
        binding.tb.ivMenu.isVisible()
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
        }
    }

}