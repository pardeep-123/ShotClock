package com.app.shotclock.models
import com.google.gson.annotations.SerializedName


data class GetNotificationResponse(
    @SerializedName("body")
    var body: ArrayList<GetNotificationBody>,
    @SerializedName("code")
    var code: Int, // 200
    @SerializedName("message")
    var message: String, // Notifications get successfully
    @SerializedName("success")
    var success: Int // 1
) {
    data class GetNotificationBody(
        @SerializedName("comment_id")
        var commentId: Any?, // null
        @SerializedName("created_at")
        var createdAt: String, // 2021-10-26T06:48:32.000Z
        @SerializedName("data")
        var `data`: String, // Rat send you request
        @SerializedName("from_id")
        var fromId: Int, // 267
        @SerializedName("groupName")
        var groupName: String, // d0bd1c28dff35ebbbe5ca644b2d4810b91512707
        @SerializedName("id")
        var id: Int, // 592
        @SerializedName("message_id")
        var messageId: Any?, // null
        @SerializedName("post_id")
        var postId: Any?, // null
        @SerializedName("read_status")
        var readStatus: Int, // 0
        @SerializedName("reply_id")
        var replyId: Any?, // null
        @SerializedName("request")
        var request: NotificationRequest,
        @SerializedName("request_id")
        var requestId: Int, // 559
        @SerializedName("to_id")
        var toId: Int, // 261
        @SerializedName("type")
        var type: Int // 1
    ) {
        data class NotificationRequest(
            @SerializedName("callStart")
            var callStart: Int, // 0
            @SerializedName("callStatus")
            var callStatus: Int, // 0
            @SerializedName("createdAt")
            var createdAt: String, // 2021-10-26T06:48:32.000Z
            @SerializedName("groupName")
            var groupName: String, // d0bd1c28dff35ebbbe5ca644b2d4810b91512707
            @SerializedName("id")
            var id: Int, // 559
            @SerializedName("isCancelledByAdmin")
            var isCancelledByAdmin: Int, // 0
            @SerializedName("requestBy")
            var requestBy: NotificationRequestBy,
            @SerializedName("request_to")
            var requestTo: String, // 261
            @SerializedName("status")
            var status: Int, // 1
            @SerializedName("updatedAt")
            var updatedAt: String, // 2021-10-26T06:48:32.000Z
            @SerializedName("userId")
            var userId: Int // 267
        ) {
            data class NotificationRequestBy(
                @SerializedName("id")
                var id: Int, // 267
                @SerializedName("profile_image")
                var profileImage: String, // /uploads/user_images/1635229215573-file.jpeg
                @SerializedName("username")
                var username: String // Rat
            )
        }
    }
}