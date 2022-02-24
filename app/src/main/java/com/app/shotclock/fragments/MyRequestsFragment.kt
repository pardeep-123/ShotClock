package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.MyRequestsAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentMyRequestsBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.RequestListResponseModel
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.viewmodels.HomeViewModel
import javax.inject.Inject

class MyRequestsFragment : BaseFragment<FragmentMyRequestsBinding>(),Observer<Resource<RequestListResponseModel>> {

    lateinit var homeViewModel : HomeViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var requestAdapter : MyRequestsAdapter? =null
    private var requestList = ArrayList<RequestListResponseModel.RequestListResponseBody>()

    override fun getViewBinding(): FragmentMyRequestsBinding {
        return FragmentMyRequestsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewModel()
        handleClicks()
//        handleHomeFragmentBackPress()

        requestAdapter = MyRequestsAdapter(requireContext(),requestList)
        binding.rvMyRequests.adapter = requestAdapter

    }

    private fun configureViewModel() {
        homeViewModel = ViewModelProviders.of(this,viewModelFactory).get(HomeViewModel::class.java)
        homeViewModel.requestList().observe(viewLifecycleOwner,this)
    }

    private fun handleClicks() {
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivMenu.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
        }

        binding.tvCurrent.setOnClickListener {
            binding.tvNoDataFound.isGone()
             binding.clSpeedDate.isVisible()

        }
        binding.tvPast.setOnClickListener {
            binding.clSpeedDate.isGone()
            binding.tvNoDataFound.isVisible()
        }
    }

    //        for back press in fragment
    private fun handleHomeFragmentBackPress() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //Handle back event from any fragment
                    activity?.onBackPressed()
                }
            })
    }

    // request list api
    override fun onChanged(t: Resource<RequestListResponseModel>) {
        when(t.status){
            Status.SUCCESS->{
            binding.pb.clLoading.isGone()
                requestList.addAll(t.data?.body!!)
            }
            Status.ERROR->{
                binding.pb.clLoading.isGone()
                showError(t.message!!)
            }
            Status.LOADING->{
                binding.pb.clLoading.isVisible()
            }
        }
    }
}