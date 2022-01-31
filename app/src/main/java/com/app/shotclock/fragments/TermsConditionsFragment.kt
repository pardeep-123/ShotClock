package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentTermsConditionsBinding


class TermsConditionsFragment : BaseFragment<FragmentTermsConditionsBinding>() {

    override fun getViewBinding(): FragmentTermsConditionsBinding {
        return FragmentTermsConditionsBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}