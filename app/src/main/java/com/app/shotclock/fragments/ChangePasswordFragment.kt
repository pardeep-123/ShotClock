package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentChangePasswordBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.models.ChangePassRequestModel
import com.app.shotclock.utils.Validation
import com.app.shotclock.utils.hideKeyboard
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.viewmodels.LoginSignUpViewModel
import javax.inject.Inject

class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>(),
    Observer<Resource<BaseResponseModel>> {

    lateinit var loginSignUpViewModel: LoginSignUpViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getViewBinding(): FragmentChangePasswordBinding {
        return FragmentChangePasswordBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleClicks()
        configureViewModel()

    }

    private fun configureViewModel() {
        loginSignUpViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginSignUpViewModel::class.java)
    }

    private fun handleClicks() {
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivMenu.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
            hideKeyboard(it, requireActivity())
        }

        binding.btUpdate.setOnClickListener {
            if (Validation().changePassValidation(requireActivity(),
                    binding.etOldPassword.text.toString().trim(),
                    binding.etNewPassword.text.toString().trim(),
                    binding.etConfirmPassword.text.toString().trim()
                )
            ) {
                changePasswordData()
            }
        }
    }

    private fun changePasswordData() {
        val data = ChangePassRequestModel()
        data.old_password = binding.etOldPassword.text.toString().trim()
        data.new_password = binding.etNewPassword.text.toString().trim()
        loginSignUpViewModel.changePassword(data).observe(viewLifecycleOwner, this)
    }

    override fun onChanged(t: Resource<BaseResponseModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                activity?.onBackPressed()
            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                showError(t.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()

            }
        }
    }

}