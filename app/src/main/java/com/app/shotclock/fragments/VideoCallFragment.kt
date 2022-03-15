package com.app.shotclock.fragments

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentVideoCallBinding
import com.app.shotclock.models.sockets.VideoCallResponse
import com.app.shotclock.utils.App
import com.app.shotclock.utils.SocketManager
import com.app.shotclock.utils.isVisible
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class VideoCallFragment : BaseFragment<FragmentVideoCallBinding>(), SocketManager.Observer {

    private var videoList = ArrayList<VideoCallResponse>()
    private var activityScope = CoroutineScope(Dispatchers.Main)
    private lateinit var socketManager: SocketManager
    private var channelName = ""
    private var videoToken = ""
    private var status =
        ""    // 0=calling, 1=callConnected, 2=call Declined, 3=Call Disconnected, 4=Missed call
    private var isCallEnd =
        "" // 0=callEnded by receiver or auto cut or skip by admin, 1=call cut by admin
    private var duration = ""

    override fun getViewBinding(): FragmentVideoCallBinding {
        return FragmentVideoCallBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        socketManager = App.mInstance.getSocketManager()!!
        if (!socketManager.isConnected() || socketManager.getmSocket() == null)
            socketManager.init()

        val bundle = arguments
        channelName = bundle?.getString("channel_name")!!
        videoToken = bundle.getString("video_token")!!

        handleClicks()
        videoTimingDialog()

    }

    private fun handleClicks() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()

        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.tvIcebreaker.setOnClickListener {
            this.findNavController().navigate(R.id.action_videoCallFragment_to_icebreakerQuestionsFragment)
        }

        binding.tvCancel.setOnClickListener {
            val dialog = Dialog(requireContext())
            with(dialog) {
                setCancelable(false)
                setContentView(R.layout.alert_dialog_cancel_call)

                val btOk: MaterialButton = findViewById(R.id.btYes)
                val btNo: MaterialButton = findViewById(R.id.btNo)

                btOk.setOnClickListener {
                    dismiss()
                }
                btNo.setOnClickListener {
                    dismiss()
                }
                show()
            }
        }

        binding.tvSkip.setOnClickListener {
            isCallEnd = "0"
        }


    }

    private fun getVideoCallData() {

    }

    private fun receiveToCall() {

    }

    private fun getCallStatus() {
        val jsonObject = JSONObject()
        jsonObject.put("status", status)
        jsonObject.put("channelName", channelName)
        jsonObject.put("isCallEnd", isCallEnd)
        jsonObject.put("duration", duration)
        socketManager.getCallStatus(jsonObject)

    }

    override fun onResume() {
        super.onResume()
        socketManager.unRegister(this)
        socketManager.onRegister(this)
        if (!socketManager.isConnected() || socketManager.getmSocket() == null)
            socketManager.init()

        getCallStatus()

    }

    override fun onResponseArray(event: String, args: JSONArray) {

    }

    override fun onResponse(event: String, args: JSONObject) {

    }

    override fun onError(event: String, vararg args: Array<*>) {

    }

    private fun videoTimingDialog() {
        val count = 0
        val dialog = Dialog(requireContext(),android.R.style.Theme_Translucent_NoTitleBar)
        with(dialog) {
            setCancelable(false)
            setContentView(R.layout.items_video_calling_timer)
            val tvTimer: TextView = findViewById(R.id.tvTimer)

//            =new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen)Dialog dialog

            val timer = object : CountDownTimer(5000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    tvTimer.text = String.format("%d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished))

                }

                override fun onFinish() {
                    dismiss()
                }
            }
            timer.start()

//                if (tvTimer.text.length >5)
//                tvTimer.text = count.toString()
            show()
        }
    }

}