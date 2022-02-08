package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentProfileBinding
import com.app.shotclock.utils.isVisible

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override fun getViewBinding(): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickHandle()


    }

    private fun clickHandle() {
        binding.tb.ivMenu.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
        }

        binding.btEdit.setOnClickListener {
            this.findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

    }
}