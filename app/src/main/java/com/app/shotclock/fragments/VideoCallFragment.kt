package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentVideoCallBinding
import com.app.shotclock.utils.isVisible


class VideoCallFragment : BaseFragment<FragmentVideoCallBinding>() {

    override fun getViewBinding(): FragmentVideoCallBinding {
        return FragmentVideoCallBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()

        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}