package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentPrivcayPolicyBinding

class PrivacyPolicyFragment : BaseFragment<FragmentPrivcayPolicyBinding>() {

    override fun getViewBinding(): FragmentPrivcayPolicyBinding {
        return FragmentPrivcayPolicyBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}