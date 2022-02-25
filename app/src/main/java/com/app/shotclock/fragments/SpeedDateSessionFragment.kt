package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.shotclock.adapters.SpeedDateSessionAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentSpeedDateSessionBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.RequestListResponseModel
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.viewmodels.HomeViewModel
import javax.inject.Inject


class SpeedDateSessionFragment : BaseFragment<FragmentSpeedDateSessionBinding>(), Observer<Resource<RequestListResponseModel>> {

    private lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var speedAdapter: SpeedDateSessionAdapter? = null
    private var speedList = ArrayList<RequestListResponseModel.RequestListResponseBody>()

    override fun getViewBinding(): FragmentSpeedDateSessionBinding {
        return FragmentSpeedDateSessionBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViewModel()
        handleClicks()
        setAdapter()

    }

    private fun setAdapter() {
        speedAdapter = SpeedDateSessionAdapter(requireContext(), speedList)
        binding.rvSpeedDate.adapter = speedAdapter
    }

    private fun configureViewModel() {
        homeViewModel = ViewModelProviders.of(this,viewModelFactory).get(HomeViewModel::class.java)
        homeViewModel.requestList().observe(viewLifecycleOwner,this)
    }

    private fun handleClicks() {
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivBack.isVisible()
        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onChanged(t: Resource<RequestListResponseModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                speedList.addAll(t.data?.body!!)
                speedAdapter?.notifyDataSetChanged()
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