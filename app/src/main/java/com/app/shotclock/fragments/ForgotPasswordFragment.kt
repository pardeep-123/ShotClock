package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentForgotPasswordBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.models.ForgotPasswordRequest
import com.app.shotclock.utils.Validation
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.utils.showErrorAlert
import com.app.shotclock.viewmodels.LoginSignUpViewModel
import javax.inject.Inject

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>(),
    Observer<Resource<BaseResponseModel>> {

    lateinit var loginSignUpViewModel: LoginSignUpViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val type = ""

    override fun getViewBinding(): FragmentForgotPasswordBinding {
        return FragmentForgotPasswordBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleClicks()
        configureViewModel()

    }

    private fun handleClicks() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btSubmitForgot.setOnClickListener {
            if (Validation().verifyForgotPassword(
                    requireActivity(),
                    binding.etEmailForgot.text.toString().trim()
                )
            ) {
                forgotPassword()
            }
        }
    }

    private fun configureViewModel() {
        loginSignUpViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(LoginSignUpViewModel::class.java)
    }

    private fun forgotPassword() {
        val data = ForgotPasswordRequest(binding.etEmailForgot.text.toString().trim())
        loginSignUpViewModel.forgotPassword(data).observe(viewLifecycleOwner, this)
    }

    override fun onChanged(t: Resource<BaseResponseModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                activity?.onBackPressed()
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