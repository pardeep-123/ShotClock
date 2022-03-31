package com.app.shotclock.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.MessagesAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.cache.getUser
import com.app.shotclock.databinding.FragmentMessageBinding
import com.app.shotclock.models.sockets.GetChatListModel
import com.app.shotclock.models.sockets.VideoCallResponse
import com.app.shotclock.utils.*
import com.app.shotclock.videocallingactivity.VideoCallActivity
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class MessageFragment : BaseFragment<FragmentMessageBinding>(), SocketManager.Observer,TextWatcher {

    private var messageAdapter: MessagesAdapter? = null
    private var getChatList = ArrayList<GetChatListModel.GetChatListModelItem>()
    private val activityScope = CoroutineScope(Dispatchers.Main)
    private lateinit var socketManager: SocketManager

    companion object {
        lateinit var txtMessage: TextView
    }


    override fun getViewBinding(): FragmentMessageBinding {
        return FragmentMessageBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        socketManager = App.mInstance.getSocketManager()!!
        if (!socketManager.isConnected() || socketManager.getmSocket() == null) {
            socketManager.init()
        }
        txtMessage = view.findViewById(R.id.tvNoChatHistory)
        setAdapter()
        handleClicks()
//        getChatList()

    }


    private fun setAdapter() {
        messageAdapter = MessagesAdapter(requireContext(), getChatList)
        binding.rvMessages.adapter = messageAdapter

        messageAdapter?.onItemClickListener = { pos ->
            val bundle= Bundle()
            if (getChatList[pos].userTwo == getUser(requireContext())?.id){
                bundle.putInt("user2Id",getChatList[pos].userOne)
            }else{
                bundle.putInt("user2Id",getChatList[pos].userTwo)
            }

            bundle.putString("username",getChatList[pos].userName)
            bundle.putString("receiverImage",getChatList[pos].userImage)
            findNavController().navigate(R.id.action_messageFragment_to_chatFragment,bundle)
        }

    }

    private fun handleClicks() {
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivMenu.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
            hideKeyboard(it, requireActivity())
        }

            // for edit text search
        binding.etSearch.addTextChangedListener(this)

    }

    private fun getChatList() {
        val jsonObjects = JSONObject()
        val userid = getUser(requireContext())?.id.toString()
        jsonObjects.put("userid", userid)
        socketManager.getChatList(jsonObjects)
    }


    override fun onResume() {
        super.onResume()
        socketManager.unRegister(this)
        socketManager.onRegister(this)

        if (!socketManager.isConnected() || socketManager.getmSocket() == null) {
            socketManager.init()
        }

        socketManager.callToUserActivate()
        messageAdapter?.arrayList = getChatList
        //messageAdapter?.notifyDataSetChanged()
        if (getChatList.isNotEmpty()) {
            binding.tvNoChatHistory.isGone()
        } else {
            binding.tvNoChatHistory.isVisible()
        }

        getChatList()
    }

    override fun onResponseArray(event: String, args: JSONArray) {
        try {
            activityScope.launch {
                val mObject = args as JSONArray
                val gson = GsonBuilder().create()
                val listChat = gson.fromJson(mObject.toString(), GetChatListModel::class.java)

//                val filteredList = listChat.filter { it.userTwo != getUser(requireContext())?.id }
                getChatList.clear()
                getChatList.addAll(listChat)
                messageAdapter?.notifyDataSetChanged()
                if (getChatList.isEmpty())
                    txtMessage.isVisible()
                else
                    txtMessage.isGone()
            }
        } catch (e: Exception) {

        }
    }

    override fun onResponse(event: String, args: JSONObject) {

        when(event){
            SocketManager.call_to_user_listener -> {
                try {
                    activityScope.launch {
                        val mObject = args as JSONObject
                        val gson = GsonBuilder().create()

                        val userToCallList = gson.fromJson(mObject.toString(), VideoCallResponse::class.java)
//                        val bundle = Bundle()
//                        bundle.putString("channel_name", userToCallList.channelName)
//                        bundle.putString("video_token", userToCallList.videoToken)
//                        findNavController().navigate(R.id.videoCallFragment, bundle)

                        val intent = Intent(requireContext(), VideoCallActivity::class.java)
                        intent.putExtra("channel_name", userToCallList.channelName)
                        intent.putExtra("video_token", userToCallList.videoToken)
//                        Log.e("=====message",userToCallList.channelName+"====="+userToCallList.videoToken)
                        startActivity(intent)
                    }
                } catch (e: Exception) {

                }
            }

        }

    }

    override fun onError(event: String, vararg args: Array<*>) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        messageAdapter?.filter?.filter(s.toString().trim())
    }

    override fun afterTextChanged(s: Editable?) {

    }

}