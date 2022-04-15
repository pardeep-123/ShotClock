package com.app.shotclock.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.adapters.SpeedDateSessionAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.cache.getUser
import com.app.shotclock.constants.CacheConstants
import com.app.shotclock.databinding.FragmentSpeedDateSessionBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.models.CancelRequestAdminRequest
import com.app.shotclock.models.RequestListResponseModel
import com.app.shotclock.models.sockets.VideoCallResponse
import com.app.shotclock.utils.*
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
        CacheConstants.Current = "speedDate"
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
            if (speedList!=null){
                if (speedList.isNotEmpty()){
                    if (speedList[0].requestCount == 0) {
                        showErrorAlert(requireActivity(),getString(R.string.you_can_start_your_speed_date_once))
                    } else {
                        callToUser()
                    }
                }
            }

        }
    }

    // request list api
    override fun onChanged(t: Resource<RequestListResponseModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                speedList.addAll(t.data?.body!!)
                for (i in 0 until t.data.body.size){
                    groupName = t.data.body[i].groupName
                    status= t.data.body[i].status
                }
                speedAdapter?.notifyDataSetChanged()
            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                showErrorAlert(requireActivity(),t.message!!)
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
                showErrorAlert(requireActivity(),it.message!!)
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
        for (i in 0 until speedList.size){
            if (speedList[i].status == 2){
                jsonObject.put("receiverId", speedList[i].id)
                jsonObject.put("requestId", speedList[i].requestTo)
                jsonObject.put("receiverName", speedList[i].username)
                jsonObject.put("receiverImage", speedList[i].profileImage)
            }
        }
        jsonObject.put("senderName", getUser(requireContext())?.username)
        jsonObject.put("senderImage", getUser(requireContext())?.profileImage)
        jsonObject.put("senderId", getUser(requireContext())?.id)
        jsonObject.put("callType", "1")// callType(0=>for single call,1=>for group call)
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
                        val userToCallList = gson.fromJson(mObject.toString(), VideoCallResponse::class.java)

                        val intent = Intent(requireContext(), VideoCallActivity::class.java)
                        intent.putExtra("channel_name", userToCallList.channelName)
                        intent.putExtra("video_token", userToCallList.videoToken)
                        intent.putExtra("type","fromRequest")
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