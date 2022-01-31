package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentOtpBinding

class OtpFragment : BaseFragment<FragmentOtpBinding>() {

    override fun getViewBinding(): FragmentOtpBinding {
        return FragmentOtpBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBackOtp.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btSubmit.setOnClickListener {
            this.findNavController().navigate(R.id.action_otpFragment_to_signUpFragment)
        }

    }

}