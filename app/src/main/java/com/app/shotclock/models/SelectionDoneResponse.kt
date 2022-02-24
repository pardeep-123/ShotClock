package com.app.shotclock.models
import com.google.gson.annotations.SerializedName


data class SelectionDoneResponse(
    @SerializedName("body")
    var body: ArrayList<SelectionDoneBody>,
    @SerializedName("code")
    var code: Int, // 200
    @SerializedName("message")
    var message: String, // selection data
    @SerializedName("success")
    var success: Int // 1
) {
    data class SelectionDoneBody(
        @SerializedName("callStart")
        var callStart: Int, // 0
        @SerializedName("callStatus")
        var callStatus: Int, // 0
        @SerializedName("createdAt")
        var createdAt: String, // 2022-02-24T10:45:09.000Z
        @SerializedName("groupName")
        var groupName: String, // 568d579a11ed3b6221b396c51efeb3d47c9bcf78
        @SerializedName("id")
        var id: Int, // 1175
        @SerializedName("isCancelledByAdmin")
        var isCancelledByAdmin: Int, // 0
        @SerializedName("request_to")
        var requestTo: String, // 262
        @SerializedName("status")
        var status: Int, // 1
        @SerializedName("updatedAt")
        var updatedAt: String, // 2022-02-24T10:45:09.000Z
        @SerializedName("userId")
        var userId: Int // 320
    )
}