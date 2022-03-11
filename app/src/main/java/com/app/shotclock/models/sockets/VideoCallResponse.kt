package com.app.shotclock.models.sockets
import com.google.gson.annotations.SerializedName


data class VideoCallResponse(
    @SerializedName("callType")
    var callType: String, // 0
    @SerializedName("channelName")
    var channelName: String, // 46a51428272bd3ad181fec180bc2225ce907dbb4
    @SerializedName("groupName")
    var groupName: String, // singleCall
    @SerializedName("message")
    var message: String, // Calling
    @SerializedName("messageType")
    var messageType: Int, // 0
    @SerializedName("notificationTitle")
    var notificationTitle: String, // You have a new video call
    @SerializedName("receiverId")
    var receiverId: Int, // 274
    @SerializedName("receiverName")
    var receiverName: String, // malvika jasswall
    @SerializedName("requestId")
    var requestId: Int, // 0
    @SerializedName("seconds")
    var seconds: Int, // 300
    @SerializedName("senderId")
    var senderId: Int, // 275
    @SerializedName("senderImage")
    var senderImage: String, // /uploads/profile/1646978914279-file.jpg
    @SerializedName("senderName")
    var senderName: String, // aashi
    @SerializedName("status")
    var status: Int, // 0
    @SerializedName("title")
    var title: String, // Shot Clock
    @SerializedName("videoToken")
    var videoToken: String // 0062e3700ef5e0648be8fc20e03324c5651IAArReavWAG8YZYuNm81CINCQtu0K7G8s+UYYdnc5Anxb1ORoCz7jBoeIgDiCfuoKYEsYgQAAQC5PStiAgC5PStiAwC5PStiBAC5PSti
)