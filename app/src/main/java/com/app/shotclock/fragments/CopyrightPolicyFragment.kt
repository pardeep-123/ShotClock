package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentCopyrightPolicyBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.CopyRightNoticeResponse
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.viewmodels.LoginSignUpViewModel
import javax.inject.Inject


class CopyrightPolicyFragment : BaseFragment<FragmentCopyrightPolicyBinding>(),Observer<Resource<CopyRightNoticeResponse>> {

    lateinit var loginSignUpViewModel: LoginSignUpViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getViewBinding(): FragmentCopyrightPolicyBinding {
        return FragmentCopyrightPolicyBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewModel()
        clickHandle()
    }

    private fun configureViewModel() {
        loginSignUpViewModel = ViewModelProviders.of(this,viewModelFactory).get(LoginSignUpViewModel::class.java)
        loginSignUpViewModel.copyRightNotice().observe(viewLifecycleOwner,this)
    }

    private fun clickHandle() {
        binding.tb.ivMenu.isVisible()
        binding.tb.ivAppLogo2.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
        }
    }

    override fun onChanged(t: Resource<CopyRightNoticeResponse>) {
        when(t.status){
            Status.SUCCESS->{
                binding.pb.clLoading.isGone()
                binding.tvCopyrightDescription.text = HtmlCompat.fromHtml(t.data?.body?.copyrightsnotices!!,HtmlCompat.FROM_HTML_MODE_COMPACT)
            }
            Status.ERROR->{
                binding.pb.clLoading.isGone()
                showError(t.message!!)
            }
            Status.LOADING->{
                binding.pb.clLoading.isVisible()
            }
        }
    }

}