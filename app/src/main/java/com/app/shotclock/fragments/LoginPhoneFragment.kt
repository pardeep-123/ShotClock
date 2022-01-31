package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentLoginPhoneBinding

class LoginPhoneFragment : BaseFragment<FragmentLoginPhoneBinding>() {

    override fun getViewBinding(): FragmentLoginPhoneBinding {
        return FragmentLoginPhoneBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btSubmit.setOnClickListener {
            findNavController().navigate(com.app.shotclock.R.id.action_loginPhoneFragment_to_otpFragment)
        }
    }

}