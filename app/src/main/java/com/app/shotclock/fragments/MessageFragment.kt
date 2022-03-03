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
import com.app.shotclock.utils.SocketManager
import com.app.shotclock.utils.hideKeyboard
import com.app.shotclock.utils.isVisible
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject


class MessageFragment : BaseFragment<FragmentMessageBinding>(), SocketManager.SocketInterface {

    private var messageAdapter: MessagesAdapter? = null
    private var getChatList = ArrayList<GetChatListModel.GetChatListModelItem>()
    var socketManager: SocketManager? = null

    override fun getViewBinding(): FragmentMessageBinding {
        return FragmentMessageBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        handleClicks()
        /* SocketManager.onDisConnect()
         SocketManager.initSocket()*/

        getChatList()

    }


    private fun setAdapter() {
        messageAdapter = MessagesAdapter(requireContext(), getChatList)
        binding.rvMessages.adapter = messageAdapter

        messageAdapter?.onItemClickListener={pos ->
            findNavController().navigate(R.id.action_messageFragment_to_chatFragment)
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
        val jsonObject = JSONObject()
        jsonObject.put("userid", getUser(requireContext())?.id.toString())
        SocketManager.sendDataToServer(SocketManager.CHAT_LISTING, jsonObject)
    }


    override fun onSocketCall(event: String?, vararg args: Any?) {
        when (event) {
            SocketManager.CHAT_LISTING_LISTENER -> {
                requireActivity().runOnUiThread {
                    val mObject = (JSONArray(args[0]).get(0) as JSONArray)
//                    val mObject = (JSONArray(args[0]).get(0)) as JSONObject

                    val getChatListModel =
                        Gson().fromJson(mObject.toString(), GetChatListModel::class.java)

                    getChatList.clear()
                    getChatList.addAll(getChatListModel)
                    messageAdapter?.notifyDataSetChanged()


                    Log.e("getChatListing", getChatList.toString())
                }

            }

            SocketManager.READ_UNREAD_LISTENER -> {

            }
        }
    }

    override fun onSocketConnect(vararg args: Any?) {

    }

    override fun onSocketDisConnect(vararg args: Any?) {

    }

    override fun onError(event: String?, vararg args: Any?) {

    }

    override fun onResume() {
        super.onResume()
        SocketManager.onRegister(this)
    }

}