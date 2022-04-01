package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.cache.saveString
import com.app.shotclock.cache.saveToken
import com.app.shotclock.cache.saveUser
import com.app.shotclock.databinding.FragmentLoginBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.LoginRequestModel
import com.app.shotclock.models.LoginResponseModel
import com.app.shotclock.utils.*
import com.app.shotclock.viewmodels.LoginSignUpViewModel
import javax.inject.Inject

class LoginFragment : BaseFragment<FragmentLoginBinding>(), Observer<Resource<LoginResponseModel>> {

    lateinit var loginSignUpViewModel: LoginSignUpViewModel
    private var isMyChecked = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun getViewBinding(): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickHandles()
        configureViewModel()

    }

    private fun clickHandles() {

        binding.etEmail.setText(RememberShared(requireContext()).getString("email"))
        binding.etPassword.setText(RememberShared(requireContext()).getString("password"))

        binding.tvForgotPassword.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.tvSignUp.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.btSignIn.setOnClickListener {
            if (Validation().verifyLoginValidation(
                    requireActivity(),
                    binding.etEmail.text.toString().trim(),
                    binding.etPassword.text.toString().trim()
                )
            ) {
                userLogin()
            }

        }


        binding.cbRememberMe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isMyChecked = true
                RememberShared(requireContext()).setString("email", binding.etEmail.text.toString().trim())
                RememberShared(requireContext()).setString("password", binding.etPassword.text.toString().trim())
            } else {
                isMyChecked = false
                RememberShared(requireContext()).clearShared()
            }
        }


    }

    private fun userLogin() {

        if (!isMyChecked)   RememberShared(requireContext()).clearShared()

        val body = LoginRequestModel()
        body.email = binding.etEmail.text.trim().toString()
        body.password = binding.etPassword.text.trim().toString()
        body.device_type = 2
        body.device_token = Prefs.with(requireContext()).getString("token","0")
        loginSignUpViewModel.loginUser(body).observe(viewLifecycleOwner,this)
    }

    private fun configureViewModel() {
        loginSignUpViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(LoginSignUpViewModel::class.java)
    }

    override fun onChanged(t: Resource<LoginResponseModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isVisible()
                saveUser(requireContext(), t.data?.body!!)
                saveToken(requireContext(), t.data.body.authKey)

                if (t.data.body.isComplete == 1) {
                    this.findNavController().navigate(R.id.action_loginFragment_to_homeActivity)
                    saveString(requireContext(), t.data.body.authKey)
                } else
                    this.findNavController().navigate(R.id.action_loginFragment_to_completeProfileFragment)
            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                showErrorAlert(requireActivity(),t.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

}