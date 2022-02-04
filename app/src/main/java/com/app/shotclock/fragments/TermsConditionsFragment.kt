package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentTermsConditionsBinding
import com.app.shotclock.utils.isVisible


class TermsConditionsFragment : BaseFragment<FragmentTermsConditionsBinding>() {

    override fun getViewBinding(): FragmentTermsConditionsBinding {
        return FragmentTermsConditionsBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()

        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

    }
}