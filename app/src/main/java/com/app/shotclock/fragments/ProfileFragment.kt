package com.app.shotclock.fragments

import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override fun getViewBinding(): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }

}