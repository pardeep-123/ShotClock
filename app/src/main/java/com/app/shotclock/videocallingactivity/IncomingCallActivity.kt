package com.app.shotclock.videocallingactivity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import com.app.shotclock.R
import com.app.shotclock.base.BaseActivity
import com.app.shotclock.databinding.ItemsNotificationVideoCallingBinding
import com.app.shotclock.models.sockets.VideoCallStatusResponse
import com.app.shotclock.utils.App
import com.app.shotclock.utils.SocketManager
import com.app.shotclock.utils.isNetworkConnected
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class IncomingCallActivity :BaseActivity() , SocketManager.Observer {
    lateinit var binding : ItemsNotificationVideoCallingBinding
    var mCallerId = ""
    var mReceiverID = 0
    var mSenderImage = ""
    private var callerName = ""
    var mChannelName = ""
    var agoraToken = ""
    var requestId = 0
    var status = 0
    private var callType = "" // 0= singleCall , 1= groupCall
    private var callerImage = ""
    private var mAnimator: PortraitAnimator? = null
    private var mPlayer: MediaPlayer? = null
    private var mCounter: CountDownTimer? = null
    private lateinit var socketManager: SocketManager
    private val activityScope = CoroutineScope(Dispatchers.Main)


//    override fun getViewBinding(): ItemsNotificationVideoCallingBinding {
//        return ItemsNotificationVideoCallingBinding.inflate(layoutInflater)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = ItemsNotificationVideoCallingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeSocket()
        activateReceiverListenerSocket()


        mChannelName = intent.extras?.get("channelName").toString()
        callerName = intent.extras?.get("receiverName").toString()
        mCallerId = intent.extras?.getString("senderId").toString()
        agoraToken = intent.extras?.get("token").toString()
        callType = intent.extras?.get("callType").toString()

        Log.e("channelName", mChannelName)

        setOnClicks()

        timeCounter()
        startRinging()
    }

   private fun activateReceiverListenerSocket() {
       socketManager.callToUserActivate()
       socketManager.callStatusActivate()

    }

    private fun setOnClicks() {
        binding.btAccept.setOnClickListener {
            stopRinging()
                //0=>calling,1=> callConnected,2=>call Declined,3=>Call Disconnected,4=>Missed call
//                val jsonObject = JSONObject()
//                jsonObject.put("channelName", mChannelName)
//                jsonObject.put("status", "1")
//                jsonObject.put("isCallEnd","2")
//                jsonObject.put("duration","0")
//                socketManager.getCallStatus(jsonObject)

            /**
             * send to video call
             */
            val intent = Intent(this@IncomingCallActivity, VideoCallActivity::class.java)
            intent.putExtra("channel_name", mChannelName)
            intent.putExtra("video_token", agoraToken)
            intent.putExtra("senderId", mCallerId)
            intent.putExtra("type","chat")
            startActivity(intent)
            finish()
        }

        binding.btDecline.setOnClickListener {
            stopRinging()
            if (isNetworkConnected()) {
                val jsonObject = JSONObject()
                jsonObject.put("channelName", mChannelName)
                jsonObject.put("status", "2")
                jsonObject.put("receiverId", mCallerId)
//                jsonObject.put("isCallEnd","2")
//                jsonObject.put("duration","0")
                socketManager.getCallStatus(jsonObject)

                val notifManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notifManager.cancelAll()
            } else {
            }
            //   showAlertWithOk(resources.getString(R.string.internet_connection))
        }

        binding.tvSenderName.text = callerName

    }

    private fun initializeSocket() {

        socketManager = App.mInstance.getSocketManager()!!
        if (!socketManager.isConnected() || socketManager.getmSocket() == null) {
            socketManager.init()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        stopRinging()
        mCounter?.cancel()
        socketManager.unRegister(this)
    }

    override fun onResume() {
        super.onResume()
        socketManager.unRegister(this)
        socketManager.onRegister(this)
    }

    private fun startRinging() {
        /*if (isCallee()) {
            mPlayer = playCalleeRing()
        } else if (isCaller()) {*/
        mPlayer = playCallerRing()
        /*}*/
    }

    private fun playCallerRing(): MediaPlayer {
        return startRinging(R.raw.basic_tones)
    }


    private fun startRinging(resource: Int): MediaPlayer {
        val player = MediaPlayer.create(this, resource)
        player.isLooping = true
        player.start()
        return player
    }

    private fun stopRinging() {
        if (mPlayer != null && mPlayer?.isPlaying!!) {
            mPlayer?.stop()
            mPlayer?.release()
            mPlayer = null
        }
    }

    override fun onStart() {
        super.onStart()
        mAnimator?.start()
    }

    override fun onStop() {
        super.onStop()
        stopRinging()
        mAnimator?.stop()

    }


    private fun timeCounter() {
        mCounter = object : CountDownTimer(45000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                Log.e("Tag", "seconds remaining: " + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                //showToast(resources.getString(R.string.no_answer))
                finish()
            }
        }.start()
    }

    private class PortraitAnimator internal constructor(
        private val mLayer1: View,
        private val mLayer2: View,
        private val mLayer3: View
    ) {
        private val mAnim1: Animation
        private val mAnim2: Animation
        private val mAnim3: Animation
        private var mIsRunning = false
        private fun buildAnimation(startOffset: Int): AnimationSet {
            val set = AnimationSet(true)
            val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
            alphaAnimation.duration = ANIM_DURATION.toLong()
            alphaAnimation.startOffset = startOffset.toLong()
            alphaAnimation.repeatCount = Animation.INFINITE
            alphaAnimation.repeatMode = Animation.RESTART
            alphaAnimation.fillAfter = true
            val scaleAnimation = ScaleAnimation(
                1.0f, 1.3f, 1.0f, 1.3f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
            )
            scaleAnimation.duration = ANIM_DURATION.toLong()
            scaleAnimation.startOffset = startOffset.toLong()
            scaleAnimation.repeatCount = Animation.INFINITE
            scaleAnimation.repeatMode = Animation.RESTART
            scaleAnimation.fillAfter = true
            set.addAnimation(alphaAnimation)
            set.addAnimation(scaleAnimation)
            return set
        }

        fun start() {
            if (!mIsRunning) {
                mIsRunning = true
                mLayer1.visibility = View.VISIBLE
                mLayer2.visibility = View.VISIBLE
                mLayer3.visibility = View.VISIBLE
                mLayer1.startAnimation(mAnim1)
                mLayer2.startAnimation(mAnim2)
                mLayer3.startAnimation(mAnim3)
            }
        }

        fun stop() {
            mLayer1.clearAnimation()
            mLayer2.clearAnimation()
            mLayer3.clearAnimation()
            mLayer1.visibility = View.GONE
            mLayer2.visibility = View.GONE
            mLayer3.visibility = View.GONE
        }

        companion object {
            const val ANIM_DURATION = 3000
        }

        init {
            mAnim1 = buildAnimation(0)
            mAnim2 = buildAnimation(1000)
            mAnim3 = buildAnimation(2000)
        }
    }

    override fun onResponseArray(event: String, args: JSONArray) {

    }

    override fun onResponse(event: String, args: JSONObject) {

        when (event) {
            SocketManager.call_status_emitter->{
                activityScope.launch {
                    val data = args as JSONObject
                    Log.e("callStatus",data.toString())
                    val gson = GsonBuilder().create()
                    val userToCallList = gson.fromJson(data.toString(), VideoCallStatusResponse::class.java)
                     if (userToCallList.status == 1) {
                         val intent = Intent(this@IncomingCallActivity, VideoCallActivity::class.java)
                         intent.putExtra("channel_name", userToCallList.channelName)
                         intent.putExtra("video_token", userToCallList.videoToken)
                         intent.putExtra("type","chat")
                         startActivity(intent)
                         finish()
                     }else{
                         finish()
                     }
                }
            }

            SocketManager.call_status_listener->{
                activityScope.launch {
                    val data = args as JSONObject
                    Log.e("callStatus",data.toString())
                    val gson = GsonBuilder().create()
                    val userToCallList = gson.fromJson(data.toString(), VideoCallStatusResponse::class.java)
                    if (userToCallList.status == 1) {
                        val intent = Intent(this@IncomingCallActivity, VideoCallActivity::class.java)
                        intent.putExtra("channel_name", userToCallList.channelName)
                        intent.putExtra("video_token", userToCallList.videoToken)
                        intent.putExtra("type","chat")
                        startActivity(intent)
                        finish()
                    }else{
                        finish()
                    }
                }
            }
        }

    }

    override fun onError(event: String, vararg args: Array<*>) {

    }

}
