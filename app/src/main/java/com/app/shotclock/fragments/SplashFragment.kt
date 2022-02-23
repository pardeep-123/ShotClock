package com.app.shotclock.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.cache.getUser
import com.app.shotclock.databinding.FragmentSplashBinding

class SplashFragment : BaseFragment<FragmentSplashBinding>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launchWhenResumed {
                if (getUser(requireContext())?.authKey.isNullOrEmpty())
                view.findNavController().navigate(R.id.action_splashFragment_to_walkThroughFragment)
                else
                    view.findNavController().navigate(R.id.action_splashFragment_to_homeActivity)
            }
        }, 3000)
    }

    override fun getViewBinding(): FragmentSplashBinding {
     return FragmentSplashBinding.inflate(layoutInflater)
    }


}