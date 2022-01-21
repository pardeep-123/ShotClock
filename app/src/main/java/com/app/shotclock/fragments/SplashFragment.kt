package com.app.shotclock.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.app.shotclock.R
import com.app.shotclock.base.BaseFragment

class SplashFragment : BaseFragment() {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_splash
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launchWhenResumed {

            }
        },2000)
    }

//    Handler(Looper.getMainLooper()).postDelayed({
//        lifecycleScope.launchWhenResumed {
//            view.findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
//        }
//    },3000)


}