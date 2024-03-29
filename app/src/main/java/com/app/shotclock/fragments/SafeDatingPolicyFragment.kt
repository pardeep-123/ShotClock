package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.constants.CacheConstants
import com.app.shotclock.databinding.FragmentSafeDatingPolicyBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.SafeDatingPolicyResponse
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.utils.showErrorAlert
import com.app.shotclock.viewmodels.LoginSignUpViewModel
import javax.inject.Inject


class SafeDatingPolicyFragment : BaseFragment<FragmentSafeDatingPolicyBinding>(),
    Observer<Resource<SafeDatingPolicyResponse>> {

    lateinit var loginSignUpViewModel: LoginSignUpViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getViewBinding(): FragmentSafeDatingPolicyBinding {
        return FragmentSafeDatingPolicyBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CacheConstants.Current = "safeDating"
        configureViewModel()
        clickHandle()

    }

    private fun configureViewModel() {
        loginSignUpViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginSignUpViewModel::class.java)
        loginSignUpViewModel.safeDatingPolicy().observe(viewLifecycleOwner, this)
    }

    private fun clickHandle() {
        binding.tb.ivMenu.isVisible()
        binding.tb.ivAppLogo2.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
        }
    }

    override fun onChanged(t: Resource<SafeDatingPolicyResponse>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                binding.tvSafeDatingDescription.text = HtmlCompat.fromHtml(
                    t.data?.body?.safedatingpolicy!!,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
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