package com.app.shotclock.fragments

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.constants.CacheConstants
import com.app.shotclock.databinding.FragmentVideoCallBinding
import com.app.shotclock.models.sockets.VideoCallResponse
import com.app.shotclock.utils.App
import com.app.shotclock.utils.SocketManager
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class VideoCallFragment : BaseFragment<FragmentVideoCallBinding>(), SocketManager.Observer {

    private var videoList = ArrayList<VideoCallResponse>()
    private var activityScope = CoroutineScope(Dispatchers.Main)
    private lateinit var socketManager: SocketManager
    private var channelName = ""
    private  var agoraToken=""
    private var status = "" // 0=calling, 1=callConnected, 2=call Declined, 3=Call Disconnected, 4=Missed call
    private var isCallEnd = "" // 0=callEnded by receiver or auto cut or skip by admin, 1=call cut by admin
    private var duration = ""

    private var mRtcEngine: RtcEngine? = null
    var builder: AlertDialog.Builder? = null
    private var isReceiver = false
    var requestId=""

    private var mPlayer: MediaPlayer? = null

    private var mCounter : CountDownTimer? = null

    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        /**
         * Occurs when a remote user (Communication)/ host (Live Broadcast) joins the channel.
         * This callback is triggered in either of the following scenarios:
         *
         * A remote user/host joins the channel by calling the joinChannel method.
         * A remote user switches the user role to the host by calling the setClientRole method after joining the channel.
         * A remote user/host rejoins the channel after a network interruption.
         * The host injects an online media stream into the channel by calling the addInjectStreamUrl method.
         *
         * @param uid User ID of the remote user sending the video streams.
         * @param elapsed Time elapsed (ms) from the local user calling the joinChannel method until this callback is triggered.
         */
        override fun onUserJoined(uid: Int, elapsed: Int) {
           requireActivity().runOnUiThread{
                setupRemoteVideo(uid) }
        }

        /**
         * Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
         *
         * There are two reasons for users to become offline:
         *
         *     Leave the channel: When the user/host leaves the channel, the user/host sends a
         *     goodbye message. When this message is received, the SDK determines that the
         *     user/host leaves the channel.
         *
         *     Drop offline: When no data packet of the user or host is received for a certain
         *     period of time (20 seconds for the communication profile, and more for the live
         *     broadcast profile), the SDK assumes that the user/host drops offline. A poor
         *     network connection may lead to false detections, so we recommend using the
         *     Agora RTM SDK for reliable offline detection.
         *
         * @param uid ID of the user or host who leaves the channel or goes offline.
         * @param reason Reason why the user goes offline:
         *
         *     USER_OFFLINE_QUIT(0): The user left the current channel.
         *     USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data packet was received within a certain period of time.
         *     If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline.
         *     USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from the host to the audience.
         */
        override fun onUserOffline(uid: Int, reason: Int) {
             onRemoteUserLeft()
        }

        override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
            activityScope.launch { //                    mLogView.logI("First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                Log.e("callAccepted",uid.toString())
                //  isVideoCallPicked = true
                if (mCounter != null)
                    mCounter!!.cancel()
                stopRinging()
                setupRemoteVideo(uid)
            }
        }

        /**
         * Occurs when a remote user stops/resumes sending the video stream.
         *
         * @param uid ID of the remote user.
         * @param muted Whether the remote user's video stream playback pauses/resumes:
         * true: Pause.
         * false: Resume.
         */
        override fun onUserMuteVideo(uid: Int, muted: Boolean) {
            activityScope.launch { onRemoteUserVideoMuted(uid, muted) }
        }
    }

    override fun getViewBinding(): FragmentVideoCallBinding {
        return FragmentVideoCallBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CacheConstants.Current = "video"
        socketManager = App.mInstance.getSocketManager()!!
        if (!socketManager.isConnected() || socketManager.getmSocket() == null)
            socketManager.init()
        socketManager.unRegister(this)
        socketManager.onRegister(this)
        if (!socketManager.isConnected() || socketManager.getmSocket() == null)
            socketManager.init()
        activateCallStatusListener()

        val bundle = arguments
        channelName = bundle?.getString("channel_name")!!
        agoraToken = bundle.getString("video_token")!!

        activateReceiverListenerSocket()
        handleClicks()
       // videoTimingDialog()

        if (checkSelfPermission(
                Manifest.permission.RECORD_AUDIO,
                PERMISSION_REQ_ID_RECORD_AUDIO
            ) && checkSelfPermission(
                Manifest.permission.CAMERA,
                PERMISSION_REQ_ID_CAMERA
            )
        ) {
            initAgoraEngineAndJoinChannel()
        }

    }

    private fun initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine()
        joinChannel()
        setupVideoProfile()
        setupLocalVideo()

    }

    private fun initializeAgoraEngine() {
        try {
            mRtcEngine =
                RtcEngine.create(requireContext(), getString(R.string.agora_app_id), mRtcEventHandler)
        } catch (e: Exception) {

            throw RuntimeException(
                "NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(
                    e
                )
            )
        }
    }

    private fun setupVideoProfile() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
        mRtcEngine?.enableVideo()
//      mRtcEngine!!.setVideoProfile(Constants.VIDEO_PROFILE_360P, false) // Earlier than 2.3.0

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine?.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )
    }

    private fun setupLocalVideo() {
        // This is used to set a local preview.
        // The steps setting local and remote view are very similar.
        // But note that if the local user do not have a uid or do
        // not care what the uid is, he can set his uid as ZERO.
        // Our server will assign one and return the uid via the event
        // handler callback function (onJoinChannelSuccess) after
        // joining the channel successfully.
        val container = binding.localVideoViewContainer
        val surfaceView = RtcEngine.CreateRendererView(requireContext())
        surfaceView.setZOrderMediaOverlay(true)
        container.addView(surfaceView)
        // Initializes the local video view.
        // RENDER_MODE_FIT: Uniformly scale the video until one of its dimension fits the boundary. Areas that are not filled due to the disparity in the aspect ratio are filled with black.
        mRtcEngine?.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0))

    }

    private fun joinChannel() {
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name that
        // you use to generate this token.

        var token: String? = getString(R.string.agora_app_id)
        if (agoraToken.isEmpty()) {
            token = null
        }
        if (!isReceiver) {
            startRinging()
            timeCounter()
        }
        mRtcEngine?.joinChannel(
            null,
            channelName,
            "Extra Optional Data",
            0
        )

    }
    private fun startRinging() {

        mPlayer = playCalleeRing()

    }
    private fun playCalleeRing(): MediaPlayer {
        return startRinging(R.raw.basic_tones)
    }

    private fun startRinging(resource: Int): MediaPlayer {
        val player = MediaPlayer.create(requireContext(), resource)
        player.isLooping = true
        player.start()
        return player
    }
    private fun setupRemoteVideo(uid: Int) {

        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        val container = binding.remoteVideoViewContainer1

        if (container.childCount >= 1) {
            return
        }

        /*
          Creates the video renderer view.
          CreateRendererView returns the SurfaceView type. The operation and layout of the view
          are managed by the app, and the Agora SDK renders the view provided by the app.
          The video display view must be created using this method instead of directly
          calling SurfaceView.
         */
        val surfaceView = RtcEngine.CreateRendererView(requireContext())
        container.addView(surfaceView)
        // Initializes the video view of a remote user.
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid))

        surfaceView.tag = uid // for mark purpose
    }

    private fun leaveChannel() {
        mRtcEngine?.leaveChannel()
    }

    private fun onRemoteUserLeft() {
        val container = binding.remoteVideoViewContainer1
        container.removeAllViews()
        activity?.finish()

    }

    private fun onRemoteUserVideoMuted(uid: Int, muted: Boolean) {
        val container =binding.remoteVideoViewContainer1

        val surfaceView = container.getChildAt(0) as SurfaceView

        val tag = surfaceView.tag
        if (tag != null && tag as Int == uid) {
            surfaceView.visibility = if (muted) View.GONE else View.VISIBLE
        }
    }

    fun activateReceiverListenerSocket() {

        val jsonObject = JSONObject()
        jsonObject.put("status", "1")
        socketManager.getCallStatus(jsonObject)
    }

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(permission),
                requestCode
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQ_ID_RECORD_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA,
                        PERMISSION_REQ_ID_CAMERA
                    )
                } else {
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO)
                    Log.e("===three","finish")
                    activity?.finish()
                }
            }
            PERMISSION_REQ_ID_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initAgoraEngineAndJoinChannel()
                } else {
                    showLongToast("No permission for " + Manifest.permission.CAMERA)
                    Log.e("===four","finish")
                    activity?.finish()
                }
            }
        }
    }

    private fun showLongToast(msg: String) {
         Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }


    private fun timeCounter()
    {
        mCounter = object : CountDownTimer(45000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                Log.e("Tag","seconds remaining: " + millisUntilFinished / 1000)

            }
            override fun onFinish() {
                stopRinging()
                Log.e("===two","finish")
                //  finish()
                // showToast(resources.getString(R.string.no_answer))
            }
        }.start()
    }

    private fun stopRinging() {
        if (mPlayer != null && mPlayer!!.isPlaying) {
            mPlayer!!.stop()
            mPlayer!!.release()
            mPlayer = null
        }
    }

    private fun activateCallStatusListener() {
        socketManager.callStatusActivate()

    }

    private fun handleClicks() {
//        binding.tb.ivBack.isVisible()
//        binding.tb.ivAppLogo.isVisible()
//
//        binding.tb.ivBack.setOnClickListener {
//            activity?.onBackPressed()
//        }

//        binding.tvIcebreaker.setOnClickListener {
//            this.findNavController().navigate(R.id.action_videoCallFragment_to_icebreakerQuestionsFragment)
//        }

     /*   binding.tvCancel.setOnClickListener {
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
        }*/


//        (TimeUnit.MILLISECONDS.toMinutes(millis) -
//                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))),
//        (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
//            TimeUnit.MILLISECONDS.toMinutes(millis)


    }

    private fun getCallStatus() {
        val jsonObject = JSONObject()
        jsonObject.put("status", status)
        jsonObject.put("channelName", channelName)
        jsonObject.put("isCallEnd", isCallEnd)
        jsonObject.put("duration", duration)
        socketManager.getCallStatus(jsonObject)

    }


    override fun onResponseArray(event: String, args: JSONArray) {

    }

    override fun onResponse(event: String, args: JSONObject) {

        when(event){
            SocketManager.call_status_listener ->{
                activityScope.launch {

                }
            }

        }

    }

    override fun onError(event: String, vararg args: Array<*>) {

    }

    private fun videoTimingDialog() {
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

            show()
        }
    }
    companion object {

        private val LOG_TAG = VideoCallFragment::class.java.simpleName

        private const val PERMISSION_REQ_ID_RECORD_AUDIO = 22
        private const val PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1
    }

}