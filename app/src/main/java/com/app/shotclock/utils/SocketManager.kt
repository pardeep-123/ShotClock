package com.app.shotclock.utils

import android.util.Log
import com.app.shotclock.constants.ApiConstants
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.*

class SocketManager {

    private val connectUser = "connect_user"   //socket name
    private val connectListener = "connect_listener"  //listener

    private val sendMessage = "send_message"
    private val myChat1Listener = "body"

    private val getChatEvent = "get_chat"
    private val myChatListener = "my_chat"

    private val getChatListEvent = "get_chat_list"
    private val getListListener = "get_list"

    private val deleteChatEmitter = "delete_chat"
    private val deleteListener = "delete_data"

    private val clearChatEmitter = "clear_chat"
    private val clearListener = "clear_data"

    private val blockUnblockUserEmitter = "blockUnblock_user"
    private val blockDataListener = "block_data"

    private val getProfileDataEmitter = "get_profile_data"
    private val profileInformationListener = "profile_information"

    private val readUnreadEmitter = "read_unread"
    private val readDataStatusListener = "read_data_status"


    private var mSocket: Socket? = null
    private var observerList: MutableList<Observer>? = null

    private val errorMessage = "error_message"

    private fun isConnected(): Boolean {
        return mSocket != null && mSocket!!.connected()
    }

    fun init() {
        initializeSocket()
    }

    private fun getSocket(): Socket? {
        run {
            try {
                mSocket = IO.socket(ApiConstants.SOCKET_URL)
            } catch (e: URISyntaxException) {
                throw RuntimeException(e)
            }
        }
        return mSocket
    }

    fun onRegister(observer: Observer) {
        if (observerList != null && !requireNotNull(observerList?.contains(observer))) {
            observerList?.clear()
            observerList?.add(observer)
        } else {
            observerList = ArrayList()
            observerList?.clear()
            observerList?.add(observer)
        }
    }

    fun unRegister(observer: Observer) {
        observerList?.let { list ->
            for (i in list.indices) {
                val model = list[i]
                if (model === observer) {
                    observerList?.remove(model)
                }
            }
        }
    }

    private fun initializeSocket() {
        if (mSocket == null) {
            mSocket = getSocket()
        }
        if (observerList == null || observerList!!.size == 0) {
            observerList = ArrayList()
        }
        disconnect()
        mSocket!!.connect()
        mSocket!!.on(Socket.EVENT_CONNECT, onConnect)
        mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        mSocket!!.on(errorMessage, onErrorMessage)
    }

    private fun disconnect() {
        if (mSocket != null) {
            mSocket!!.off(Socket.EVENT_DISCONNECT, onDisconnect)
            mSocket!!.off(Socket.EVENT_CONNECT, onConnect)
            mSocket!!.off(Socket.EVENT_DISCONNECT, onDisconnect)
            mSocket!!.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
            mSocket!!.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
            mSocket!!.off()
            mSocket!!.disconnect()
        }
    }

    private val onDisconnect = Emitter.Listener { args ->
        try {
            Log.e("Socket", "DISCONNECTED :::$args")
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    private val onConnect = Emitter.Listener {
        Log.e("Socket", "CONNECTED")
        if (isConnected()) {
            try {
                Log.e("Socket", "want to go online")
                val jsonObject = JSONObject()
                // val loginStatus:Boolean= prefs?.getLoggedIn()!!
                //   if(loginStatus)
                //   {
                //     if(prefs?.getString(USER_ID) != "0")
                //    {
                jsonObject.put("userid", App.app?.getString("user_id"))
                mSocket!!.off(connectListener, onConnectListener)
                mSocket!!.on(connectListener, onConnectListener)
                mSocket!!.emit(connectUser, jsonObject)
                mSocket!!.off(myChat1Listener, onNewMessage)
                mSocket!!.on(myChat1Listener, onNewMessage)
//                        connectSingleChatSocket()
                //     }
                //}
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else {
            initializeSocket()
        }
    }

    private val onConnectError = Emitter.Listener { args ->
        try {
            Log.e("Socket", "CONNECTION ERROR :::$args")
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    private val onErrorMessage = Emitter.Listener { args ->
        for (observer in observerList!!) {
            try {
                val data = args[0] as JSONObject
                Log.e("Socket", "Error Message :::$data")
                observer.onError(connectUser, args)
            } catch (ex: Exception) {
                ex.localizedMessage
            }
        }
    }

    private val onConnectListener = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.e("ConnectListener", data.toString())
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    // function for get message list
    fun getMessageList(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            if (!mSocket!!.connected()) {
                mSocket!!.connect()
                mSocket!!.off(getListListener, onGetMessageList)
                mSocket!!.on(getListListener, onGetMessageList)
                mSocket!!.emit(getChatListEvent, jsonObject)
            } else {
                mSocket!!.off(getListListener, onGetMessageList)
                mSocket!!.on(getListListener, onGetMessageList)
                mSocket!!.emit(getChatListEvent, jsonObject)
            }
            Log.e("Socket", "Chat List Called")
        }
    }

    private val onGetMessageList = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONArray
            for (observer in observerList!!) {
                observer.onGetChatList(getListListener, data)
            }

            Log.e("Socket", "onGetChatRead ::$data")
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    // function for delete chat
    fun deleteChat(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            if (!mSocket!!.connected()) {
                mSocket!!.connect()
                mSocket!!.off(deleteListener, deletChatList)
                mSocket!!.on(deleteListener, deletChatList)
                mSocket!!.emit(deleteChatEmitter, jsonObject)
            } else {
                mSocket!!.off(deleteListener, deletChatList)
                mSocket!!.on(deleteListener, deletChatList)
                mSocket!!.emit(deleteChatEmitter, jsonObject)
            }
            Log.e("Socket", "delete List Called")
        }
    }

    private val deletChatList = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            for (observer in observerList!!) {
                observer.onResponse(deleteListener, data)
            }
            Log.e("DeleteSocket", "onGetChatRead ::$data")
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    //   function for get chatting list
    fun getSingleChatList(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            if (!mSocket!!.connected()) {
                mSocket!!.connect()
                mSocket!!.emit(getChatEvent, jsonObject)
                mSocket!!.off(myChatListener, onGetChatList)
                mSocket!!.on(myChatListener, onGetChatList)
                mSocket!!.off(myChat1Listener)
                mSocket!!.on(myChat1Listener, onNewMessage)
            } else {
                mSocket!!.emit(getChatEvent, jsonObject)
                mSocket!!.off(myChatListener, onGetChatList)
                mSocket!!.on(myChatListener, onGetChatList)
                mSocket!!.off(myChat1Listener)
                mSocket!!.on(myChat1Listener, onNewMessage)
            }
            Log.e("Socket", "Chat List Called")
        }
    }

    private val onGetChatList = Emitter.Listener { args ->
        try {
            // val data = args[0] as JSONObject
            val data = args[0] as JSONArray
            for (observer in observerList!!) {
                //   observer.onResponse(myChatListener, data)
                observer.onGetChatList(myChatListener, data)
            }

            Log.e("Socket", "onGetChatRead ::$data")
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    //    function for send message to sender
    fun sendMessage(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            try {
                if (!mSocket!!.connected()) {
                    mSocket!!.connect()
                    mSocket!!.emit(sendMessage, jsonObject)
                    mSocket!!.off(myChat1Listener)
                    mSocket!!.on(myChat1Listener, onNewMessage)
                } else {
                    mSocket!!.emit(sendMessage, jsonObject)
                    mSocket!!.off(myChat1Listener)
                    mSocket!!.on(myChat1Listener, onNewMessage)
                }
            } catch (ex: Exception) {
                ex.localizedMessage
            }

            Log.e("Socket", "Send Message Called")
        }
    }

    private val onNewMessage = Emitter.Listener { args ->
        Log.e("Socket", "ON_NEW_MESSAGE")
        try {
            val data = args[0] as JSONObject
            Log.e("Socket", "New Message :::$data")
            for (observer in observerList!!) {
                observer.onResponse(myChat1Listener, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    // function for clear chat
    fun clearChat(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            if (!mSocket!!.connected()) {
                mSocket!!.connect()
                mSocket!!.off(clearListener, clearChatList)
                mSocket!!.on(clearListener, clearChatList)
                mSocket!!.emit(clearChatEmitter, jsonObject)
            } else {
                mSocket!!.off(clearListener, clearChatList)
                mSocket!!.on(clearListener, clearChatList)
                mSocket!!.emit(clearChatEmitter, jsonObject)
            }
            Log.e("DeleteSocket", "delete List Called")
        }
    }

    private val clearChatList = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            for (observer in observerList!!) {
                observer.onCheckResponse(clearListener, data)
            }
            Log.e("ClaerSocket", "onGetChatRead ::$data")
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    // function for block unblock user
    fun blockUnblockUser(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            if (!mSocket!!.connected()) {
                mSocket!!.off(blockDataListener, blockUnblockUser)
                mSocket!!.on(blockDataListener, blockUnblockUser)
                mSocket!!.emit(blockUnblockUserEmitter, jsonObject)
            } else {
                mSocket!!.off(blockDataListener, blockUnblockUser)
                mSocket!!.on(blockDataListener, blockUnblockUser)
                mSocket!!.emit(blockUnblockUserEmitter, jsonObject)
            }
            Log.e("BlockUnBlockUser", "block list Called")
        }
    }

    private val blockUnblockUser = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            for (observer in observerList!!) {
                observer.onCheckResponse(blockDataListener, data)
            }
            Log.e(" ", "onBlockUnblock ::$data")

        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    // function for get profile data
    fun getProfileData(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            if (!mSocket!!.connected()) {
                mSocket!!.off(profileInformationListener, getProfileData)
                mSocket!!.on(profileInformationListener, getProfileData)
                mSocket!!.emit(getProfileDataEmitter, jsonObject)
            } else {
                mSocket!!.off(profileInformationListener, getProfileData)
                mSocket!!.on(profileInformationListener, getProfileData)
                mSocket!!.emit(getProfileDataEmitter, jsonObject)
            }
            Log.e("ProfileInformation", "profile list Called")
        }
    }

    private val getProfileData = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            for (observer in observerList!!) {
                observer.onCheckResponse(profileInformationListener, data)
            }
            Log.e("getProfileInformation", "profileInformationListener::  $data")
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    // function for read unread status
    fun readUnreadStatus(jsonObject: JSONObject?) {
        if (jsonObject != null)
            if (!mSocket!!.connected()) {
                mSocket!!.off(readDataStatusListener, readDataStatus)
                mSocket!!.on(readDataStatusListener, readDataStatus)
                mSocket!!.emit(readUnreadEmitter, jsonObject)
            } else {
                mSocket!!.off(readDataStatusListener, readDataStatus)
                mSocket!!.on(readDataStatusListener, readDataStatus)
                mSocket!!.emit(readUnreadEmitter, jsonObject)
            }
        Log.e("ReadUnreadStatus", "read unread list Called")
    }

    private val readDataStatus = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            for (observer in observerList!!) {
                observer.onResponse(readDataStatusListener, data)
            }
            Log.e("readDataStatusListener", "ReadDataStatus::  $data")
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    fun connectSingleChatSocket() {
        if (mSocket!!.connected()) {
            mSocket!!.off(myChatListener, onNewMessage)
            mSocket!!.on(myChatListener, onNewMessage)
        } else {
            mSocket!!.connect()
            mSocket!!.off(myChatListener, onNewMessage)
            mSocket!!.on(myChatListener, onNewMessage)
        }
        Log.e("Socket", "single chat")
    }

    interface Observer {
        fun onResponse(listener: String, args: JSONObject)
        fun onCheckResponse(listener: String, args: JSONObject)
        fun onGetChatList(listener: String, args: JSONArray)
        fun onError(listener: String, vararg args: Array<*>)
    }
}