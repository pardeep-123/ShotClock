package com.app.shotclock.fragments

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.shotclock.R
import com.app.shotclock.adapters.ChatAdapter
import com.app.shotclock.cache.getUser
import com.app.shotclock.cache.saveChatString
import com.app.shotclock.cache.saveString
import com.app.shotclock.constants.CacheConstants
import com.app.shotclock.databinding.FragmentChatBinding
import com.app.shotclock.models.sockets.ChatHistoryResponse
import com.app.shotclock.models.sockets.VideoCallResponse
import com.app.shotclock.utils.*
import com.app.shotclock.videocallingactivity.SingleVideoCallActivity
import com.app.shotclock.videocallingactivity.VideoCallActivity
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class ChatFragment : ImagePickerUtility1<FragmentChatBinding>(), SocketManager.Observer {

    private var chatAdapter: ChatAdapter? = null
    private val activityScope = CoroutineScope(Dispatchers.Main)
    private lateinit var socketManager: SocketManager
    private var linearLayoutManager: LinearLayoutManager? = null
    private var user2Id = 0
    private var userName = ""
    private var messageType = 0  // 0-text, 1-image
    private var chatList = ArrayList<ChatHistoryResponse.ChatHistoryResponseItem>()
    private var imageFilePath = ""
    private var extension = ""
    private var receiverImage = ""


    override fun getViewBinding(): FragmentChatBinding {
        return FragmentChatBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CacheConstants.Current = "chat"
        Constants.OnMessageScreen = true
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        socketManager = App.mInstance.getSocketManager()!!
        if (!socketManager.isConnected() || socketManager.getmSocket() == null)
            socketManager.init()

        val bundle = arguments
        user2Id = bundle?.getInt("user2Id")!!
        userName = bundle.getString("username")!!
        if (bundle.getString("receiverImage") != null)
        receiverImage = bundle.getString("receiverImage")!!
        Constants.user2Id = user2Id.toString()

        clickHandle()
        setAdapter()


    }

    private fun setAdapter() {

        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        chatAdapter = ChatAdapter(requireContext(), chatList)
        binding.rvChat.layoutManager = linearLayoutManager
        binding.rvChat.adapter = chatAdapter

//        scrollToPosition(items.size() - 1)
//        binding.rvChat.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
//            if (bottom < oldBottom) scrollToBottom()
//        }

    }

   private fun scrollToBottom(){
        linearLayoutManager?.scrollToPosition(chatList.size-1)
    }

//    private fun scrollToBottom() {
//        linearLayoutManager?.smoothScrollToPosition(binding.rvChat, null, chatAdapter?.itemCount!!)
//    }

    var FACEBOOK_URL = "https://www.facebook.com/Shot-Clock-Dating-118006690891734"
    var FACEBOOK_PAGE_ID = "Shot-Clock-Dating-118006690891734"

    //method to get the right URL to use in the intent
    fun getFacebookPageURL(context: Context){
        val packageManager = context.packageManager
         try {
            val versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
            if (versionCode >= 3002850) { //newer versions of fb app
                "fb://facewebmodal/f?href=$FACEBOOK_URL"
            } else { //older versions of fb app
                "fb://page/$FACEBOOK_PAGE_ID"
            }
        } catch (e: PackageManager.NameNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/Shot-Clock-Dating-118006690891734")
                )
            )
        }
    }

    private fun clickHandle() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivVideoCall.isVisible()
        binding.tb.ivFb.isVisible()
        binding.tb.ivInsta.isVisible()

        binding.tb.ivFb.setOnClickListener {
            getFacebookPageURL(requireActivity())
        }

        binding.tb.ivInsta.setOnClickListener {
            getOpenInstaIntent(requireActivity())
        }

        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.tb.ivVideoCall.setOnClickListener {
            // to video call to user
            callToUser()
        }

        binding.ivAttachment.setOnClickListener {
            getImage(requireActivity(), 0, false)
        }

        binding.ivSend.setOnClickListener {
            // for send text
            if (messageType == 0) {
                if (binding.etSendMsg.text.toString().trim().isEmpty()) {
                    showErrorAlert(requireActivity(),getString(R.string.please_enter_something))
                } else {
                    sendMessageChat()
                    binding.etSendMsg.text.clear()
                }
            }
        }
    }

    private fun getOpenInstaIntent(requireActivity: Context) {

        val uri = Uri.parse("http://instagram.com/_u/shotclockdating")


        val i = Intent(Intent.ACTION_VIEW, uri)

        i.setPackage("com.instagram.android")

        try {
            startActivity(i)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/shotclockdating")
                )
            )
        }

    }

    private fun getChatHistory() {
        val jsonObject = JSONObject()
//        val userid = getUser(requireContext())?.id.toString()
        jsonObject.put("userid", getUser(requireContext())?.id)
        jsonObject.put("user2Id", user2Id)
        socketManager.getFriendChat(jsonObject)

    }

    private fun readUnreadMessage() {
        val jsonObject = JSONObject()
        jsonObject.put("user2Id", user2Id)
        jsonObject.put("userid", getUser(requireContext())?.id)
        socketManager.readUnReadMessage(jsonObject)
    }

    private fun sendMessageChat() {
        val jsonObject = JSONObject()
        jsonObject.put("userid", getUser(requireContext())?.id)
        jsonObject.put("user2Id", user2Id)
        jsonObject.put("messageType", messageType)
        jsonObject.put("message", binding.etSendMsg.text.toString().trim())
        socketManager.sendMessageForChat(jsonObject)
    }

    private fun callToUser() {
        val jsonObject = JSONObject()
        jsonObject.put("senderName", getUser(requireContext())?.username)
        jsonObject.put("senderImage", getUser(requireContext())?.profileImage)
        jsonObject.put("senderId", getUser(requireContext())?.id)
        jsonObject.put("receiverId", user2Id)
        jsonObject.put("requestId", "0")
        jsonObject.put("receiverName", userName)
        jsonObject.put("receiverImage", receiverImage)
        jsonObject.put("callType", "0")     // (0=>for single call,1=>for group call)
        jsonObject.put("groupName", "singleCall")
        socketManager.callToUser(jsonObject)
        Log.e("========", jsonObject.toString())

    }

    override fun onResume() {
        super.onResume()
        saveChatString(requireActivity(), "true" + user2Id.toString())
        socketManager.unRegister(this)
        socketManager.onRegister(this)
        if (!socketManager.isConnected() || socketManager.getmSocket() == null) {
            socketManager.init()
        }
        socketManager.receiveMsgListener()
        getChatHistory()
        readUnreadMessage()

    }

    override fun onDestroy() {
        saveChatString(requireActivity(), "false")
        super.onDestroy()

    }

    override fun onPause() {
        saveChatString(requireActivity(), "false")

        super.onPause()
    }
    override fun onDestroyView() {
        saveChatString(requireActivity(), "false")
        super.onDestroyView()
    }
    override fun onResponseArray(event: String, args: JSONArray) {
        when(event){
            SocketManager.get_chat-> {
                try {
                    activityScope.launch {
                        val mObject = args as JSONArray

                        val gson = GsonBuilder().create()
                        val listChatHistory =
                            gson.fromJson(mObject.toString(), ChatHistoryResponse::class.java)

                        chatList.clear()
                        chatList.addAll(listChatHistory)
                        Log.e("====chatListSize",chatList.size.toString())
                        binding.tvUserName.text = userName
                        chatAdapter?.notifyDataSetChanged()
                        scrollToBottom()
                    }
                } catch (e: Exception) {

                }
            }
        }

    }

    override fun onResponse(event: String, args: JSONObject) {
        when(event){
            SocketManager.send_message->{
                try {
                    activityScope.launch {
                        val mObject = args as JSONObject

                        val gson = GsonBuilder().create()
                        val listChatHistory = gson.fromJson(
                            mObject.toString(),
                            ChatHistoryResponse.ChatHistoryResponseItem::class.java
                        )
                        chatList.add(listChatHistory)
                        chatAdapter?.notifyDataSetChanged()
                        binding.rvChat.smoothScrollToPosition(chatList.size - 1)
                        binding.tvUserName.text = userName

                    }
                } catch (e: Exception) {

                }
            }

            SocketManager.send_message_listener->{
                try {
                    activityScope.launch {
                        val mObject = args as JSONObject

                        val gson = GsonBuilder().create()
                        val listChatHistory = gson.fromJson(
                            mObject.toString(),
                            ChatHistoryResponse.ChatHistoryResponseItem::class.java
                        )
                        chatList.add(listChatHistory)
                        chatAdapter?.notifyDataSetChanged()
                        binding.rvChat.smoothScrollToPosition(chatList.size - 1)
                        binding.tvUserName.text = userName

                    }
                } catch (e: Exception) {

                }
            }

            SocketManager.call_to_user_emitter -> {
                try {
                        activityScope.launch {
                            val mObject = args as JSONObject
                            val gson = GsonBuilder().create()

                            val userToCallList = gson.fromJson(mObject.toString(), VideoCallResponse::class.java)

                            val intent = Intent(requireContext(), SingleVideoCallActivity::class.java)
                            intent.putExtra("channel_name", userToCallList.channelName)
                            intent.putExtra("video_token", userToCallList.videoToken)
                            intent.putExtra("senderId", user2Id.toString())
                            intent.putExtra("groupName",userToCallList.groupName)
                            intent.putExtra("type","chat")

                            startActivity(intent)
                            Log.e("=====chat",userToCallList.channelName+"====="+userToCallList.videoToken)
                    }
                } catch (e: Exception) {

                }
            }
        }

    }

    override fun onError(event: String, vararg args: Array<*>) {

    }

    override fun selectedImage(imagePath: String?, code: Int) {
        if (!imagePath.isNullOrEmpty()) {
            imageFilePath = imagePath

            try {
                extension = imageFilePath.substring(imageFilePath.lastIndexOf(".") + 1); // Without dot jpg, png
            } catch (e: Exception) {
            }

            val imgBase64 = getBase64FromPath(imageFilePath)
            val jsonObject = JSONObject()
            jsonObject.put("userid", getUser(requireContext())?.id)
            jsonObject.put("user2Id", user2Id)
            jsonObject.put("messageType", 1)
            jsonObject.put("message", imgBase64)
            jsonObject.put("extension", extension)
            socketManager.sendMessageForChat(jsonObject)

        }
    }

}