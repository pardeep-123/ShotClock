package com.app.shotclock.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.app.shotclock.cache.getUser
import com.app.shotclock.databinding.FragmentMyRequestsBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.AllRequestResponseModel
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.models.CancelRequestAdminRequest
import com.app.shotclock.models.RequestListResponseModel
import com.app.shotclock.models.sockets.VideoCallResponse
import com.app.shotclock.utils.App
import com.app.shotclock.utils.SocketManager
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.videocallingactivity.VideoCallActivity
import com.app.shotclock.viewmodels.HomeViewModel
import com.google.android.material.button.MaterialButton
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class MyRequestsFragment : BaseFragment<FragmentMyRequestsBinding>(), Observer<Resource<RequestListResponseModel>>, SocketManager.Observer {

    private lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var requestAdapter: MyRequestsAdapter? = null
    private var allRequestAdapter: AllRequestAdapter? = null
    private var requestList = ArrayList<RequestListResponseModel.RequestListResponseBody>()
    private var groupName = ""
    private var status = 0
    private var allRequestList = ArrayList<ArrayList<AllRequestResponseModel.AllRequestBody>>()
    private var activityScope = CoroutineScope(Dispatchers.Main)
    private lateinit var socketManager: SocketManager
    private var mChannelName = ""

    override fun getViewBinding(): FragmentMyRequestsBinding {
        return FragmentMyRequestsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        socketManager = App.mInstance.getSocketManager()!!
        if (!socketManager.isConnected() || socketManager.getmSocket() == null)
            socketManager.init()

        configureViewModel()
        handleClicks()
        setAdapter()
//        handleHomeFragmentBackPress()

    }

    private fun setAdapter() {
        requestAdapter = MyRequestsAdapter(requireContext(), requestList)
        binding.rvMyRequests.adapter = requestAdapter

        requestAdapter?.onItemClickListener = { pos ->
            this.findNavController().navigate(R.id.action_myRequestsFragment_to_speedDateSessionFragment)
        }
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
            binding.tvCancel.isVisible()
            binding.tvCurrent.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_round_corners)
            binding.tvCurrent.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.tvPast.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_grey_round_corners)
            binding.tvPast.setTextColor(ContextCompat.getColor(requireContext(), R.color.hintcolor))
            // api hit
            homeViewModel.requestList().observe(viewLifecycleOwner, this)
        }

        binding.tvPast.setOnClickListener {
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

        binding.tvStart.setOnClickListener {
            callToUser()
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

    // request list api for current
    override fun onChanged(t: Resource<RequestListResponseModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                requestList.clear()
                allRequestList.clear()
                for (i in 0 until t.data?.body!!.size) {
                    groupName = t.data.body[i].groupName
                    status = t.data.body[i].status
                }
                if (t.data.body.size > 0) {
                    binding.tvNoDataFound.isGone()
                    binding.rvAllRequests.isGone()
                    binding.tvCancel.isVisible()
                    binding.clSpeedDate.isVisible()
                    binding.rvMyRequests.isVisible()
                    requestList.addAll(t.data.body)

                    // for video calling start
                    if (t.data.body[0].requestCount == 0) {
                        binding.tvStart.isGone()
                    } else {
                        binding.tvStart.isVisible()

                    }

                } else {
                    binding.clSpeedDate.isGone()
                    binding.tvNoDataFound.isVisible()
                }

                requestAdapter?.notifyDataSetChanged()
                allRequestAdapter?.notifyDataSetChanged()
            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                if (t.message!="Invalid Authorization Key" || t.message!= "Invalid authorization key")
                showError(t.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

    // all request api for past
    private val allRequestListObserver = Observer<Resource<AllRequestResponseModel>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                requestList.clear()
                allRequestList.clear()
                if (it.data?.body!!.isNotEmpty()) {
                    binding.tvNoDataFound.isGone()
                    binding.rvMyRequests.isGone()
                    binding.clSpeedDate.isVisible()
                    binding.rvAllRequests.isVisible()
                    allRequestList.addAll(it.data.body)
                    val body = it.data.body
                    allRequestAdapter = AllRequestAdapter(requireContext(), body)
                    binding.rvAllRequests.adapter = allRequestAdapter
                } else {
                    binding.clSpeedDate.isGone()
                    binding.tvNoDataFound.isVisible()
                }
                requestAdapter?.notifyDataSetChanged()
                allRequestAdapter?.notifyDataSetChanged()

            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                if (it.message!="Invalid Authorization Key" || it.message!= "Invalid authorization key")
                showError(it.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

    private fun setCancelRequestData(groupName:String,status:Int) {
        val data = CancelRequestAdminRequest()
        data.groupName = groupName
        data.status = status
        homeViewModel.cancelRequestAdmin(data).observe(viewLifecycleOwner, cancelRequestAdminObserver)
    }

    // cancel request admin
    private val cancelRequestAdminObserver = Observer<Resource<BaseResponseModel>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
               if (requestList.size > 0) {
                   this.findNavController().navigate(R.id.action_myRequestsFragment_to_homeFragment)

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

    override fun onResume() {
        super.onResume()
        socketManager.unRegister(this)
        socketManager.onRegister(this)
        if (!socketManager.isConnected() || socketManager.getmSocket() == null) {
            socketManager.init()
        }

    }

    // video calling socket
    private fun callToUser() {
        val jsonObject = JSONObject()
        jsonObject.put("senderName", getUser(requireContext())?.username)
        jsonObject.put("senderImage",getUser(requireContext())?.profileImage)
        jsonObject.put("senderId",getUser(requireContext())?.id)
        jsonObject.put("receiverId", requestList[0].requestTo)
        jsonObject.put("requestId", requestList[0].id)
        jsonObject.put("receiverName", requestList[0].username)
        jsonObject.put("receiverImage", requestList[0].profileImage)
        jsonObject.put("callType", "1") // callType(0=>for single call,1=>for group call)
        jsonObject.put("groupName", requestList[0].groupName)
        socketManager.callToUser(jsonObject)

        Log.e("========List",jsonObject.toString())

    }

    override fun onResponseArray(event: String, args: JSONArray) {

    }

    override fun onResponse(event: String, args: JSONObject) {
        when (event) {
            SocketManager.call_to_user_emitter -> {
                try {
                    activityScope.launch {
                        val mObject = args as JSONObject
                        val gson = GsonBuilder().create()

                        val userToCallList = gson.fromJson(mObject.toString(), VideoCallResponse::class.java)

//                        val bundle = Bundle()
//                        bundle.putString("channel_name", userToCallList.channelName)
//                        bundle.putString("video_token", userToCallList.videoToken)
//                        findNavController().navigate(R.id.action_myRequestsFragment_to_videoCallFragment, bundle)

                        val intent = Intent(requireContext(), VideoCallActivity::class.java)
                        intent.putExtra("channel_name", userToCallList.channelName)
                        intent.putExtra("video_token", userToCallList.videoToken)
                        startActivity(intent)

                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    override fun onError(event: String, vararg args: Array<*>) {

    }

}