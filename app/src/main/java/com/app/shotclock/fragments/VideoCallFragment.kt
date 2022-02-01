package com.app.shotclock.fragments

import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentVideoCallBinding


class VideoCallFragment : BaseFragment<FragmentVideoCallBinding>() {

    override fun getViewBinding(): FragmentVideoCallBinding {
        return FragmentVideoCallBinding.inflate(layoutInflater)
    }

}