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
import com.app.shotclock.models.sockets.VideoCallResponse
import com.app.shotclock.utils.*
import com.app.shotclock.viewmodels.HomeViewModel
import com.google.android.material.button.MaterialButton
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class SpeedDateSessionFragment : BaseFragment<FragmentSpeedDateSessionBinding>(),
    Observer<Resource<RequestListResponseModel>>, SocketManager.Observer {

    private lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var speedAdapter: SpeedDateSessionAdapter? = null
    private var speedList = ArrayList<RequestListResponseModel.RequestListResponseBody>()
    private var groupName = ""
    private var status = 0
    private var activityScope = CoroutineScope(Dispatchers.Main)
    private lateinit var socketManager: SocketManager

    override fun getViewBinding(): FragmentSpeedDateSessionBinding {
        return FragmentSpeedDateSessionBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        socketManager = App.mInstance.getSocketManager()!!
        if (!socketManager.isConnected() || socketManager.getmSocket() == null)
            socketManager.init()

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
                    setCancelRequestData(groupName, status)
                    dismiss()
                }

                btNo.setOnClickListener {
                    dismiss()
                }
                show()
            }
        }

        binding.tvStart.setOnClickListener {
            if (speedList[0].requestCount == 0) {
//                You can't this calling because no user has accept your request yet
                showToast("You can start your speed date once your matches accept your request. Hang tight!")
            } else {
                callToUser()
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

    override fun onResume() {
        super.onResume()

        socketManager.unRegister(this)
        socketManager.onRegister(this)
        if (!socketManager.isConnected() || socketManager.getmSocket() == null) {
            socketManager.init()
        }
    }

    private fun callToUser() {
        val jsonObject = JSONObject()
        jsonObject.put("senderName", speedList[0].username)
        jsonObject.put("senderImage", speedList[0].profileImage)
        jsonObject.put("senderId", speedList[0].id)
        jsonObject.put("receiverId", speedList[0].id)
        jsonObject.put("requestId", speedList[0].requestTo)
        jsonObject.put("receiverName", speedList[0].username)
        jsonObject.put("callType", "1")
        jsonObject.put("groupName", speedList[0].groupName)
        socketManager.callToUser(jsonObject)

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

                        val userToCallList =
                            gson.fromJson(mObject.toString(), VideoCallResponse::class.java)
                        val bundle = Bundle()
                        bundle.putString("channel_name", userToCallList.channelName)
                        bundle.putString("video_token", userToCallList.videoToken)
                        findNavController().navigate(
                            R.id.action_speedDateSessionFragment_to_videoCallFragment,
                            bundle
                        )
                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    override fun onError(event: String, vararg args: Array<*>) {

    }

}