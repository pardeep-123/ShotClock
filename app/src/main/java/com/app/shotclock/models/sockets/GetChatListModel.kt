package com.app.shotclock.models.sockets
import com.google.gson.annotations.SerializedName

class GetChatListModel : ArrayList<GetChatListModel.GetChatListModelItem>(){
    data class GetChatListModelItem(
        @SerializedName("created")
        var created: Int, // 1635234964
        @SerializedName("createdAt")
        var createdAt: String, // 2021-10-26T07:56:04.000Z
        @SerializedName("created_ats")
        var createdAts: Int, // 1635234964
        @SerializedName("deletedId")
        var deletedId: Int, // 0
        @SerializedName("id")
        var id: Int, // 25
        @SerializedName("lastMessage")
        var lastMessage: String, // /uploads/message/1635234964.png
        @SerializedName("lastMessageId")
        var lastMessageId: Int, // 292
        @SerializedName("messageType")
        var messageType: Int, // 1
        @SerializedName("type")
        var type: Int, // 1
        @SerializedName("unreadcount")
        var unreadcount: Int, // 0
        @SerializedName("updated")
        var updated: Any?, // null
        @SerializedName("updatedAt")
        var updatedAt: String, // 2021-10-26T07:56:04.000Z
        @SerializedName("userImage")
        var userImage: String, // /uploads/user_images/1635234179015-file.jpeg
        @SerializedName("userName")
        var userName: String, // malvika
        @SerializedName("userOne")
        var userOne: Int, // 275
        @SerializedName("userTwo")
        var userTwo: Int, // 274
        @SerializedName("userdetail_id")
        var userdetailId: Int // 274
    )
}