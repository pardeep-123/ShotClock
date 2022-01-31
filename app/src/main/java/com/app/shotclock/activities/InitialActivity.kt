package com.app.shotclock.activities

import android.os.Bundle
import com.app.shotclock.base.BaseActivity
import com.app.shotclock.databinding.ActivityInitialBinding

class InitialActivity : BaseActivity() {

    private lateinit var binding : ActivityInitialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitialBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}