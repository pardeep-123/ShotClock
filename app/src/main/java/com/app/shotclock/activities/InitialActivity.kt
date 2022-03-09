package com.app.shotclock.activities

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseActivity
import com.app.shotclock.databinding.ActivityInitialBinding

class InitialActivity : BaseActivity() {

    private lateinit var binding : ActivityInitialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("expired")){
            findNavController(R.id.fragment_host).navigate(R.id.action_splashFragment_to_loginFragment)
            Toast.makeText(this, "Session Expired", Toast.LENGTH_LONG).show()
        }else if (intent.hasExtra("logout")){
            findNavController(R.id.fragment_host).navigate(R.id.action_splashFragment_to_walkThroughFragment)
        }

    }
}
