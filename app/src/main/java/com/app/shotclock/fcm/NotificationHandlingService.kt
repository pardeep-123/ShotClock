package com.app.shotclock.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.shotclock.R
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.utils.Constants
import com.app.shotclock.videocallingactivity.IncomingCallActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class NotificationHandlingService : FirebaseMessagingService() {
    private val TAG = "FireBasePush"
    private var i = 0
    private var title = ""
    private var message: String? = ""
    private var CHANNEL_ID = "ShotClock"
    private var senderId = ""
    private var senderName = ""
    private var notificationCode = ""
    private lateinit var soundUri: Uri

    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)
        Log.e(TAG, "Refreshed token: $refreshedToken")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.e(TAG, "Notification: ${remoteMessage.data}")

        try {
            message = remoteMessage.data["message"]
            notificationCode = remoteMessage.data["notification_code"]!!.toString()
            title = remoteMessage.data["title"]!!

            if (notificationCode =="20"){
                val intent = Intent(this, IncomingCallActivity::class.java)
                intent.putExtra("channelName", JSONObject(remoteMessage.data["body"]!!).get("channelName").toString())
                intent.putExtra("receiverName", JSONObject(remoteMessage.data["body"]!!).get("senderName").toString())
                intent.putExtra("senderId", JSONObject(remoteMessage.data["body"]!!).get("senderId").toString())
                intent.putExtra("token", JSONObject(remoteMessage.data["body"]!!).get("videoToken").toString())

                makePush(intent)

            }else{
                title = remoteMessage.data["title"]!!
                senderId = remoteMessage.data["sender_id"]!!
                senderName = remoteMessage.data["sender_name"]!!
                val intent = Intent(applicationContext, HomeActivity::class.java)
                intent.putExtra("notification_code", notificationCode)
                intent.putExtra("sender_id", senderId)
                intent.putExtra("sender_name", senderName)

//                if (Constants.user2Id != senderId || !Constants.OnMessageScreen)
                    makePush(intent)
            }


        } catch (e: Exception) {
        }
    }

    private fun makePush(intent: Intent?) {

        val pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT)

        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val channelId = CHANNEL_ID
        soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(notificationIcon)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_app_logo))
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
//            .setColor(ContextCompat.getColor(this, R.color.pink))
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_ALL)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "ShotClock", NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = Color.MAGENTA
            channel.setShowBadge(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    private val notificationIcon: Int
        get() {
            val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            return if (useWhiteIcon) R.drawable.ic_trans else R.drawable.ic_app_logo
        }
}