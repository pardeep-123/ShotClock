package com.app.shotclock.models
import com.google.gson.annotations.SerializedName


data class SelectionDoneRequestModel(
    @SerializedName("user_list")
    var userList: ArrayList<SelectionDoneUser>
) {
    data class SelectionDoneUser(
        @SerializedName("user_id")
        var userId: String // 262
    )
}