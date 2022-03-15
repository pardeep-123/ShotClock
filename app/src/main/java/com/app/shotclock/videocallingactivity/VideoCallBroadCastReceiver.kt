package com.app.shotclock.videocallingactivity

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.app.shotclock.R
import org.json.JSONObject


class VideoCallBroadCastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent!!.getStringExtra("type") == "call") {
            val acceptCallIntent = Intent(context, IncomingCallActivity::class.java)
            acceptCallIntent.putExtra("channelName", intent.getStringExtra("channelName").toString())
            acceptCallIntent.putExtra("agoraToken", intent.getStringExtra("agoraToken").toString())
            acceptCallIntent.putExtra("requestId", intent.getStringExtra("requestId").toString())
            acceptCallIntent.putExtra("callerName", intent.getStringExtra("callerName").toString())
            acceptCallIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context!!.startActivity(acceptCallIntent)

            /*val videoCall = AgoraVideoCall(
                context,
                context!!.getString(R.string.agora_app_id),
                context!!.getString(R.string.agora_access_token),
                "demo"
            )
            val uiConfig = UIConfig()
            uiConfig.hideVideoMute()
            videoCall.setConfig(uiConfig)

            val intent2: Intent = videoCall.start()
            intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ContextCompat.startActivity(context, intent2, Bundle())*/
        }else
            if (intent.getStringExtra("type") == "reject") {
                if (AppUtils.isNetworkConnected()) {

                    val jsonObject = JSONObject()
                    jsonObject.put("requestId",  intent.getStringExtra("channelName").toString())
                    jsonObject.put("status", "2")
                    AppController.mInstance.getSocketManager()?.acceptRejectCall(jsonObject)
                }
                else
                    Toast.makeText(context, context!!.resources.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show()
            }

        val notificationId = intent.getIntExtra("notificationId", 1)

        val manager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(notificationId)

        val closeIntent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context.sendBroadcast(closeIntent)
    }
}