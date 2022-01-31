package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentSafeDatingPolicyBinding


class SafeDatingPolicyFragment : BaseFragment<FragmentSafeDatingPolicyBinding>() {

    override fun getViewBinding(): FragmentSafeDatingPolicyBinding {
        return FragmentSafeDatingPolicyBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}