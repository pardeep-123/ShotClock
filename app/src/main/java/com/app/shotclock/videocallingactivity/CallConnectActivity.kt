package com.app.shotclock.videocallingactivity

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import com.app.shotclock.R
import com.app.shotclock.base.BaseActivity
import com.app.shotclock.databinding.ActivityVideoChatViewBinding
import com.app.shotclock.utils.App
import com.app.shotclock.utils.SocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.json.JSONArray
import org.json.JSONObject

class CallConnectActivity : BaseActivity(), SocketManager.Observer, View.OnClickListener {
    private lateinit var binding: ActivityVideoChatViewBinding
    var requestId = ""
    var channelName = ""
    var agoraToken = ""
    var callerName=""
    var callerImage=""
    private var mPlayer: MediaPlayer? = null
    private val activityScope = CoroutineScope(Dispatchers.Main)
    private lateinit var socketManager: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoChatViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeSocket()
        activateReceiverListenerSocket()
        requestId = intent.getStringExtra("requestId").toString()
        channelName = intent.getStringExtra("channelName").toString()
        agoraToken = intent.getStringExtra("agoraToken").toString()
        callerName = intent.getStringExtra("callerName").toString()
        callerImage = intent.getStringExtra("callerImage").toString()
//        ivImage.loadImage(callerImage)
//        tvName.text= "$callerName phone is ringing"

        startRinging()

//        ivRejectCall.setOnClickListener(this)

    }

    private fun startRinging() {
        mPlayer = playCallRing()
    }

    private fun playCallRing(): MediaPlayer {
        return startRinging(R.raw.basic_tones)
    }

    fun activateReceiverListenerSocket() {

//        socketManager.receivedMessageActivate()
    }

    private fun startRinging(resource: Int): MediaPlayer {
        val player = MediaPlayer.create(this, resource)
        player.isLooping = true
        player.start()
        return player
    }

    private fun stopRinging() {
        try {
            if (mPlayer != null && mPlayer!!.isPlaying) {
                mPlayer!!.stop()
                mPlayer!!.release()
                mPlayer = null
            }
        } catch (ex: Exception) {
        }
    }

    private fun initializeSocket() {

        socketManager = App.mInstance.getSocketManager()!!
        if (!socketManager.isConnected() || socketManager.getmSocket() == null) {
            socketManager.init()
        }
        socketManager.unRegister(this)
        socketManager.onRegister(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        stopRinging()
//        val jsonObject = JSONObject()
//        jsonObject.put("requestId", requestId)
//        socketManager.callTermination(jsonObject)
        socketManager.unRegister(this)
    }

    override fun onResume() {
        super.onResume()
        socketManager.onRegister(this)
    }

    override fun onResponseArray(event: String, args: JSONArray) {

    }

    override fun onResponse(event: String, args: JSONObject) {
        when (event) {
//            SocketManager.call_request_action_response_listener -> {
//                stopRinging()
//                var data = args as JSONObject
//                Log.e("callResponse", data.toString())
//                val gson = GsonBuilder().create()
//                val callData = gson.fromJson(
//                    data.toString(),
//                    CallActionResponseListener::class.java
//                )
//                if (callData.status == 2) {
//                    finish()
//                } else {
//                    if (callData.type == "1") {
//                        val intent = Intent(this@CallConnectActivity, VoiceChatViewActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
//                        intent.putExtra("requestId", requestId)
//                        intent.putExtra("channelName", channelName)
//                        intent.putExtra("agoraToken", agoraToken)
//                        startActivity(intent)
//                        finish()
//                    } else {
//                        val intent = Intent(this@CallConnectActivity, VideoCallActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
//                        intent.putExtra("requestId", requestId.toString())
//                        intent.putExtra("channelName", channelName)
//                        intent.putExtra("agoraToken", agoraToken)
//                        startActivity(intent)
//                        finish()
//                    }
//                }
//            }
//            SocketManager.call_request_action_listener -> {
//                stopRinging()
//                var data = args as JSONObject
//                Log.e("callResponse", data.toString())
//                val gson = GsonBuilder().create()
//                val callData = gson.fromJson(
//                    data.toString(),
//                    CallActionResponseListener::class.java
//                )
//
//                if (callData.status == 2) {
//                    finish()
//                } else if (callData.type == "1") {
//                    val intent = Intent(this@CallConnectActivity, VoiceChatViewActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
//                    intent.putExtra("requestId", requestId.toString())
//                    intent.putExtra("channelName", channelName)
//                    intent.putExtra("agoraToken", agoraToken)
//                    startActivity(intent)
//                    finish()
//                } else {
//                    val intent = Intent(this@CallConnectActivity, VideoCallActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
//                    intent.putExtra("requestId", requestId.toString())
//                    intent.putExtra("channelName", channelName)
//                    intent.putExtra("agoraToken", agoraToken)
//                    startActivity(intent)
//                    finish()
//                }
//
//            }
//
//            SocketManager.call_termination_event -> {
//                activityScope.launch {
//                    stopRinging()
//                    var data = args as JSONObject
//                    Log.e("callResponse", data.toString())
//                    val gson = GsonBuilder().create()
//                    val callData = gson.fromJson(
//                        data.toString(),
//                        CallTerminateResponse::class.java
//                    )
//                    if (callData.requestId.toString() == requestId) {
//                        finish()
//                    }
//                }
//            }
//            SocketManager.call_termination_listener -> {
//                stopRinging()
//                var data = args as JSONObject
//                Log.e("callResponse", data.toString())
//                val gson = GsonBuilder().create()
//                val callData = gson.fromJson(
//                    data.toString(),
//                    CallTerminateResponse::class.java
//                )
//                if (callData.requestId.toString() == requestId) {
//                    finish()
//                }
//            }
        }
    }

    override fun onError(event: String, vararg args: Array<*>) {

    }

    override fun onClick(v: View?) {

    }

//    override fun onClick(v: View?) {
//        when (v?.id) {
//            R.id.ivRejectCall -> {
//
//                stopRinging()
//                socketManager.unRegister(this)
//                socketManager.onRegister(this)
//                val jsonObject = JSONObject()
//                if(!requestId.isNullOrEmpty()){
//                    jsonObject.put("requestId", requestId.toInt())
//                    socketManager.callTermination(jsonObject)
//                }else{
//                    finish()
//                }
//
//            }
//        }
//    }
}