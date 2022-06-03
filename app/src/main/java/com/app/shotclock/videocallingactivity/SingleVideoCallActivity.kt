package com.app.shotclock.videocallingactivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.shotclock.R
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.activities.SelectUserActivity
import com.app.shotclock.base.BaseActivity
import com.app.shotclock.cache.getUser
import com.app.shotclock.databinding.ActivitySingleVideoCallBinding
import com.app.shotclock.databinding.ActivityVideoChatViewBinding
import com.app.shotclock.models.RequestListResponseModel
import com.app.shotclock.models.sockets.VideoCallResponse
import com.app.shotclock.utils.*
import com.google.android.material.button.MaterialButton
import com.google.gson.GsonBuilder
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

import android.R.attr.name
import android.view.WindowManager


class SingleVideoCallActivity : BaseActivity(), SocketManager.Observer {
    private lateinit var binding: ActivitySingleVideoCallBinding
    var channelName = "test"
    var name = ""
    private lateinit var socketManager: SocketManager
    private var mRtcEngine: RtcEngine? = null
    var builder: AlertDialog.Builder? = null
    var agoraToken = "006fd6547be222a4a7f88ad72c90df6e761IADSf+UGPSCGYtVb/qP7T8wdLLVcujfR6jrByLQbOIZGHwx+f9gAAAAAEAD+JYqAxVp2YgEAAQDFWnZi"
    private var isReciever = false
    private var senderId = ""
    var requestId = ""
    private var type = ""
    private var groupName = ""
    private val activityScope = CoroutineScope(Dispatchers.Main)

    private var mPlayer: MediaPlayer? = null

    private var mCounter: CountDownTimer? = null
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
        override fun onError(err: Int) {
            super.onError(err)
            Log.e("onErrorAgora",err.toString())
        }

        override fun onUserJoined(uid: Int, elapsed: Int) {
            Log.e("statusGet","onUserJoined")
            setupRemoteVideo(uid)
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
            runOnUiThread { onRemoteUserLeft() }
        }

        override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
            runOnUiThread {
                //                    mLogView.logI("First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                Log.e("callAccepted", uid.toString())
                //  isVideoCallPicked = true
                if (mCounter != null)
                    mCounter!!.cancel()
                stopRinging()
               setupRemoteVideo(uid)
                socketManager.callStatusActivate()
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
            runOnUiThread { onRemoteUserVideoMuted(uid, muted) }
        }
    }

    private fun timeCounter() {
        mCounter = object : CountDownTimer(45000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                Log.e("Tag", "seconds remaining: " + millisUntilFinished / 1000)

            }

            override fun onFinish() {
                stopRinging()
                Log.e("===two", "finish")
                //  finish()
                // showToast(resources.getString(R.string.no_answer))
            }
        }.start()
    }

    private fun stopRinging() {
        if (mPlayer != null && mPlayer!!.isPlaying()) {
            mPlayer!!.stop()
            mPlayer!!.release()
            mPlayer = null
        }
    }

    var selectedUser = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        try {
            channelName = intent?.getStringExtra("channel_name").toString()
            agoraToken = intent?.getStringExtra("video_token").toString()
            groupName = intent?.getStringExtra("groupName").toString()
            senderId = intent?.getStringExtra("senderId").toString()

        } catch (e: Exception) {
        }

        type = intent?.getStringExtra("type").toString()

        if (checkSelfPermission(
                Manifest.permission.RECORD_AUDIO,
                PERMISSION_REQ_ID_RECORD_AUDIO
            ) && checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)
        ) {
            initAgoraEngineAndJoinChannel()
        }

        Log.e("videocall", channelName + "====" + agoraToken)

        binding.tvIcebreaker.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fromVideo", "fromVideo")
            startActivity(intent)
            finish()
        }

        initialiseSocket()

        socketManager.callStatusActivate()

    }


    private fun initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine()
        joinChannel()
        setupLocalVideo()
        setupVideoProfile()

    }

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        Log.i(LOG_TAG, "checkSelfPermission $permission $requestCode")
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                requestCode
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode)

        when (requestCode) {
            PERMISSION_REQ_ID_RECORD_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)
                } else {
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO)
                    Log.e("===three", "finish")
                   // finish()
                }
            }
            PERMISSION_REQ_ID_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initAgoraEngineAndJoinChannel()
                } else {
                    showLongToast("No permission for " + Manifest.permission.CAMERA)
                    Log.e("===four", "finish")
                  //  finish()
                }
            }
        }
    }

    private fun showLongToast(msg: String) {
        this.runOnUiThread { Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show() }
    }

    override fun onDestroy() {
        super.onDestroy()
//        val jsonObject = JSONObject()
//        jsonObject.put("status", "3")
//        socketManager.getCallStatus(jsonObject)
        callStatus("3","2")
        socketManager.unRegister(this)
        leaveChannel()

        RtcEngine.destroy()
        mRtcEngine = null
    }

    fun onLocalVideoMuteClicked(view: View) {
        val iv = view as ImageView
        if (iv.isSelected) {
            iv.isSelected = false
            iv.clearColorFilter()
        } else {
            iv.isSelected = true
            iv.setColorFilter(resources.getColor(R.color.pink), PorterDuff.Mode.MULTIPLY)
        }

        // Stops/Resumes sending the local video stream.
        mRtcEngine!!.muteLocalVideoStream(iv.isSelected)

        val container = findViewById<View>(R.id.local_video_view_container) as FrameLayout
        val surfaceView = container.getChildAt(0) as SurfaceView
        surfaceView.setZOrderMediaOverlay(!iv.isSelected)
        surfaceView.visibility = if (iv.isSelected) {
            iv.setImageResource(R.drawable.ic_video)
            View.GONE
        } else {
            iv.setImageResource(R.drawable.ic_video)
            View.VISIBLE
        }

    }

    fun onLocalAudioMuteClicked(view: View) {
        val iv = view as ImageView
        if (iv.isSelected) {
            iv.isSelected = false
            iv.clearColorFilter()
        } else {
            iv.isSelected = true
            iv.setColorFilter(resources.getColor(R.color.pink), PorterDuff.Mode.MULTIPLY)
        }

        // Stops/Resumes sending the local audio stream.
        mRtcEngine?.muteLocalAudioStream(iv.isSelected)
    }

    fun onSwitchCameraClicked(view: View) {
        // Switches between front and rear cameras.
        mRtcEngine!!.switchCamera()
    }
    var isOwnReject=false

    //   (0=>calling,1=> callConnected,2=>call Declined,3=>Call Disconnected,4=>Missed call
    fun onEncCallClicked(view: View) {
        if (isNetworkConnected()) {

                callStatus("3","2")


        }
    }

    private fun initializeAgoraEngine() {
        try {
            mRtcEngine =
                RtcEngine.create(baseContext, getString(R.string.agora_app_id), mRtcEventHandler)
        } catch (e: Exception) {
            Log.e(LOG_TAG, Log.getStackTraceString(e))

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
        val container = findViewById<FrameLayout>(R.id.local_video_view_container)
        val surfaceView = RtcEngine.CreateRendererView(baseContext)
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
        if (!isReciever) {
            startRinging()
            timeCounter()
        }
        Log.e("channelData", agoraToken)
        Log.e("channelData", channelName)
        mRtcEngine?.joinChannel(
            "",
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
        val player = MediaPlayer.create(this, resource)
        player.isLooping = false
        player.start()
        return player
    }


    private fun setupRemoteVideo(uid: Int) {

        Log.e("remoteJoined","true")

        val container = findViewById<View>(R.id.remote_video_view_container) as FrameLayout

        if (container.childCount >= 1) {
            return
        }
        val surfaceView = RtcEngine.CreateRendererView(this)
        container.addView(surfaceView)
        // Initializes the video view of a remote user.
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid))

        surfaceView.tag = uid // for mark purpose
     }

    private fun leaveChannel() {
        mRtcEngine?.leaveChannel()
    }

    private fun onRemoteUserLeft() {
        val container = findViewById<View>(R.id.remote_video_view_container) as FrameLayout
        container.removeAllViews()
       finish()

    }

    private fun onRemoteUserVideoMuted(uid: Int, muted: Boolean) {
        val container = findViewById<View>(R.id.remote_video_view_container) as FrameLayout

        val surfaceView = container.getChildAt(0) as SurfaceView

        val tag = surfaceView.tag
        if (tag != null && tag as Int == uid) {
            surfaceView.visibility = if (muted) View.GONE else View.VISIBLE
        }
    }

    companion object {

        private val LOG_TAG = SingleVideoCallActivity::class.java.simpleName

        private const val PERMISSION_REQ_ID_RECORD_AUDIO = 22
        private const val PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1
    }

    override fun onResume() {
        super.onResume()
        initialiseSocket()
        socketManager.callStatusActivate()
    }

    private fun initialiseSocket() {
        socketManager = App.mInstance.getSocketManager()!!
        if (!socketManager.isConnected() || socketManager.getmSocket() == null) {
            socketManager.init()
        }
        socketManager.unRegister(this)
        socketManager.onRegister(this)
        socketManager.callStatusActivate()

    }

    override fun onBackPressed() {
        finish()
    }


    override fun onResponseArray(event: String, args: JSONArray) {

    }


    override fun onResponse(event: String, args: JSONObject) {
        when (event) {
            SocketManager.call_to_user_emitter -> {
                activityScope.launch {
                    val mObject = args as JSONObject
                    val gson = GsonBuilder().create()
                    val userToCallList =
                        gson.fromJson(mObject.toString(), VideoCallResponse::class.java)
                    channelName = userToCallList.channelName
                    agoraToken = userToCallList.videoToken
                    senderId = userToCallList.receiverId.toString()



                }
            }
            SocketManager.call_status_listener -> {

                Log.e("call_status_listener",args.toString())
                activityScope.launch {
                    val mObject = args as JSONObject
                    val gson = GsonBuilder().create()
                    val userToCallList = gson.fromJson(mObject.toString(), VideoCallResponse::class.java)

                    if (userToCallList.status == 3 || userToCallList.status == 2 || userToCallList.status == 4) {

                        val intent = Intent(this@SingleVideoCallActivity, HomeActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                        startActivity(intent)
                        finish()

                    }


                }

            }

            SocketManager.call_status_emitter -> {

                Log.e("call_status_emitter",args.toString())
                activityScope.launch {
                    val mObject = args as JSONObject
                    val gson = GsonBuilder().create()
                    val userToCallList = gson.fromJson(mObject.toString(), VideoCallResponse::class.java)


                    if (userToCallList.status == 3 || userToCallList.status == 2 || userToCallList.status == 4) {

                        val intent =
                            Intent(this@SingleVideoCallActivity, HomeActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                        startActivity(intent)
                        finish()

                    }
                }

            }
        }
    }

    private fun callStatus(status: String, isCallEnd: String) {

        val jsonObject = JSONObject()
        jsonObject.put("channelName", channelName)
        jsonObject.put("status", status)
        jsonObject.put("receiverId", senderId)
        jsonObject.put("isCallEnd", isCallEnd)
        jsonObject.put("duration", "0")
        socketManager.getCallStatus(jsonObject)

    }

    override fun onError(event: String, vararg args: Array<*>) {

    }
}
