package com.app.shotclock.models.sockets
import com.google.gson.annotations.SerializedName


class ChatHistoryResponse : ArrayList<ChatHistoryResponse.ChatHistoryResponseItem>(){
    data class ChatHistoryResponseItem(
        @SerializedName("created")
        var created: Int, // 1635243713
        @SerializedName("id")
        var id: Int, // 307
        @SerializedName("message")
        var message: String, // Vm vm 
        @SerializedName("messageType")
        var messageType: Int, // 0
        @SerializedName("ReceiverId")
        var receiverId: Int, // 275
        @SerializedName("ReceiverImage")
        var receiverImage: String, // /uploads/user_images/1635234340883-file.jpeg
        @SerializedName("ReceiverName")
        var receiverName: String, // aashi
        @SerializedName("SenderID")
        var senderID: Int, // 278
        @SerializedName("SenderImage")
        var senderImage: String, // https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=4060328434089724&height=200&width=200&ext=1637835163&hash=AeSf8d8jvTFfQI4DhzQ
        @SerializedName("SenderName")
        var senderName: String // Priyanka
    )
}