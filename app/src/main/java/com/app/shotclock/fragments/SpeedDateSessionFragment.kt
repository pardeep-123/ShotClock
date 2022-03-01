package com.app.shotclock.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.adapters.SpeedDateSessionAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentSpeedDateSessionBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.models.CancelRequestAdminRequest
import com.app.shotclock.models.RequestListResponseModel
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.viewmodels.HomeViewModel
import com.google.android.material.button.MaterialButton
import javax.inject.Inject


class SpeedDateSessionFragment : BaseFragment<FragmentSpeedDateSessionBinding>(), Observer<Resource<RequestListResponseModel>> {

    private lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var speedAdapter: SpeedDateSessionAdapter? = null
    private var speedList = ArrayList<RequestListResponseModel.RequestListResponseBody>()
    private var groupName = ""
    private var status = 0

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

        binding.tvCancel.setOnClickListener {
            val dialog = Dialog(requireContext())
            with(dialog) {
                setCancelable(false)
                setContentView(R.layout.alert_dialog_cancel_requests)
                val btYes: MaterialButton = findViewById(R.id.btYes)
                val btNo: MaterialButton = findViewById(R.id.btNo)

                btYes.setOnClickListener {
                    setCancelRequestData(groupName,status)
                    dismiss()
                }

                btNo.setOnClickListener {
                    dismiss()
                }
                show()
            }
        }
    }

    // request list api
    override fun onChanged(t: Resource<RequestListResponseModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                for (i in 0 until t.data?.body!!.size){
                    groupName = t.data.body[i].groupName
                    status= t.data.body[i].status
                }
                speedList.addAll(t.data.body)
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

    private fun setCancelRequestData(groupName:String,status:Int) {
        val data = CancelRequestAdminRequest()
        data.groupName = groupName
        data.status= status
        homeViewModel.cancelRequestAdmin(data).observe(viewLifecycleOwner, cancelRequestAdminObserver)
    }

    // cancel request admin
    private val cancelRequestAdminObserver = Observer<Resource<BaseResponseModel>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                speedAdapter?.notifyDataSetChanged()
               findNavController().navigate(R.id.action_speedDateSessionFragment_to_homeFragment)
//                requestList.clear()
//                allRequestList.clear()

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