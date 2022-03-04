package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.shotclock.R
import com.app.shotclock.adapters.ChatAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentChatBinding
import com.app.shotclock.models.sockets.ChatHistoryResponse
import com.app.shotclock.utils.App
import com.app.shotclock.utils.SocketManager
import com.app.shotclock.utils.isVisible
import com.app.shotclock.utils.showToast
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class ChatFragment : BaseFragment<FragmentChatBinding>(), SocketManager.Observer {

    private var chatAdapter: ChatAdapter? = null
    private val activityScope = CoroutineScope(Dispatchers.Main)
    private lateinit var socketManager: SocketManager
    private var linearLayoutManager: LinearLayoutManager? = null
    private var userId = 0
    private var user2Id = 0
    private var userName = ""
    private var messageType = 0  // 0-text, 1-image
    private var message = ""
    private var chatList = ArrayList<ChatHistoryResponse.ChatHistoryResponseItem>()


    override fun getViewBinding(): FragmentChatBinding {
        return FragmentChatBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        user2Id = bundle?.getInt("user2Id")!!
        userId = bundle.getInt("userId")
        userName = bundle.getString("username")!!
        socketManager = App.mInstance.getSocketManager()!!

        if (!socketManager.isConnected() || socketManager.getmSocket() == null)
            socketManager.init()

        clickHandle()
        setAdapter()

        getChatHistory()
        readUnreadMessage()
    }

    private fun setAdapter() {

        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        chatAdapter = ChatAdapter(requireContext(), chatList)
        binding.rvChat.layoutManager = linearLayoutManager
        binding.rvChat.adapter = chatAdapter

//        scrollToPosition(items.size() - 1)
        binding.rvChat.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) scrollToBottom()
        }

    }

    private fun scrollToBottom() {
        linearLayoutManager?.smoothScrollToPosition(binding.rvChat, null, chatAdapter?.itemCount!!)
    }

    private fun clickHandle() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivVideoCall.isVisible()
        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.tb.ivVideoCall.setOnClickListener {
            this.findNavController().navigate(R.id.action_chatFragment_to_videoCallFragment)
        }

        binding.ivSend.setOnClickListener {
            // for send text
            if (messageType == 0) {
                if (binding.etSendMsg.text.toString().trim().isEmpty()) {
                    showToast("Please enter something")
                } else {
                    sendMessageChat()
                    binding.etSendMsg.text.clear()
                }
            } else {
                // for send images
                binding.ivAttachment.setOnClickListener {
//                openImagePopUp(message,requireContext())
                }
            }


        }


    }

    private fun getChatHistory() {
        val jsonObject = JSONObject()
//        val userid = getUser(requireContext())?.id.toString()
        jsonObject.put("userid", userId)
        jsonObject.put("user2Id", user2Id)
        socketManager.getFriendChat(jsonObject)

    }

    private fun readUnreadMessage() {
        val jsonObject = JSONObject()
        jsonObject.put("user2Id", user2Id)
        jsonObject.put("userid", userId)
        socketManager.readUnReadMessage(jsonObject)
    }

    private fun sendMessageChat() {
        val jsonObject = JSONObject()
        jsonObject.put("userid", userId)
        jsonObject.put("user2Id", user2Id)
        jsonObject.put("messageType", messageType)
        jsonObject.put("message", binding.etSendMsg.text.toString().trim())
        socketManager.sendMessageForChat(jsonObject)
    }


    override fun onResume() {
        super.onResume()

        socketManager.unRegister(this)
        socketManager.onRegister(this)
        if (!socketManager.isConnected() || socketManager.getmSocket() == null) {
            socketManager.init()
        }
        getChatHistory()
        readUnreadMessage()
    }

    override fun onResponseArray(event: String, args: JSONArray) {

        try {
            activityScope.launch {
                val mObject = args as JSONArray

                val gson = GsonBuilder().create()
                val listChatHistory =
                    gson.fromJson(mObject.toString(), ChatHistoryResponse::class.java)

                chatList.clear()
                chatList.addAll(listChatHistory)
                binding.tvUserName.text = userName
                chatAdapter?.notifyDataSetChanged()
            }
        } catch (e: Exception) {

        }


    }

    override fun onResponse(event: String, args: JSONObject) {

    }

    override fun onError(event: String, vararg args: Array<*>) {

    }
}