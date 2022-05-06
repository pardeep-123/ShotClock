package com.app.shotclock.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class RequestListResponseModel(
    @SerializedName("body")
    var body: ArrayList<RequestListResponseBody>,
    @SerializedName("code")
    var code: Int, // 200
    @SerializedName("message")
    var message: String, // data get successfully
    @SerializedName("success")
    var success: Int // 1
):Serializable {
    data class RequestListResponseBody(
        var isSelected:Boolean=false,
        @SerializedName("bio")
        var bio: String, // Gshnskakal
        @SerializedName("callStart")
        var callStart: Int, // 0
        @SerializedName("callStatus")
        var callStatus: Int, // 0
        @SerializedName("created_at")
        var createdAt: String, // 2022-02-24T12:37:59.000Z
        @SerializedName("email")
        var email: String, // tom@yopmaip.com
        @SerializedName("groupName")
        var groupName: String, // 102ed9167b99d7a907a9f842c778750d29504d7b
        @SerializedName("id")
        var id: Int, // 1180
        @SerializedName("isCancelledByAdmin")
        var isCancelledByAdmin: Int, // 0
        @SerializedName("is_online")
        var isOnline: String, // 1
        @SerializedName("profile_image")
        var profileImage: String, // /uploads/user_images/1635228768909-file.jpeg
        @SerializedName("request_count")
        var requestCount: Int, // 0
        @SerializedName("request_to")
        var requestTo: String, // 264
        @SerializedName("status")
        var status: Int, // 1
        @SerializedName("updated_at")
        var updatedAt: String, // 2022-02-24T12:37:59.000Z
        @SerializedName("user_id")
        var userId: Int, // 320
        @SerializedName("username")
        var username: String // tom
    ):Serializable
}