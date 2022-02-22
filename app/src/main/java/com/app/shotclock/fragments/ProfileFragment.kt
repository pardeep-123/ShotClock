package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.ProfilePagerAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.cache.getUser
import com.app.shotclock.databinding.FragmentProfileBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.ProfileImagesModel
import com.app.shotclock.models.ProfileViewModel
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.viewmodels.LoginSignUpViewModel
import com.bumptech.glide.Glide
import javax.inject.Inject

class ProfileFragment : BaseFragment<FragmentProfileBinding>(),
    Observer<Resource<ProfileViewModel>> {

    lateinit var loginSignUpViewModel: LoginSignUpViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var imageList = ArrayList<ProfileImagesModel>()

    override fun getViewBinding(): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewModel()
        clickHandle()

    }

    private fun configureViewModel() {
        loginSignUpViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(LoginSignUpViewModel::class.java)
        loginSignUpViewModel.userProfile().observe(viewLifecycleOwner, this)
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

    }

    override fun onChanged(t: Resource<ProfileViewModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                val body = t.data?.body!!
                binding.tvUserName.text = body.username
                binding.tvDetails.text = body.bio
                binding.tvMail.text = body.email
                binding.tvPhoneNo.text = body.countryCode+""+body.phone
                binding.tvDOB.text = body.dateofbirth
                binding.tvGender.text = body.gender.toString()
                binding.tvHeight.text = body.height
                binding.tvGraduation.text = body.qualification
                binding.tvLocation.text = body.address
                binding.tvInterestedIn.text = body.interested.toString()
                binding.tvSexualOrientation.text = body.sexualOrientation
                binding.tvAstrologicalSign.text = body.astrologicalSign
                binding.tvSmoking.text = body.smoking
                binding.tvDrinking.text = body.drinking
                binding.tvPets.text = body.pets

            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                showError(t.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }


}