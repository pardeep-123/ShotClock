package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentSignUpBinding

class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    override fun getViewBinding(): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar :Toolbar = view.findViewById(R.id.toolbar)
        val ivBack : ImageView = toolbar.findViewById(R.id.ivBack)
        ivBack.setOnClickListener {
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