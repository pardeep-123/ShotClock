package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentSafeDatingPolicyBinding
import com.app.shotclock.utils.isVisible


class SafeDatingPolicyFragment : BaseFragment<FragmentSafeDatingPolicyBinding>() {

    override fun getViewBinding(): FragmentSafeDatingPolicyBinding {
        return FragmentSafeDatingPolicyBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickHandle()

    }

    private fun clickHandle() {
        binding.tb.ivMenu.isVisible()
        binding.tb.ivAppLogo2.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
        }
    }

}