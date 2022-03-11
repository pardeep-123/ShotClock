package com.app.shotclock.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.ProfilePagerAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentProfileBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.ProfileBody
import com.app.shotclock.models.ProfileUserImage
import com.app.shotclock.models.ProfileViewModel
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.viewmodels.ProfileViewModels
import javax.inject.Inject


class ProfileFragment : BaseFragment<FragmentProfileBinding>(),
    Observer<Resource<ProfileViewModel>> {

    private lateinit var profileViewModels: ProfileViewModels

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var imageList = ArrayList<ProfileUserImage>()
    private var gender = 0
    private var interested = 0
    private var profileData: ProfileBody? = null
    private val oneFeet = 0.0328  //Don't change this value

    override fun getViewBinding(): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewModel()
        clickHandle()

    }

    private fun configureViewModel() {
        profileViewModels =
            ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModels::class.java)
        profileViewModels.userProfile().observe(viewLifecycleOwner, this)
    }

    private fun clickHandle() {
        binding.tb.ivMenu.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
        }

        binding.btEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("data",profileData)
            this.findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment,bundle)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onChanged(t: Resource<ProfileViewModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                val body = t.data?.body!!
                profileData = body
                imageList.clear()
                if (t.data.body.user_images.size > 0) {
                    imageList = t.data.body.user_images
                }

                binding.tvHeight.text = body.height
                /*     if (body.height.isNotEmpty()) {
                         val feetHeight = (oneFeet * body.height.toDouble()).toFloat()
                         binding.tvHeight.text =  String.format("%.1f", feetHeight)+ "'"
                     }*/

                gender = body.gender
                interested = body.interested
                binding.tvUserName.text = body.username
                binding.tvDetails.text = body.bio
                binding.tvMail.text = body.email
                binding.tvPhoneNo.text = body.countryCode + "" + body.phone
                binding.tvDOB.text = body.dateofbirth
                binding.tvGraduation.text = body.qualification
                binding.tvLocation.text = body.address
                binding.tvSexualOrientation.text = body.sexualOrientation
                binding.tvAstrologicalSign.text = body.astrologicalSign
                binding.tvSmoking.text = body.smoking
                binding.tvDrinking.text = body.drinking
                binding.tvPets.text = body.pets

                // pager adapter
                binding.viewPager.adapter = ProfilePagerAdapter(requireContext(), imageList)
                binding.circleIndicator.setViewPager(binding.viewPager)

                binding.tvGender.text = body.gender.toString()
                if (gender == 1)
                    binding.tvGender.text = "Male"
                else
                    binding.tvGender.text = "Female"

                binding.tvInterestedIn.text = body.interested.toString()
                when (interested) {
                    1 -> binding.tvInterestedIn.text = "Men"
                    2 -> binding.tvInterestedIn.text = "Women"
                    else -> binding.tvInterestedIn.text = "Others"
                }
            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                if (t.message != "Invalid Authorization Key" || t.message != "Invalid authorization key")
                    showError(t.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

}