package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.databinding.FragmentSignUpBinding
import com.app.shotclock.utils.ImagePickerUtility1
import com.app.shotclock.utils.isVisible
import com.bumptech.glide.Glide

class SignUpFragment : ImagePickerUtility1<FragmentSignUpBinding>() {
    private var imageResultPath = ""


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

        binding.civUser.setOnClickListener {
            getImage(requireActivity(), 0, false)
        }

    }

    override fun selectedImage(imagePath: String?, code: Int) {
        if (!imagePath.isNullOrEmpty()) {
            imageResultPath = imagePath
            Glide.with(context!!).load(imageResultPath).into(binding.civUser)
        }
    }
}