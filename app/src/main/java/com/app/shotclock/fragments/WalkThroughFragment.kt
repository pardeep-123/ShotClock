package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.app.shotclock.R
import com.app.shotclock.adapters.WalkThroughAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentWalkThroughBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.models.WalkThroughModel
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.viewmodels.LoginSignUpViewModel
import javax.inject.Inject


class WalkThroughFragment : BaseFragment<FragmentWalkThroughBinding>(),
    Observer<Resource<BaseResponseModel>> {

    lateinit var loginSignUpViewModel: LoginSignUpViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var walkList: ArrayList<WalkThroughModel>? = ArrayList()

    override fun getViewBinding(): FragmentWalkThroughBinding {
        return FragmentWalkThroughBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        configureViewModel()
        handleClicks()

    }

    private fun configureViewModel() {
        loginSignUpViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(LoginSignUpViewModel::class.java)
    }

    private fun handleClicks() {
        /*       binding.ivPhone.setOnClickListener {
           this.findNavController().navigate(R.id.action_walkThroughFragment_to_loginPhoneFragment)
       }*/

        binding.ivEmail.setOnClickListener {
            this.findNavController().navigate(R.id.action_walkThroughFragment_to_loginFragment)
        }

    }

    private fun setAdapter() {
        walkList?.clear()
        walkList?.add(WalkThroughModel(R.drawable.img_four))
        walkList?.add(WalkThroughModel(R.drawable.img_three))
        walkList?.add(WalkThroughModel(R.drawable.img_two))

        binding.rvWalkThrough.adapter = WalkThroughAdapter(walkList!!)

        binding.rvWalkThrough.set3DItem(false)
        binding.rvWalkThrough.setInfinite(false)
        binding.rvWalkThrough.setAlpha(false)
        binding.rvWalkThrough.setFlat(false)
        binding.rvWalkThrough.setIntervalRatio(0.6F)
//        binding.rvWalkThrough.scrollToPosition(selectedPos!!)
//        val carouselLayoutManager = binding.rvWalkThrough.getCarouselLayoutManager()
//        val currentlyCenterPosition = binding.rvWalkThrough.getSelectedPosition()

        val pagerSnapHelper = PagerSnapHelper()

        pagerSnapHelper.attachToRecyclerView(binding.rvWalkThrough)
        binding.viewPagerIndicator.attachToRecyclerView(binding.rvWalkThrough, pagerSnapHelper)

    }

    private fun setSocialLoginData() {
//        val body = SocialLoginRequestModel()

//        loginSignUpViewModel.socialLogin(body).observe(viewLifecycleOwner,this)
    }

    override fun onChanged(t: Resource<BaseResponseModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()

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