package com.app.shotclock.models
import com.google.gson.annotations.SerializedName


data class SubscriptionPlansResponse(
    @SerializedName("body")
    var body: ArrayList<SubscriptionPlansBody>,
    @SerializedName("code")
    var code: Int, // 200
    @SerializedName("message")
    var message: String, // data get successfully
    @SerializedName("success")
    var success: Int // 1
) {
    data class SubscriptionPlansBody(
        @SerializedName("amount")
        var amount: String, // 3.49
        @SerializedName("created_at")
        var createdAt: String, // 2021-08-16T08:20:22.000Z
        @SerializedName("duration")
        var duration: String, // 3.499
        @SerializedName("id")
        var id: Int, // 1
        @SerializedName("name")
        var name: String, // monthly
        @SerializedName("status")
        var status: Int, // 1
        @SerializedName("updated_at")
        var updatedAt: String // 2021-08-16T08:20:22.000Z
    )
}