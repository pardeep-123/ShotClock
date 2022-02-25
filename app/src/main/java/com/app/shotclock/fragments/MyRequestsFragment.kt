package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.AllRequestAdapter
import com.app.shotclock.adapters.MyRequestsAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentMyRequestsBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.AllRequestResponseModel
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.models.CancelRequestAdminRequest
import com.app.shotclock.models.RequestListResponseModel
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.viewmodels.HomeViewModel
import javax.inject.Inject

class MyRequestsFragment : BaseFragment<FragmentMyRequestsBinding>(),Observer<Resource<RequestListResponseModel>> {

    private lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var requestAdapter: MyRequestsAdapter? = null
    private var allRequestAdapter: AllRequestAdapter? = null
    private var requestList = ArrayList<RequestListResponseModel.RequestListResponseBody>()
    private var allRequestList = ArrayList<AllRequestResponseModel.AllRequestBody>()
    private var groupName = ""
    private var status = 0

    override fun getViewBinding(): FragmentMyRequestsBinding {
        return FragmentMyRequestsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewModel()
        handleClicks()
        setAdapter()
//        handleHomeFragmentBackPress()



    }

    private fun setAdapter() {

        requestAdapter = MyRequestsAdapter(requireContext(), requestList)
        binding.rvMyRequests.adapter = requestAdapter
        requestAdapter?.onItemClickListener = {pos->
            this.findNavController().navigate(R.id.action_myRequestsFragment_to_speedDateSessionFragment)
        }

        allRequestAdapter = AllRequestAdapter(requireContext(), allRequestList)
        binding.rvAllRequests.adapter = allRequestAdapter

    }

    private fun configureViewModel() {
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        homeViewModel.requestList().observe(viewLifecycleOwner, this)
    }

    private fun handleClicks() {
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivMenu.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
        }

        // myRequest adapter click to open speed date session fragment


        binding.tvCurrent.setOnClickListener {
//            binding.tvNoDataFound.isGone()
//            binding.clSpeedDate.isVisible()
            binding.tvCancel.isVisible()
            binding.tvCurrent.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_round_corners)
            binding.tvCurrent.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.tvPast.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_grey_round_corners)
            binding.tvPast.setTextColor(ContextCompat.getColor(requireContext(), R.color.hintcolor))
            // api hit
            homeViewModel.requestList().observe(viewLifecycleOwner, this)
        }
        binding.tvPast.setOnClickListener {
//            binding.clSpeedDate.isGone()
//            binding.tvNoDataFound.isVisible()
            binding.tvStart.isGone()
            binding.tvCancel.isGone()
            binding.tvPast.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_round_corners)
            binding.tvPast.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.tvCurrent.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_grey_round_corners)
            binding.tvCurrent.setTextColor(ContextCompat.getColor(requireContext(), R.color.hintcolor))
            // api hit
            homeViewModel.allRequestList().observe(viewLifecycleOwner, allRequestListObserver)
        }

        binding.tvCancel.setOnClickListener {

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
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                requestList.clear()
                allRequestList.clear()
                if (t.data?.body!!.size > 0) {
                    requestList.addAll(t.data.body)
                    if (t.data.body[0].requestCount == 0)
                        binding.tvStart.isGone()
                    else
                        binding.tvStart.isVisible()
                    requestAdapter?.notifyDataSetChanged()
                    allRequestAdapter?.notifyDataSetChanged()
                } else {
                    binding.clSpeedDate.isGone()
                    binding.tvNoDataFound.isVisible()
                }
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

    private val allRequestListObserver = Observer<Resource<AllRequestResponseModel>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                requestList.clear()
                allRequestList.clear()
                if (it.data?.body!!.isNotEmpty()) {
                    allRequestList.addAll(it.data.body[0])
                    requestAdapter?.notifyDataSetChanged()
                    allRequestAdapter?.notifyDataSetChanged()
                } else {
                    binding.clSpeedDate.isGone()
                    binding.tvNoDataFound.isVisible()
                }
            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                showError(it.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

    private fun setCancelRequestData() {
        val data = CancelRequestAdminRequest()
        data.groupName
        data.status
        homeViewModel.cancelRequestAdmin(data)
            .observe(viewLifecycleOwner, cancelRequestAdminObserver)
    }

    // cancel request admin
    private val cancelRequestAdminObserver = Observer<Resource<BaseResponseModel>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                requestList.clear()
                allRequestList.clear()

            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                showError(it.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

}