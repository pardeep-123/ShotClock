package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun getViewBinding(): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater)
    }

}