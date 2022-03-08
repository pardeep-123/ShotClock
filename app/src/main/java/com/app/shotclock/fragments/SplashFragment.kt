package com.app.shotclock.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.cache.getUser
import com.app.shotclock.databinding.FragmentSplashBinding
import com.app.shotclock.utils.Prefs
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class SplashFragment : BaseFragment<FragmentSplashBinding>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        "gg",
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }
                val token = task.result
                Log.d("token",token)
                Prefs.with(view.context).save("token",token.toString())
            })
        } catch (e: Exception) {
            e.printStackTrace()

        }

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