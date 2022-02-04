package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentSignUpBinding
import com.app.shotclock.utils.isVisible

class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    override fun getViewBinding(): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()

        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btNext.setOnClickListener {
            this.findNavController().navigate(R.id.action_signUpFragment_to_completeProfileFragment)
        }

        binding.tvSignIn.setOnClickListener {
            this.findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

    }
}