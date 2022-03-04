package com.app.shotclock.utils

import android.util.Log
import com.app.shotclock.cache.getUser
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
    // sockets listener
    private val errorMessage = "error_message"

    companion object {
        //for chat
        //emitter
        val get_chat_list_count = "get_chat_list_count"

        //listener
        val send_message_listner = "send_message_listner"
        val read_data_status = "read_data_status"
        val get_list_count = "get_list_count"
        val call_termination_listener = "call_termination_listener"

        // sockets events
        private const val connectUser = "connect_user"
        val get_chat_list = "chat_listing"
        val get_chat = "get_messages"
        val send_message = "send_messages"
        val read_unread_emitter = "read_unread"
        val connect_call_user = "connect_call_user"
        val call_request_action = "call_request_action"
        val connect_call_listener = "connect_call_listener"
        val call_request_action_listener= "call_request_action_listener"
        val call_request_action_response_listener= "call_request_action_response_listener"
        val call_request_recevied= "call_request_recevied"
        val call_termination_event = "call_termination"



        /*listener*/
        private val connect_listener = "connect_listener"
        private val get_list = "chatlistinglistner"
        val my_chat = "get_messagelistner"
        val read_unread_listener = "read_unreadlistner"

        private val new_message = "new_message"
        val receive_message = "receive_message"


        val call_request_expiry_listener = "call_request_expiry"  //event  && listener
        val call__expiry_listener = "call_request_expiry_listener"  //event  && listener

        val deliveryStatusListener = "delivery_status"
        val disconnectUserEmitter = "disconnect_user"
        val disconnectListener = "disconnect_listener"

    }


    private var mSocket: Socket? = null
    private var observerList: MutableList<Observer>? = null

    fun getmSocket(): Socket? {
        return mSocket
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
        if (observerList != null && !observerList!!.contains(observer)) {
            observerList!!.clear()
            observerList!!.add(observer)
        } else {
            observerList = ArrayList()
            observerList!!.clear()
            observerList!!.add(observer)
        }
    }

    fun unRegister(observer: Observer) {
        observerList?.let { list ->
            for (i in 0 until list.size - 1) {
                val model = list[i]
                if (model === observer) {
                    observerList?.remove(model)
                }
            }
        }
    }


    fun init() {
        initializeSocket()
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
        // mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        // mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        mSocket!!.on(errorMessage, onErrorMessage)

       /* mSocket!!.off(deliveryStatusListener)
        mSocket!!.on(deliveryStatusListener, onGettingMessageUpdate)*/

    }

    private fun disconnect() {
        if (mSocket != null) {
            // mSocket!!.off(Socket.EVENT_DISCONNECT, onDisconnect)
            mSocket!!.off(Socket.EVENT_CONNECT, onConnect)
            mSocket!!.off(Socket.EVENT_DISCONNECT, onDisconnect)
            mSocket!!.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
            // mSocket!!.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
            mSocket!!.off()
            mSocket!!.disconnect()
        }
    }

    fun disconnectAll() {
        try {
            if (mSocket != null) {
                // mSocket!!.emit(Socket.EVENT_DISCONNECT, onDisconnect)
                mSocket!!.off(Socket.EVENT_CONNECT, onConnect)
                mSocket!!.off(Socket.EVENT_DISCONNECT, onDisconnect)
                mSocket!!.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
                //mSocket!!.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
                mSocket!!.off()
                mSocket!!.disconnect()
            }
        } catch (e: Exception) {
        }
    }


    private val onConnect = Emitter.Listener {
        if (isConnected()) {
            try {
                val jsonObject = JSONObject()
                if(getUser(App.context!!)!=null){
                    val userid = getUser(App.context!!)?.id!!

                    if (userid.toInt() != 0) {
                        jsonObject.put("user_id", userid.toInt())
                        mSocket!!.off(connect_listener, onConnectListener)
                        mSocket!!.on(connect_listener, onConnectListener)
                        mSocket!!.emit(connectUser, jsonObject)

                    }
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else {
            initializeSocket()
        }
    }


    private val onDisconnectLogoutListener = Emitter.Listener { args ->
        try {

            Log.e("Socket", "Disconnected" + args[0].toString())

            Log.e("Socket", "Disconnected" + args[0].toString())

            // val data = args[1] as JSONObject

            // val data = args[1] as JSONObject
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    fun isConnected(): Boolean {
        return mSocket != null && mSocket!!.connected()
    }

    private val onConnectListener = Emitter.Listener { args ->
        try {

            Log.e("Socket", "Connected" + args[0].toString())

            Log.e("Socket", "Connected" + args[0].toString())

            // val data = args[1] as JSONObject

            // val data = args[1] as JSONObject
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    private val onDisconnect = Emitter.Listener { args ->
        try {
            Log.e("Socket", "DISCONNECTED :::$args")
        } catch (ex: Exception) {
            ex.localizedMessage
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


    fun readUnReadMessage(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            try {
                if (!mSocket!!.connected()) {
                    mSocket!!.connect()
                    mSocket!!.off(read_unread_listener)
                    mSocket!!.on(read_unread_listener, onReadDataStatus)
                    mSocket!!.emit(read_unread_emitter, jsonObject)
                } else {
                    mSocket!!.off(read_unread_listener)
                    mSocket!!.on(read_unread_listener, onReadDataStatus)
                    mSocket!!.emit(read_unread_emitter, jsonObject)
                }
            } catch (ex: Exception) {
                ex.localizedMessage
            }

            Log.i("Socket", "Send Message Called")
        }
    }

    private val onReadDataStatus = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.e("Socket", "read_unread :::$data")
            for (observer in observerList!!) {
                observer.onResponse(read_data_status, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    fun acceptRejectCall(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            try {
                if (!mSocket!!.connected()) {
                    mSocket!!.connect()
                    mSocket!!.off(call_request_action_listener)
                    mSocket!!.on(call_request_action_listener, onCallResponseListener)
                    mSocket!!.emit(call_request_action, jsonObject)
                } else {
                    mSocket!!.off(call_request_action_listener)
                    mSocket!!.on(call_request_action_listener, onCallResponseListener)
                    mSocket!!.emit(call_request_action, jsonObject)
                }
            } catch (ex: Exception) {
                ex.localizedMessage
            }

            Log.i("Socket", "call accept reject")
        }
    }

    fun onLogoutDisconnect(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            try {
                if (!mSocket!!.connected()) {
                    mSocket!!.connect()
                    mSocket!!.off(disconnectListener, onDisconnectLogoutListener)
                    mSocket!!.on(disconnectListener, onDisconnectLogoutListener)
                    mSocket!!.emit(disconnectUserEmitter, jsonObject)
                } else {
                    mSocket!!.off(disconnectListener, onDisconnectLogoutListener)
                    mSocket!!.on(disconnectListener, onDisconnectLogoutListener)
                    mSocket!!.emit(disconnectUserEmitter, jsonObject)
                }
            } catch (ex: Exception) {
                ex.localizedMessage
            }

            Log.i("Socket", "call accept reject")
        }
    }



    fun callTermination(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            try {
                if (!mSocket!!.connected()) {
                    mSocket!!.connect()
                    mSocket!!.off(call_termination_listener)
                    mSocket!!.on(call_termination_listener, onCallTerminateListener)
                    mSocket!!.emit(call_termination_event, jsonObject)
                } else {
                    mSocket!!.off(call_termination_listener)
                    mSocket!!.on(call_termination_listener, onCallTerminateListener)
                    mSocket!!.emit(call_termination_event, jsonObject)
                }
            } catch (ex: Exception) {
                ex.localizedMessage
            }
        }
    }



    fun connectCall(jsonObject: JSONObject?){
        if (jsonObject != null) {
            try {
                if (!mSocket!!.connected()) {
                    mSocket!!.connect()
                    mSocket!!.off(connect_call_listener)
                    mSocket!!.on(connect_call_listener, onConnectCallListener)
                    mSocket!!.emit(connect_call_user, jsonObject)
                } else {
                    mSocket!!.off(connect_call_listener)
                    mSocket!!.on(connect_call_listener, onConnectCallListener)
                    mSocket!!.emit(connect_call_user, jsonObject)
                }
            } catch (ex: Exception) {
            } catch (ex: Exception) {
                ex.localizedMessage
            }

            Log.i("Socket", "Send Message Called")
        }
    }


    fun sendMessage(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            try {
                if (!mSocket!!.connected()) {
                    mSocket!!.connect()
                    mSocket!!.off(new_message)
                    mSocket!!.on(new_message, onNewMessage)
                    mSocket!!.emit(send_message, jsonObject)
                } else {
                    mSocket!!.off(new_message)
                    mSocket!!.on(new_message, onNewMessage)
                    mSocket!!.emit(send_message, jsonObject)
                }
            } catch (ex: Exception) {
                ex.localizedMessage
            }

            Log.i("Socket", "Send Message Called")
        }
    }


    private val onNewMessage = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.e("Socket", "onNewMessage :::$data")
            for (observer in observerList!!) {
                observer.onResponse(send_message, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    private val onConnectCallListener = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.e("Socket", "onNewMessage :::$data")
            for (observer in observerList!!) {
                observer.onResponse(connect_call_listener, data)
            }
        } catch (ex: Exception) {
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }


    fun getChatListCount(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            try {
                if (!mSocket!!.connected()) {
                    mSocket!!.connect()
                    mSocket!!.off(get_list_count)
                    mSocket!!.on(get_list_count, onGetChatListCount)
                    mSocket!!.emit(get_chat_list_count, jsonObject)
                } else {
                    mSocket!!.off(get_list_count)
                    mSocket!!.on(get_list_count, onGetChatListCount)
                    mSocket!!.emit(get_chat_list_count, jsonObject)
                }
            } catch (ex: Exception) {
                ex.localizedMessage
            }

            Log.i("Socket", "Send Message Called")
        }
    }



    private val onGetChatListCount = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.e("Socket", "read_unread :::$data")
            for (observer in observerList!!) {
                observer.onResponse(get_chat_list_count, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    fun callTerminateListeners() {

        try {
            if (!mSocket!!.connected()) {
                mSocket!!.connect()
                mSocket!!.off(call_termination_listener)
                mSocket!!.on(call_termination_listener, onCallTerminateEventListener)

            } else {
                mSocket!!.off(call_termination_listener)
                mSocket!!.on(call_termination_listener, onCallTerminateEventListener)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }

        Log.i("Socket", "received Message Called")

    }


    private val onReadMessage = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.e("Socket", "read_message :::$data")
            for (observer in observerList!!) {
                observer.onResponse(read_unread_listener, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }


    private val onReceivedMessage = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.e("Socket", "onGetFriendChat :::$data")
            for (observer in observerList!!) {
                observer.onResponse(receive_message, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    private val onVideoCallListener = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.e("Socket", "onVideoCallListener :::$data")
            for (observer in observerList!!) {
                observer.onResponse(call_request_action_response_listener, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    private val onCallReqListener = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.e("Socket", "onVideoCallListener :::$data")
            for (observer in observerList!!) {
                observer.onResponse(call_request_recevied, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }


    fun getFriendChat(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            try {
                if (!mSocket!!.connected()) {
                    mSocket!!.connect()
                    mSocket!!.off(my_chat)
                    mSocket!!.on(my_chat, onGetFriendChat)
                    mSocket!!.emit(get_chat, jsonObject)
                } else {
                    mSocket!!.off(my_chat)
                    mSocket!!.on(my_chat, onGetFriendChat)
                    mSocket!!.emit(get_chat, jsonObject)
                }
            } catch (ex: Exception) {
                ex.localizedMessage
            }

            Log.i("Socket", "Send Message Called")
        }
    }

    private val onGetFriendChat = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONArray
            Log.e("Socket", "onGetFriendChat :::$data")
            for (observer in observerList!!) {
                observer.onResponseArray(get_chat, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }




    private val onCallResponseListener = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.e("Socket", "call_response :::$data")
            for (observer in observerList!!) {
                observer.onResponse(call_request_action_listener, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    private val onCallTerminateListener = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.e("Socket", "call_response :::$data")
            for (observer in observerList!!) {
                observer.onResponse(call_termination_event, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }

    private val onCallTerminateEventListener = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.e("Socket", "call_response :::$data")
            for (observer in observerList!!) {
                observer.onResponse(call_termination_listener, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }


    fun sendMessageForChat(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            try {
                if (!mSocket!!.connected()) {
                    mSocket!!.connect()
                    mSocket!!.off(send_message_listner)
                    mSocket!!.on(send_message_listner, onSendMessageListenerForChat)
                    mSocket!!.emit(send_message, jsonObject)
                } else {
                    mSocket!!.off(send_message_listner)
                    mSocket!!.on(send_message_listner, onSendMessageListenerForChat)
                    mSocket!!.emit(send_message, jsonObject)
                }
            } catch (ex: Exception) {
                ex.localizedMessage
            }

            Log.i("Socket", "Send Message Called")
        }
    }

    fun sendMessageListenerForActivate() {
        try {
            if (!mSocket!!.connected()) {
                mSocket!!.connect()
                mSocket!!.off(send_message_listner)
                mSocket!!.on(send_message_listner, onSendMessageListenerForChat)
            } else {
                mSocket!!.off(send_message_listner)
                mSocket!!.on(send_message_listner, onSendMessageListenerForChat)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }

    }

    private val onSendMessageListenerForChat = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.e("Socket", "sendMessage :::$data")
            for (observer in observerList!!) {
                observer.onResponse(send_message, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }


    fun getChatList(jsonObject: JSONObject?) {
        if (jsonObject != null) {
            try {
                if (!mSocket!!.connected()) {
                    mSocket!!.connect()
                    mSocket!!.off(get_list)
                    mSocket!!.on(get_list, chatListListener)
                    mSocket!!.emit(get_chat_list, jsonObject)
                } else {
                    mSocket!!.off(get_list)
                    mSocket!!.on(get_list, chatListListener)
                    mSocket!!.emit(get_chat_list, jsonObject)
                }
            } catch (ex: Exception) {
                ex.localizedMessage
            }

            Log.i("Socket", "Send Message Called")
        }
    }

    private val chatListListener = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONArray
            Log.e("Socket", "chatList :::$data")
            for (observer in observerList!!) {
                observer.onResponseArray(get_chat_list, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }


    private val onGettingMessageUpdate = Emitter.Listener { args ->
        Log.e("Socket", "ON_GETTING_UPDATE")
        try {
            val data = args[0] as JSONObject
            Log.e("Socket", "Message Update :::$data")
            for (observer in observerList!!) {
                observer.onResponse(deliveryStatusListener, data)
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }
    }


    interface Observer {
        fun onResponseArray(event: String, args: JSONArray)
        fun onResponse(event: String, args: JSONObject)
        fun onError(event: String, vararg args: Array<*>)
    }


}