package com.app.shotclock.models.sockets
import com.google.gson.annotations.SerializedName


data class VideoCallStatusResponse(
    @SerializedName("channelName")
    var channelName: String, // 67db5751b80deb340b42c856a868b8ef30ad2f73
    @SerializedName("groupName")
    var groupName: String, // singleCall
    @SerializedName("message")
    var message: String, // Connected
    @SerializedName("messageType")
    var messageType: Int, // 1
    @SerializedName("notificationTitle")
    var notificationTitle: String, // Call connected
    @SerializedName("receiverId")
    var receiverId: Int, // 274
    @SerializedName("receiverImage")
    var receiverImage: String, // /uploads/profile/1646913466296-file.jpg
    @SerializedName("receiverName")
    var receiverName: String, // malvika jasswall
    @SerializedName("requestId")
    var requestId: Int, // 0
    @SerializedName("senderId")
    var senderId: Int, // 275
    @SerializedName("senderImage")
    var senderImage: String, // /uploads/user_images/1647319884537-file.jpeg
    @SerializedName("senderName")
    var senderName: String, // aashi
    @SerializedName("status")
    var status: Int, // 1
    @SerializedName("title")
    var title: String, // Shot Clock
    @SerializedName("videoToken")
    var videoToken: String // 0062e3700ef5e0648be8fc20e03324c5651IABzmHJGlRFEFwDNuJ37D9q/fiSLj+fQH7tKqiOWAGyDe1Pk23b7jBoeIgDiCfuoEwozYgQAAQCjxjFiAgCjxjFiAwCjxjFiBACjxjFi
)