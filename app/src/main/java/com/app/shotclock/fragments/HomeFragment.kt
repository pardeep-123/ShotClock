package com.app.shotclock.fragments

import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }


}