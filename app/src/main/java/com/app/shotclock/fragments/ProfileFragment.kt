package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.ProfilePagerAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentProfileBinding
import com.app.shotclock.models.ProfileImagesModel
import com.app.shotclock.utils.isVisible

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private var imageList = ArrayList<ProfileImagesModel>()
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

        imageList.add(ProfileImagesModel(R.drawable.img_two))
        imageList.add(ProfileImagesModel(R.drawable.img_two))
        imageList.add(ProfileImagesModel(R.drawable.img_two))
        binding.viewPager.adapter = ProfilePagerAdapter(requireContext(), imageList)
        binding.circleIndicator.setViewPager(binding.viewPager)
//        binding.viewPagerIndicator.setViewPager(binding.viewPager)

    }
}