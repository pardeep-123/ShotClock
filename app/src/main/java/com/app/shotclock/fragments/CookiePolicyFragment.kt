package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentCookiePolicyBinding

class CookiePolicyFragment : BaseFragment<FragmentCookiePolicyBinding>() {

    override fun getViewBinding(): FragmentCookiePolicyBinding {
        return FragmentCookiePolicyBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}