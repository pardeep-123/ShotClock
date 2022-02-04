package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentChangePasswordBinding
import com.app.shotclock.utils.isVisible

class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {

    override fun getViewBinding(): FragmentChangePasswordBinding {
        return FragmentChangePasswordBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()

        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btUpdate.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}