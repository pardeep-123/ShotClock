package com.app.shotclock.models
import com.google.gson.annotations.SerializedName


data class IceBreakerQuestionResponse(
    @SerializedName("body")
    var body: ArrayList<IceBreakerBody>,
    @SerializedName("code")
    var code: Int, // 200
    @SerializedName("message")
    var message: String, // data get successfully
    @SerializedName("success")
    var success: Int // 1
) {
    data class IceBreakerBody(
        @SerializedName("answer")
        var answer: String,
        @SerializedName("created_at")
        var createdAt: String, // 2022-01-06T11:15:32.000Z
        @SerializedName("id")
        var id: Int, // 4
        @SerializedName("question")
        var question: String, // Describe something you’ve done that you’re really proud of. What is your greatest accomplishment?
        @SerializedName("status")
        var status: Int, // 0
        @SerializedName("updated_at")
        var updatedAt: String // 2022-01-06T11:15:32.000Z
    )
}