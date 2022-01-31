package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>() {


    override fun getViewBinding(): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvForgotPassword.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.tvSignUp.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.btSignIn.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_homeActivity)
        }
    }

}