package com.app.shotclock.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.MessagesAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.cache.getUser
import com.app.shotclock.databinding.FragmentMessageBinding
import com.app.shotclock.models.sockets.GetChatListModel
import com.app.shotclock.utils.App
import com.app.shotclock.utils.SocketManager
import com.app.shotclock.utils.hideKeyboard
import com.app.shotclock.utils.isVisible
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class MessageFragment : BaseFragment<FragmentMessageBinding>(), SocketManager.Observer {

    private var messageAdapter: MessagesAdapter? = null
    private var getChatList = ArrayList<GetChatListModel.GetChatListModelItem>()
    private val activityScope = CoroutineScope(Dispatchers.Main)
    private lateinit var socketManager: SocketManager

    override fun getViewBinding(): FragmentMessageBinding {
        return FragmentMessageBinding.inflate(layoutInflater)
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        socketManager = App.mInstance.getSocketManager()!!
        if (!socketManager.isConnected() || socketManager.getmSocket() == null) {
            socketManager.init()
        }
        setAdapter()
        handleClicks()
        getChatList()

    }


    private fun setAdapter() {
        messageAdapter = MessagesAdapter(requireContext(), getChatList)
        binding.rvMessages.adapter = messageAdapter

        messageAdapter?.onItemClickListener = { pos ,user2Id->
            val bundle= Bundle()
            bundle.putInt("userId",getChatList[pos].userOne)
            bundle.putInt("user2Id",getChatList[pos].userTwo)
            bundle.putString("username",getChatList[pos].userName)
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
        getChatList()
    }

    override fun onResponseArray(event: String, args: JSONArray) {
        try {
            activityScope.launch {
                val mObject = args as JSONArray
                val gson = GsonBuilder().create()
                val listChat = gson.fromJson(mObject.toString(), GetChatListModel::class.java)

                getChatList.clear()
                getChatList.addAll(listChat)
                messageAdapter?.notifyDataSetChanged()
            }
        } catch (e: Exception) {

        }
    }

    override fun onResponse(event: String, args: JSONObject) {


    }

    override fun onError(event: String, vararg args: Array<*>) {
    }

}