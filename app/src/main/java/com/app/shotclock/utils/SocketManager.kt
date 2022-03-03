package com.app.shotclock.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.app.shotclock.cache.getUser
import com.app.shotclock.constants.ApiConstants
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.*

object SocketManager {

    private var mSocket: Socket? = null
    private var observerList: MutableList<SocketInterface>? = null
    private var isConnect = false
    private val TAG = SocketManager::class.java.canonicalName


    //**********************EVENT***********************
    private const val CONNECT_USER = "connect_user"  //event
    const val SEND_MSG = "send_messages"
    const val GET_MESSAGE = "get_messages"
    const val CHAT_LISTING = "chat_listing"
    const val READ_UNREAD = "read_unread"


    //************************LISTENER***********************
    private const val CONNECT_LISTENER = "connect_listener"  //listener
    const val SEND_MSG_LISTENER = "send_message_listner"
    const val GET_MSG_LISTENER = "get_messagelistner"
    const val CHAT_LISTING_LISTENER = "chatlistinglistner"
    const val READ_UNREAD_LISTENER = "read_unreadlistner"


    init {
        try {
            createSocketOptions()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun initSocket() {
        try {
            createSocketOptions()
            onConnect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun isConnected(): Boolean {
        return isConnect
    }

    private fun createSocketOptions() {
        val opts = IO.Options()

        opts.reconnection = true
        mSocket = IO.socket(ApiConstants.SOCKET_URL, opts)
        if (observerList == null || observerList!!.size == 0) {
            observerList = ArrayList()
        }
    }

    fun onConnect() {
        if (mSocket == null)
            return
        if (!mSocket!!.connected()) {
            mSocket!!.on(Socket.EVENT_CONNECT, onConnectListener)
            mSocket!!.on(Socket.EVENT_DISCONNECT, onDisConnect)
            mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
//            mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
            mSocket!!.on(CONNECT_LISTENER, onConnectUserListener)
            mSocket!!.on(SEND_MSG_LISTENER, onSendMsgListener)
            mSocket!!.on(GET_MSG_LISTENER, onGetMsgListener)
            mSocket!!.on(CHAT_LISTING_LISTENER, onChatListingListener)
            mSocket!!.on(READ_UNREAD_LISTENER, onReadUnreadListener)

            mSocket!!.connect()
        } else {
            Log.e("info", "Socket connected: ${mSocket?.id()}")
        }
    }

    /**
     * Call this method in onPause and onDestroy
     */
    fun onDisConnect() {
        Log.e("onDisConnect", "Socket disConnected: ${mSocket?.id()}")
        if (mSocket == null)
            return
        isConnect = false
        mSocket!!.off(Socket.EVENT_CONNECT, onConnectListener)
        mSocket!!.off(Socket.EVENT_DISCONNECT, onDisConnect)
        mSocket!!.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket!!.off(CONNECT_LISTENER, onConnectUserListener)
        mSocket!!.off(SEND_MSG_LISTENER, onSendMsgListener)
        mSocket!!.off(GET_MSG_LISTENER, onGetMsgListener)
        mSocket!!.off(CHAT_LISTING_LISTENER, onChatListingListener)
        mSocket!!.off(READ_UNREAD_LISTENER,onReadUnreadListener)

        mSocket!!.disconnect()
    }

    /*
     * Send Data to server by use of socket
     * */
    fun sendDataToServer(methodName: String?, mObject: Any) {
        // Get a handler that can be used to post to the main thread
        Handler(Looper.getMainLooper()).post {
            mSocket?.emit(methodName, mObject)
            Log.e(methodName, mObject.toString())
        }
    }


    private val onConnectUserListener = Emitter.Listener { args ->
        // Get a handler that can be used to post to the main thread
        Handler(Looper.getMainLooper()).post { //                    JSONObject data = (JSONObject) args[0];
            Log.e("onConnectUserListener", args[0].toString())
            for (observer in observerList!!) {
                observer.onSocketCall(CONNECT_LISTENER, *args)
            }
        }
    }

    /********************************************************************************/
    private val onConnectListener = Emitter.Listener { args ->
        // Get a handler that can be used to post to the main thread
        Log.e("onConnectListener", "Socket connected: ${mSocket?.id()}")
        Handler(Looper.getMainLooper()).post {
            Log.e(TAG, "socket connected")
            isConnect = true

            try {

                val userId = getUser(App.context!!)?.id.toString()

                if (userId.isNotEmpty()) {
                    val jsonObject = JSONObject()
                    jsonObject.put("user_id", userId)
                    sendDataToServer(CONNECT_USER, jsonObject)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            for (observer in observerList!!) {
                observer.onSocketConnect(*args)
            }
        }
    }

    private val onDisConnect = Emitter.Listener { args ->
        // Get a handler that can be used to post to the main thread
        Handler(Looper.getMainLooper()).post {
            Log.e(TAG, "socket disConnected")
            isConnect = false
            //                    if (mSocketInterface!=null)
//                        mSocketInterface.onSocketDisConnect(args);
            for (observer in observerList!!) {
                observer.onSocketDisConnect(*args)
            }
        }
    }

    private val onConnectError =
        Emitter.Listener { args -> // Get a handler that can be used to post to the main thread
            Handler(Looper.getMainLooper()).post {
                for (observer in observerList!!) {
                    observer.onError("ERROR", *args)
                }
                Log.e(TAG, "Run" + args[0])
                Log.e(TAG, "socket Error connecting")
//                if (!checkStringNull(MyApplication.instance!!.getString(Constants.AuthKey)))
//                    initSocket()
            }
        }


    fun onRegister(observer: SocketInterface) {
        if (observerList != null && !observerList!!.contains(observer)) {
            observerList!!.add(observer)
        } else {
            observerList = ArrayList()
            observerList!!.add(observer)
        }
    }

    fun unRegister(observer: SocketInterface) {
        if (!observerList.isNullOrEmpty()) {
            for (i in observerList!!.indices) {
                /*if (!checkObjectNull(observerList!![i])) {*/
                if (i < observerList!!.size) {
                    val model = observerList!![i]
                    if (model === observer) {
                        observerList!!.remove(model)
                    }
                }
                /*  }*/
            }
        }
    }

    private val onSendMsgListener = Emitter.Listener { args ->
        Handler(Looper.getMainLooper()).post {
            for (observer in observerList!!) {
                observer.onSocketCall(SEND_MSG_LISTENER, args)
            }
        }
    }

    private val onGetMsgListener = Emitter.Listener { args ->
        Handler(Looper.getMainLooper()).post {
            for (observer in observerList!!) {
                observer.onSocketCall(GET_MSG_LISTENER, args)
            }
        }
    }

    private val onChatListingListener = Emitter.Listener { args ->
        Handler(Looper.getMainLooper()).post {
            for (observer in observerList!!) {
                observer.onSocketCall(CHAT_LISTING_LISTENER, args)
            }
        }
    }

    private val onReadUnreadListener = Emitter.Listener { args ->
        Handler(Looper.getMainLooper()).post{
            for (observer in observerList!!){
                observer.onSocketCall(READ_UNREAD_LISTENER,args)
            }
        }
    }

    interface SocketInterface {
        fun onSocketCall(event: String?, vararg args: Any?)
        fun onSocketConnect(vararg args: Any?)
        fun onSocketDisConnect(vararg args: Any?)
        fun onError(event: String?, vararg args: Any?)
    }

}